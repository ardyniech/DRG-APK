package com.example.shared.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppLauncher {

    fun openChat(context: Context, phoneNumber: String, message: String = "") {
        try {
            var cleanPhone = phoneNumber.replace("+", "").replace(" ", "").replace("-", "")
            if (cleanPhone.startsWith("0")) {
                cleanPhone = "62" + cleanPhone.substring(1)
            }
            val textParam = if (message.isNotEmpty()) "&text=${Uri.encode(message)}" else ""
            val uri = Uri.parse("https://wa.me/$cleanPhone$textParam")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp tidak terinstal atau nomor tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGoogleMapsAddress(context: Context, address: String) {
        if (address.isBlank()) {
            Toast.makeText(context, "Alamat belum diatur", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val uri = Uri.parse("geo:0,0?q=" + Uri.encode(address.trim()))
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address.trim()))
            ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
            context.startActivity(webIntent)
        }
    }

    fun openLiveTrafficMap(context: Context, lat: Double = -7.9822, lng: Double = 112.6303, zoom: Int = 14) {
        val trafficUrl = "https://www.google.com/maps/@$lat,$lng,${zoom}z/data=!5m1!1e1"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trafficUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setPackage("com.google.android.apps.maps")
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trafficUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        }
    }

    fun openDialer(context: Context, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.trim()}"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "Tidak dapat membuka dialer telepon", Toast.LENGTH_SHORT).show()
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
