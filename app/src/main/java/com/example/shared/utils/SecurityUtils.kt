package com.example.shared.utils

import android.content.Context
import java.security.MessageDigest

object SecurityUtils {

    fun hashPin(pin: String): String {
        val bytes = pin.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun getStoredPinHash(context: Context, memberId: String): String {
        val defaultHash = hashPin("1234")
        val mainPrefs = context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
        val mainSaved = mainPrefs.getString("pin_hash_$memberId", null)
        if (mainSaved != null) return mainSaved

        val securityPrefs = context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE)
        return securityPrefs.getString("pin_hash_$memberId", defaultHash) ?: defaultHash
    }

    fun storePinHash(context: Context, memberId: String, pin: String) {
        val prefs = context.getSharedPreferences("drg_security_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("pin_hash_$memberId", hashPin(pin)).apply()
    }
}
