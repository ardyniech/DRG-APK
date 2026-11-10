package com.example.shared.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.core.security.PinHasher

object SecurityUtils {

    fun hashPin(context: Context, pin: String): String = PinHasher.hashPin(context, pin)

    /** Legacy overload tanpa context (fallback software, akan di-migrasi saat login). */
    fun hashPin(pin: String): String {
        val ctx = runCatching { com.example.DRGApplication.instance.applicationContext }.getOrNull()
        return if (ctx != null) PinHasher.hashPin(ctx, pin) else PinHasher.hashPinSoftware(pin)
    }

    fun getStoredPinHash(context: Context, key: String): String? {
        val secPrefs = context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE)
        val secHash = secPrefs.getString("pin_hash_$key", null)
        if (secHash != null) return secHash

        val drgPrefs = context.getSharedPreferences("drg_prefs", Context.MODE_PRIVATE)
        val drgHash = drgPrefs.getString("pin_hash_$key", null)
        if (drgHash != null) return drgHash

        val defaultPrefs = context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
        return defaultPrefs.getString("pin_hash_$key", null)
    }

    fun storePinHash(context: Context, key: String, pin: String) {
        val hashed = hashPin(context, pin)
        context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE).edit()
            .putString("pin_hash_$key", hashed).apply()
        context.getSharedPreferences("drg_prefs", Context.MODE_PRIVATE).edit()
            .putString("pin_hash_$key", hashed).apply()
    }

    fun storePinHash(prefs: SharedPreferences, key: String, pin: String) {
        val hashed = hashPin(pin)
        prefs.edit().putString("pin_hash_$key", hashed).apply()
    }

    fun verifyPin(context: Context, pin: String, stored: String?): Boolean =
        PinHasher.verifyPin(context, pin, stored)

    fun needsMigration(stored: String?): Boolean = PinHasher.needsMigration(stored)

    fun generateAndStoreSession(prefs: SharedPreferences, memberId: String): String {
        val token = java.util.UUID.randomUUID().toString() + "_" + System.currentTimeMillis()
        prefs.edit()
            .putString("logged_in_member_id", memberId)
            .putString("device_session_token", token)
            .putString("device_session_member_id", memberId)
            .putLong("device_session_timestamp", System.currentTimeMillis())
            .apply()
        return token
    }

    fun validateSession(prefs: SharedPreferences, memberId: String): Boolean {
        val storedToken = prefs.getString("device_session_token", null) ?: return false
        val storedMemberId = prefs.getString("device_session_member_id", null) ?: return false
        return storedToken.isNotEmpty() && storedMemberId == memberId
    }

    fun clearSession(prefs: SharedPreferences) {
        prefs.edit()
            .remove("logged_in_member_id")
            .remove("device_session_token")
            .remove("device_session_member_id")
            .remove("device_session_timestamp")
            .apply()
    }

    fun isSeedSuperAdmin(memberId: String?): Boolean {
        if (memberId == null) return false
        return memberId == com.example.BuildConfig.SEED_SUPER_ADMIN_ID
    }
}
