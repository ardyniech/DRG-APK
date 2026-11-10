package com.example.core.update

import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Primitif format BSDIFF40 (bsdiff/bspatch) untuk updater delta OTA.
 *
 * Header 32 byte: magic "BSDIFF40" + ctrlLen + diffLen + newSize (int64
 * sign-magnitude little-endian). Tiga blok setelahnya (ctrl, diff, extra)
 * dikompresi bzip2 — lihat [DeltaPatchApplier].
 */
internal object Bsdiff40 {

    val MAGIC: ByteArray = "BSDIFF40".toByteArray(Charsets.US_ASCII)

    /** Decode int64 sign-magnitude little-endian khas bsdiff. */
    fun offtin(buf: ByteArray, off: Int): Long {
        var y = (buf[off + 7].toInt() and 0x7F).toLong()
        for (i in 6 downTo 0) y = (y shl 8) + (buf[off + i].toLong() and 0xFF)
        return if (buf[off + 7].toInt() and 0x80 != 0) -y else y
    }

    /** Encode int64 sign-magnitude little-endian (dipakai generator/test). */
    fun offtout(x: Long): ByteArray {
        val buf = ByteArray(8)
        var y = x
        if (y < 0) {
            buf[7] = 0x80.toByte()
            y = -y
        }
        for (i in 0..6) {
            buf[i] = (y and 0xFF).toByte()
            y = y shr 8
        }
        return buf
    }

    /** Baca tepat [len] byte; false kalau stream habis lebih awal. */
    fun readFully(input: InputStream, buf: ByteArray, len: Int): Boolean {
        var read = 0
        while (read < len) {
            val n = input.read(buf, read, len - read)
            if (n < 0) return false
            read += n
        }
        return true
    }

    /** Baca dari file di posisi [pos]; area di luar EOF dianggap nol. */
    fun readAt(raf: RandomAccessFile, pos: Long, buf: ByteArray, len: Int): Int {
        if (pos < 0) return 0
        raf.seek(pos)
        var read = 0
        while (read < len) {
            val n = raf.read(buf, read, len - read)
            if (n < 0) break
            read += n
        }
        return read
    }
}

/** InputStream yang hanya membaca [length] byte mulai [offset] dari file. */
internal class FileSliceStream(
    file: File,
    offset: Long,
    private val length: Long
) : InputStream() {

    private val raf = RandomAccessFile(file, "r").also { it.seek(offset) }
    private var remaining = length

    override fun read(): Int {
        if (remaining <= 0) return -1
        val b = raf.read()
        if (b >= 0) remaining--
        return b
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (remaining <= 0) return -1
        val n = raf.read(b, off, minOf(len.toLong(), remaining).toInt())
        if (n > 0) remaining -= n
        return n
    }

    override fun close() {
        raf.close()
    }
}
