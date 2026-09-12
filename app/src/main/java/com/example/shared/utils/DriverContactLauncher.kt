package com.example.shared.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.shared.models.DriverMember

enum class CoordinationReason(val title: String, val template: (DriverMember) -> String) {
    ROUTINE("Koordinasi Sesama Driver", { d -> "Halo Cak ${d.name} (${d.driverId}), koordinasi sesama driver DRG Malang Raya." }),
    TRAFFIC_HAZARD("Info Pantauan Jalur/Macet", { d -> "Halo Cak ${d.name}, ingin cek info pantauan lalu lintas / titik rawan di area sekitar Anda." }),
    ROAD_ASSISTANCE("Bantuan Kendaraan / Ban Bocor", { d -> "Halo Cak ${d.name}, mohon info bantuan kendala motor di jalan untuk driver DRG." }),
    SATGAS_ALERT("Panggilan Siaga Satgas", { d -> "🚨 *SIAGA SATGAS DRG* - Mohon koordinasi dan respon tim satgas DRG segera untuk rekan ${d.name}." })
}

object DriverContactLauncher {

    fun formatPhoneNumberForWhatsApp(rawPhone: String): String {
        var cleanPhone = rawPhone.replace("+", "").replace(" ", "").replace("-", "").trim()
        if (cleanPhone.startsWith("0")) {
            cleanPhone = "62" + cleanPhone.substring(1)
        }
        return cleanPhone
    }

    fun buildWhatsAppUri(formattedPhone: String, message: String = ""): Uri {
        val textParam = if (message.isNotBlank()) "?text=${Uri.encode(message)}" else ""
        return Uri.parse("https://wa.me/$formattedPhone$textParam")
    }

    fun openDriverWhatsApp(
        context: Context,
        driver: DriverMember,
        reason: CoordinationReason = CoordinationReason.ROUTINE
    ): Boolean {
        val phone = driver.phone.ifBlank { driver.emergencyPhone }
        if (phone.isBlank()) {
            Toast.makeText(context, "Nomor telepon driver ${driver.name} belum terdaftar", Toast.LENGTH_SHORT).show()
            return false
        }
        val formattedPhone = formatPhoneNumberForWhatsApp(phone)
        val message = reason.template(driver)
        return launchWhatsAppUri(context, buildWhatsAppUri(formattedPhone, message))
    }

    fun openDriverDialer(context: Context, driver: DriverMember): Boolean {
        val phone = driver.phone.ifBlank { driver.emergencyPhone }
        if (phone.isBlank()) {
            Toast.makeText(context, "Nomor telepon driver belum terdaftar", Toast.LENGTH_SHORT).show()
            return false
        }
        return try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.trim()}")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            Toast.makeText(context, "Tidak dapat membuka dialer telepon", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun openEmergencyWhatsApp(
        context: Context,
        driver: DriverMember,
        sosReason: String = "Panggilan Darurat SOS"
    ): Boolean {
        val phone = driver.emergencyPhone.ifBlank { driver.phone }
        if (phone.isBlank()) {
            Toast.makeText(context, "Nomor kontak darurat belum terdaftar", Toast.LENGTH_SHORT).show()
            return false
        }
        val formattedPhone = formatPhoneNumberForWhatsApp(phone)
        val message = "🚨 *DARURAT DRG MALANG RAYA*\nDriver: *${driver.name}* (${driver.motorcyclePlate})\nInfo: $sosReason\nHarap segera respon koordinasi tim lapangan."
        return launchWhatsAppUri(context, buildWhatsAppUri(formattedPhone, message))
    }

    private fun launchWhatsAppUri(context: Context, uri: Uri): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            Toast.makeText(context, "Aplikasi WhatsApp tidak terinstal atau tautan tidak valid", Toast.LENGTH_SHORT).show()
            false
        }
    }
}
