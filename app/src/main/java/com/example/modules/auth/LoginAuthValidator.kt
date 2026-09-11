package com.example.modules.auth

import android.content.Context
import com.example.shared.models.DriverMember
import com.example.shared.utils.SecurityUtils

object LoginAuthValidator {
    fun authenticate(
        context: Context,
        members: List<DriverMember>,
        inputId: String,
        inputPin: String
    ): Pair<Boolean, String?> {
        val clean = inputId.trim()
        val target = members.find {
            it.driverId.equals(clean, ignoreCase = true) ||
            it.id.equals(clean, ignoreCase = true) ||
            it.phone.replace("+62", "0").trim() == clean.replace("+62", "0").trim() ||
            it.motorcyclePlate.replace(" ", "").equals(clean.replace(" ", ""), ignoreCase = true)
        } ?: return Pair(false, "ID, Plat, atau No. HP tidak terdaftar di database DRG Malang.")

        val hashedInput = SecurityUtils.hashPin(inputPin)
        val storedHash = SecurityUtils.getStoredPinHash(context, target.id)
            ?: SecurityUtils.getStoredPinHash(context, target.phone)
            ?: SecurityUtils.getStoredPinHash(context, target.driverId)

        return if (storedHash == null) {
            SecurityUtils.storePinHash(context, target.id, inputPin)
            SecurityUtils.storePinHash(context, target.driverId, inputPin)
            Pair(true, target.id)
        } else if (hashedInput == storedHash) {
            Pair(true, target.id)
        } else {
            Pair(false, "PIN keamanan salah. Silakan coba lagi.")
        }
    }
}
