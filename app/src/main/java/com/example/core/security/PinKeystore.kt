package com.example.core.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Bungkus/lepas derived key dengan Android Keystore (AES-GCM, non-exportable).
 * Dipisah dari PinHasher agar tiap file tetap < 125 baris.
 */
object PinKeystore {

    private const val ALIAS = "drg_pin_wrap_key"
    private const val GCM_IV_BYTES = 12
    private const val GCM_TAG_BITS = 128

    /** Buat/ambil kunci Keystore. Null bila Keystore tidak tersedia. */
    fun getOrCreateKey(): SecretKey? = try {
        val ks = java.security.KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        ks.getKey(ALIAS, null) as? SecretKey ?: run {
            val gen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            gen.init(
                KeyGenParameterSpec.Builder(
                    ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
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

    fun getKey(): SecretKey? = try {
        val ks = java.security.KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        ks.getKey(ALIAS, null) as? SecretKey
    } catch (e: Exception) {
        null
    }

    /** Bungkus bytes → iv(12)+ciphertext. Pair(payload, usedKeystore). */
    fun wrap(derived: ByteArray): Pair<ByteArray, Boolean> {
        val key = getOrCreateKey() ?: return Pair(derived, false)
        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val iv = cipher.iv
            val ct = cipher.doFinal(derived)
            val payload = ByteArray(iv.size + ct.size)
            System.arraycopy(iv, 0, payload, 0, iv.size)
            System.arraycopy(ct, 0, payload, iv.size, ct.size)
            Pair(payload, true)
        } catch (e: Exception) {
            Pair(derived, false)
        }
    }

    /** Buka bungkus Keystore. Null bila gagal (payload bukan format ks). */
    fun unwrap(payload: ByteArray): ByteArray? {
        val key = getKey() ?: return null
        if (payload.size <= GCM_IV_BYTES) return null
        return try {
            val iv = payload.copyOfRange(0, GCM_IV_BYTES)
            val ct = payload.copyOfRange(GCM_IV_BYTES, payload.size)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_BITS, iv))
            cipher.doFinal(ct)
        } catch (e: Exception) {
            null
        }
    }

    fun isAvailable(): Boolean = getOrCreateKey() != null
}
