package com.example.core.sync

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.shared.models.EmergencyType

object EmergencySmsFallbackHelper {
    const val DEFAULT_HOTLINE_TRC = "081234567890"

    fun formatEmergencySms(
        driverName: String,
        plateNumber: String,
        type: EmergencyType,
        locationNote: String,
        lat: Double,
        lng: Double
    ): String {
        val mapsLink = if (lat != 0.0 && lng != 0.0) "https://maps.google.com/?q=$lat,$lng" else "GPS: Nonaktif"
        return "[SOS DRG] Rekan $driverName ($plateNumber) BUTUH BANTUAN: ${type.label}. Info: $locationNote. Peta: $mapsLink"
    }

    fun createSmsIntent(phoneNumber: String, message: String): Intent {
        val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "").ifBlank { DEFAULT_HOTLINE_TRC }
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$cleanNumber")
            putExtra("sms_body", message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun dispatchSmsFallback(context: Context, phoneNumber: String, message: String): Boolean {
        return try {
            val intent = createSmsIntent(phoneNumber, message)
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
