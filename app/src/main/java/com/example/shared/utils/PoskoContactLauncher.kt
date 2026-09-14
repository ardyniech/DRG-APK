package com.example.shared.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.shared.models.PoskoLocation

enum class PoskoCoordinationReason(val title: String, val template: (PoskoLocation) -> String) {
    CHECKIN_REST("Koordinasi Check-in / Istirahat", { p -> "Halo Cak ${p.coordinatorName} (Pengurus ${p.name}), saya sedang merapat ke posko untuk istirahat sejenak." }),
    FACILITY_INQUIRY("Tanya Fasilitas & Ketersediaan", { p -> "Halo Pengurus ${p.name}, izin tanya ketersediaan tempat istirahat / fasilitas di posko saat ini." }),
    EMERGENCY_SUPPORT("Bantuan Posko / Logistik Siaga", { p -> "🚨 *LOGISTIK POSKO DRG* - Halo Cak ${p.coordinatorName}, koordinasi kebutuhan darurat / logistik untuk ${p.name}." })
}

object PoskoContactLauncher {

    fun openPoskoWhatsApp(
        context: Context,
        posko: PoskoLocation,
        reason: PoskoCoordinationReason = PoskoCoordinationReason.CHECKIN_REST
    ): Boolean {
        if (posko.phone.isBlank()) {
            Toast.makeText(context, "Nomor telepon posko ${posko.name} belum terdaftar", Toast.LENGTH_SHORT).show()
            return false
        }
        val formattedPhone = DriverContactLauncher.formatPhoneNumberForWhatsApp(posko.phone)
        val message = reason.template(posko)
        val uri = DriverContactLauncher.buildWhatsAppUri(formattedPhone, message)
        return try {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            Toast.makeText(context, "Aplikasi WhatsApp tidak terinstal atau tautan tidak valid", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun openPoskoDialer(context: Context, posko: PoskoLocation): Boolean {
        if (posko.phone.isBlank()) {
            Toast.makeText(context, "Nomor telepon posko belum terdaftar", Toast.LENGTH_SHORT).show()
            return false
        }
        return try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${posko.phone.trim()}")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            Toast.makeText(context, "Tidak dapat membuka dialer telepon", Toast.LENGTH_SHORT).show()
            false
        }
    }
}
