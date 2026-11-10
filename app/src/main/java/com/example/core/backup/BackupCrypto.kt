package com.example.core.backup

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Enkripsi/dekripsi file backup dengan Android Keystore (AES-GCM).
 * Format terenkripsi: <iv_b64>:<ciphertext_b64>. Fallback plaintext bila Keystore gagal.
 */
object BackupCrypto {

    private const val ALIAS = "drg_backup_key"
    private const val IV_BYTES = 12
    private const val TAG_BITS = 128

    fun encrypt(context: Context, plain: ByteArray): ByteArray {
        val key = getOrCreateKey() ?: return plain
        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val iv = cipher.iv
            val ct = cipher.doFinal(plain)
            (Base64.encodeToString(iv, Base64.NO_WRAP) + ":" +
                Base64.encodeToString(ct, Base64.NO_WRAP)).toByteArray(Charsets.UTF_8)
        } catch (e: Exception) {
            plain
        }
    }

    fun decrypt(context: Context, data: ByteArray): ByteArray {
        val key = getKey() ?: return data
        val text = data.toString(Charsets.UTF_8)
        val parts = text.split(":", limit = 2)
        if (parts.size != 2) return data
        return try {
            val iv = Base64.decode(parts[0], Base64.NO_WRAP)
            val ct = Base64.decode(parts[1], Base64.NO_WRAP)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_BITS, iv))
            cipher.doFinal(ct)
        } catch (e: Exception) {
            data
        }
    }

    fun isEncrypted(data: ByteArray): Boolean {
        val text = data.toString(Charsets.UTF_8)
        val parts = text.split(":", limit = 2)
        return parts.size == 2 && parts[0].length == 16 && parts[1].length > 16
    }

    private fun getOrCreateKey(): SecretKey? = try {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        ks.getKey(ALIAS, null) as? SecretKey ?: run {
            val gen = KeyGenerator.getInstance("AES", "AndroidKeyStore")
            gen.init(
                KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
            gen.generateKey()
        }
    } catch (e: Exception) {
        null
    }

    private fun getKey(): SecretKey? = try {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        ks.getKey(ALIAS, null) as? SecretKey
    } catch (e: Exception) {
        null
    }
}
