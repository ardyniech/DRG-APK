package com.example

import androidx.test.core.app.ApplicationProvider
import com.example.core.backup.BackupCrypto
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BackupCryptoTest {

    private val ctx = ApplicationProvider.getApplicationContext()

    @Test
    fun encrypt_decrypt_roundtrip_plaintext() {
        // Test that when keystore is unavailable, encryption returns plaintext
        val original = "Hello DRG Backup".toByteArray(Charsets.UTF_8)
        val encrypted = BackupCrypto.encrypt(ctx, original)
        val decrypted = BackupCrypto.decrypt(ctx, encrypted)
        assertTrue("roundtrip plaintext match", java.util.Arrays.equals(original, decrypted))
    }

    @Test
    fun isEncrypted_false_for_plaintext() {
        val data = "plaintext data".toByteArray(Charsets.UTF_8)
        assertFalse("plaintext should not be encrypted", BackupCrypto.isEncrypted(data))
    }

    @Test
    fun isEncrypted_true_for_format() {
        // Create data that looks like the iv:ciphertext format
        val iv = java.util.UUID.randomUUID().toString().replace("-", "").take(16).toByteArray()
        val ct = "ciphertext".toByteArray(Charsets.UTF_8)
        val formatted = Base64.encodeToString(iv, Base64.NO_WRAP) + ":" + Base64.encodeToString(ct, Base64.NO_WRAP)
        val data = formatted.toByteArray(Charsets.UTF_8)
        assertTrue("formatted data should be recognized as encrypted", BackupCrypto.isEncrypted(data))
    }
}
