package com.example.shared.utils

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

object SecurityUtils {

    fun hashPin(pin: String): String {
        val bytes = pin.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
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
        val hashed = hashPin(pin)
        context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE).edit()
            .putString("pin_hash_$key", hashed).apply()
        context.getSharedPreferences("drg_prefs", Context.MODE_PRIVATE).edit()
            .putString("pin_hash_$key", hashed).apply()
    }

    fun storePinHash(prefs: SharedPreferences, key: String, pin: String) {
        prefs.edit().putString("pin_hash_$key", hashPin(pin)).apply()
    }
}
