package com.example.core.update

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Bspatch murni JVM (tanpa NDK): terapkan patch BSDIFF40 ke APK lama.
 *
 * Hasilnya byte-identik dengan APK baru yang ditandatangani CI, jadi signature
 * v2/v3 tetap valid dan Android menerima install tanpa re-sign di HP.
 */
object DeltaPatchApplier {

    class PatchException(message: String) : Exception(message)

    /** Batas wajar ukuran APK hasil patch (jaga-jaga patch korup/berbahaya). */
    private const val MAX_OUTPUT_BYTES = 400L * 1024 * 1024

    /** Terapkan [patchFile] ke [oldApk], tulis ke [output]. */
    fun apply(oldApk: File, patchFile: File, output: File): File {
        val header = ByteArray(32)
        RandomAccessFile(patchFile, "r").use { it.readFully(header) }
        if (!header.copyOfRange(0, 8).contentEquals(Bsdiff40.MAGIC)) {
            throw PatchException("Bukan patch BSDIFF40")
        }
        val ctrlLen = Bsdiff40.offtin(header, 8)
        val diffLen = Bsdiff40.offtin(header, 16)
        val newSize = Bsdiff40.offtin(header, 24)
        if (ctrlLen <= 0 || diffLen < 0 || newSize <= 0) throw PatchException("Header patch rusak")
        // Patch korup bisa mengklaim ukuran output raksasa → jangan sampai mengisi disk HP.
        if (newSize > MAX_OUTPUT_BYTES) throw PatchException("Ukuran hasil patch tidak wajar")

        val diffStart = 32L + ctrlLen
        val extraStart = diffStart + diffLen
        val extraLen = patchFile.length() - extraStart
        if (extraLen < 0) throw PatchException("Blok patch terpotong")

        FileSliceStream(patchFile, 32L, ctrlLen).use { rawCtrl ->
            BZip2CompressorInputStream(rawCtrl).use { ctrl ->
                FileSliceStream(patchFile, diffStart, diffLen).use { rawDiff ->
                    BZip2CompressorInputStream(rawDiff).use { diff ->
                        FileSliceStream(patchFile, extraStart, extraLen).use { rawExtra ->
                            BZip2CompressorInputStream(rawExtra).use { extra ->
                                writePatched(oldApk, output, ctrl, diff, extra, newSize)
                            }
                        }
                    }
                }
            }
        }
        return output
    }

    private fun writePatched(
        oldApk: File,
        output: File,
        ctrl: InputStream,
        diff: InputStream,
        extra: InputStream,
        newSize: Long
    ) {
        RandomAccessFile(oldApk, "r").use { old ->
            BufferedOutputStream(FileOutputStream(output), 1 shl 16).use { out ->
                val triple = ByteArray(24)
                val oldBuf = ByteArray(1 shl 15)
                val newBuf = ByteArray(1 shl 15)
                var oldPos = 0L
                var newPos = 0L

                while (newPos < newSize) {
                    if (!Bsdiff40.readFully(ctrl, triple, 24)) throw PatchException("Ctrl block habis")
                    var x = Bsdiff40.offtin(triple, 0)
                    val y = Bsdiff40.offtin(triple, 8)
                    val z = Bsdiff40.offtin(triple, 16)
                    if (newPos + x > newSize) x = newSize - newPos

                    var done = 0L
                    while (done < x) {
                        val chunk = minOf(x - done, oldBuf.size.toLong()).toInt()
                        val oldRead = Bsdiff40.readAt(old, oldPos + done, oldBuf, chunk)
                        if (!Bsdiff40.readFully(diff, newBuf, chunk)) throw PatchException("Diff block habis")
                        for (i in 0 until chunk) {
                            val base = if (i < oldRead) oldBuf[i].toInt() else 0
                            newBuf[i] = (newBuf[i].toInt() + base).toByte()
                        }
                        out.write(newBuf, 0, chunk)
                        done += chunk
                    }
                    newPos += x
                    oldPos += x

                    if (y > 0) {
                        var left = y
                        while (left > 0) {
                            val chunk = minOf(left, newBuf.size.toLong()).toInt()
                            if (!Bsdiff40.readFully(extra, newBuf, chunk)) throw PatchException("Extra block habis")
                            out.write(newBuf, 0, chunk)
                            left -= chunk
                        }
                        newPos += y
                    }
                    oldPos += z
                }
            }
        }
    }
}
