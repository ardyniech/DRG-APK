package com.example

import com.example.core.update.Bsdiff40
import com.example.core.update.DeltaPatchApplier
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Verifikasi bspatch murni JVM: patch BSDIFF40 yang valid harus menghasilkan
 * output byte-identik (syarat signature APK tetap sah).
 */
class DeltaPatchApplierTest {

    @get:Rule
    val tmp = TemporaryFolder()

    @Test
    fun offtinOfftoutRoundTrip() {
        val values = listOf(0L, 1L, 255L, 4096L, 123456789L, -1L, -987654321L)
        for (value in values) {
            assertEquals(value, Bsdiff40.offtin(Bsdiff40.offtout(value), 0))
        }
    }

    @Test
    fun applyReconstructsNewBytesWithDiffAndExtraBlocks() {
        val old = "Hello World".toByteArray()
        val new = "Hello Brave World".toByteArray()
        // (x=6 diff, y=6 extra, z=0) lalu (x=5 diff, y=0, z=0)
        val ctrl = ByteArrayOutputStream().apply {
            write(Bsdiff40.offtout(6)); write(Bsdiff40.offtout(6)); write(Bsdiff40.offtout(0))
            write(Bsdiff40.offtout(5)); write(Bsdiff40.offtout(0)); write(Bsdiff40.offtout(0))
        }.toByteArray()
        val patch = buildPatch(new.size.toLong(), ctrl, ByteArray(11), "Brave ".toByteArray())

        val output = applyPatch(old, patch)
        assertArrayEquals(new, output)
    }

    @Test
    fun applyHandlesLargeExtraOnlyPayload() {
        val old = ByteArray(4096) { (it % 251).toByte() }
        val new = ByteArray(200_000) { (it * 7 % 253).toByte() }
        val ctrl = ByteArrayOutputStream().apply {
            write(Bsdiff40.offtout(0)); write(Bsdiff40.offtout(new.size.toLong())); write(Bsdiff40.offtout(0))
        }.toByteArray()
        val patch = buildPatch(new.size.toLong(), ctrl, ByteArray(0), new)

        val output = applyPatch(old, patch)
        assertArrayEquals(new, output)
    }

    @Test
    fun applyRejectsNonBsdiffFile() {
        val patch = tmp.newFile("bad.patch").apply { writeBytes(ByteArray(64) { 7 }) }
        val old = tmp.newFile("old.apk").apply { writeBytes(ByteArray(16)) }
        val out = File(tmp.root, "out.apk")
        val failed = runCatching { DeltaPatchApplier.apply(old, patch, out) }.isFailure
        assertTrue("Patch tanpa magic harus ditolak", failed)
    }

    @Test
    fun applyRejectsAbsurdOutputSize() {
        val ctrl = ByteArrayOutputStream().apply {
            write(Bsdiff40.offtout(0)); write(Bsdiff40.offtout(0)); write(Bsdiff40.offtout(0))
        }.toByteArray()
        val patch = buildPatch(500L * 1024 * 1024, ctrl, ByteArray(0), ByteArray(0))
        val old = tmp.newFile().apply { writeBytes(ByteArray(16)) }
        val patchFile = tmp.newFile().apply { writeBytes(patch) }
        val failed = runCatching {
            DeltaPatchApplier.apply(old, patchFile, File(tmp.root, "huge.apk"))
        }.isFailure
        assertTrue("Ukuran output tidak wajar harus ditolak", failed)
    }

    private fun applyPatch(old: ByteArray, patch: ByteArray): ByteArray {
        val oldFile = tmp.newFile().apply { writeBytes(old) }
        val patchFile = tmp.newFile().apply { writeBytes(patch) }
        val outFile = File(tmp.root, "out-${System.nanoTime()}.apk")
        DeltaPatchApplier.apply(oldFile, patchFile, outFile)
        return outFile.readBytes()
    }

    private fun buildPatch(newSize: Long, ctrl: ByteArray, diff: ByteArray, extra: ByteArray): ByteArray {
        val ctrlZ = bzip2(ctrl)
        val diffZ = bzip2(diff)
        val extraZ = bzip2(extra)
        val out = ByteArrayOutputStream()
        out.write(Bsdiff40.MAGIC)
        out.write(Bsdiff40.offtout(ctrlZ.size.toLong()))
        out.write(Bsdiff40.offtout(diffZ.size.toLong()))
        out.write(Bsdiff40.offtout(newSize))
        out.write(ctrlZ)
        out.write(diffZ)
        out.write(extraZ)
        return out.toByteArray()
    }

    private fun bzip2(data: ByteArray): ByteArray {
        val out = ByteArrayOutputStream()
        BZip2CompressorOutputStream(out).use { it.write(data) }
        return out.toByteArray()
    }
}
