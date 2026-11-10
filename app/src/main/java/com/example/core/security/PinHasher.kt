package com.example.core.security

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hashing PIN aman: PBKDF2WithHmacSHA256 + salt acak, dibungkus Android Keystore.
 * Format: v2:<salt_b64>:<iter>:<payload_b64>:<ks|sw> ; v1:<sha256> ; <sha256>.
 */
object PinHasher {

    private const val VERSION_V2 = "v2"
    private const val VERSION_V1 = "v1"
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_BYTES = 16

    fun hashPinSoftware(pin: String): String {
        require(pin.isNotEmpty()) { "PIN tidak boleh kosong" }
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val derived = pbkdf2(pin, salt, ITERATIONS, KEY_LENGTH_BITS)
        val saltB64 = Base64.encodeToString(salt, Base64.NO_WRAP)
        val payloadB64 = Base64.encodeToString(derived, Base64.NO_WRAP)
        return "$VERSION_V2:$saltB64:$ITERATIONS:$payloadB64:sw"
    }

    fun hashPin(context: Context, pin: String): String {
        require(pin.isNotEmpty()) { "PIN tidak boleh kosong" }
        val salt = ByteArray(SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val derived = pbkdf2(pin, salt, ITERATIONS, KEY_LENGTH_BITS)
        val (payload, used) = PinKeystore.wrap(derived)
        val saltB64 = Base64.encodeToString(salt, Base64.NO_WRAP)
        val payloadB64 = Base64.encodeToString(payload, Base64.NO_WRAP)
        val mode = if (used) "ks" else "sw"
        return "$VERSION_V2:$saltB64:$ITERATIONS:$payloadB64:$mode"
    }

    fun verifyPin(context: Context, pin: String, stored: String?): Boolean {
        if (pin.isEmpty() || stored.isNullOrBlank()) return false
        return try {
            when {
                stored.startsWith("$VERSION_V2:") -> verifyV2(context, pin, stored)
                stored.startsWith("$VERSION_V1:") -> {
                    val legacy = stored.removePrefix("$VERSION_V1:")
                    timingSafeEquals(legacy, sha256Hex(pin))
                }
                stored.matches(Regex("[0-9a-fA-F]{64}")) -> timingSafeEquals(stored, sha256Hex(pin))
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun needsMigration(stored: String?): Boolean {
        if (stored.isNullOrBlank()) return false
        if (!stored.startsWith("$VERSION_V2:")) return true
        val parts = stored.split(":")
        return parts.size < 5 || parts[4] != "ks"
    }

    fun isKeystoreBacked(stored: String?): Boolean {
        if (stored.isNullOrBlank()) return false
        val parts = stored.split(":")
        return parts.size >= 5 && parts[0] == VERSION_V2 && parts[4] == "ks"
    }

    fun isKeystoreAvailable(): Boolean = PinKeystore.isAvailable()

    private fun verifyV2(context: Context, pin: String, stored: String): Boolean {
        val parts = stored.split(":")
        if (parts.size != 5) return false
        val salt = Base64.decode(parts[1], Base64.NO_WRAP)
        val iterations = parts[2].toInt()
        val payload = Base64.decode(parts[3], Base64.NO_WRAP)
        val mode = parts[4]
        val derived = pbkdf2(pin, salt, iterations, KEY_LENGTH_BITS)
        val storedDerived = if (mode == "ks") PinKeystore.unwrap(payload) else payload
        return timingSafeEqualsBytes(derived, storedDerived)
    }

    private fun pbkdf2(pin: String, salt: ByteArray, iterations: Int, keyBits: Int): ByteArray {
        val spec = PBEKeySpec(pin.toCharArray(), salt, iterations, keyBits)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    private fun sha256Hex(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    private fun timingSafeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        var r = 0
        for (i in a.indices) r = r or (a[i].code xor b[i].code)
        return r == 0
    }

    private fun timingSafeEqualsBytes(a: ByteArray, b: ByteArray?): Boolean {
        if (b == null || a.size != b.size) return false
        var r = 0
        for (i in a.indices) r = r or (a[i].toInt() xor b[i].toInt())
        return r == 0
    }
}
