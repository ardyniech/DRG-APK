package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.security.PinHasher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PinHasherTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun hashIsV2FormatAndVerifies() {
        val stored = PinHasher.hashPin(context, "1234")
        assertTrue("format v2", stored.startsWith("v2:"))
        assertTrue("verify benar", PinHasher.verifyPin(context, "1234", stored))
        assertFalse("verify salah", PinHasher.verifyPin(context, "0000", stored))
    }

    @Test
    fun saltIsUniquePerCall() {
        val a = PinHasher.hashPin(context, "1234")
        val b = PinHasher.hashPin(context, "1234")
        assertTrue("hash berbeda (salt acak)", a != b)
        // keduanya tetap verifiable
        assertTrue(PinHasher.verifyPin(context, "1234", a))
        assertTrue(PinHasher.verifyPin(context, "1234", b))
    }

    @Test
    fun emptyPinRejected() {
        assertFalse(PinHasher.verifyPin(context, "", "v2:x:x:x:sw"))
        var thrown = false
        try { PinHasher.hashPin(context, "") } catch (e: IllegalArgumentException) { thrown = true }
        assertTrue("hash empty throws", thrown)
    }

    @Test
    fun legacyV1Sha256StillVerifies() {
        val legacy = "v1:" + sha256Hex("1234")
        assertTrue(PinHasher.verifyPin(context, "1234", legacy))
        assertFalse(PinHasher.verifyPin(context, "9999", legacy))
    }

    @Test
    fun legacyBareHexStillVerifies() {
        val bare = sha256Hex("1234")
        assertTrue(PinHasher.verifyPin(context, "1234", bare))
        assertFalse(PinHasher.verifyPin(context, "9999", bare))
    }

    @Test
    fun needsMigrationFlagsLegacyAndSoftwareMode() {
        assertTrue(PinHasher.needsMigration("v1:" + sha256Hex("1234")))
        assertTrue(PinHasher.needsMigration(sha256Hex("1234")))
        val sw = PinHasher.hashPinSoftware("1234")
        assertTrue(PinHasher.needsMigration(sw))
    }

    @Test
    fun corruptedStoredReturnsFalse() {
        assertFalse(PinHasher.verifyPin(context, "1234", "bukan-hash"))
        assertFalse(PinHasher.verifyPin(context, "1234", null))
    }

    private fun sha256Hex(input: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
