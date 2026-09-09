package com.example.shared.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppLauncher {

    fun openChat(context: Context, phoneNumber: String, message: String) {
        try {
            var cleanPhone = phoneNumber.replace("+", "").replace(" ", "").replace("-", "")
            if (cleanPhone.startsWith("0")) {
                cleanPhone = "62" + cleanPhone.substring(1)
            }
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp tidak terinstal atau nomor tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareLocation(context: Context, driverName: String, motorcycle: String, lat: Double, lng: Double) {
        try {
            val mapsUrl = "https://maps.google.com/?q=$lat,$lng"
            val textMessage = "📍 *Bagikan Lokasi Live DRG* \nDriver: *$driverName*\nMotor: *$motorcycle*\nPosisi Terkini: $mapsUrl\n\n_Dipantau melalui Sistem Radar Siaga DRG Malang Raya._"
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, textMessage)
                `package` = "com.whatsapp"
            }
            val chooser = Intent.createChooser(intent, "Bagikan Lokasi")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                val mapsUrl = "https://maps.google.com/?q=$lat,$lng"
                putExtra(Intent.EXTRA_TEXT, "📍 Lokasi Live DRG - Driver: $driverName\nPosisi: $mapsUrl")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(fallbackIntent, "Bagikan Lokasi"))
        }
    }
}
