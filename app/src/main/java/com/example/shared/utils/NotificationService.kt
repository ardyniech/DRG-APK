package com.example.shared.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationService {
    const val EMERGENCY_CHANNEL_ID = "drg_emergency_channel"
    const val COMMUNITY_CHANNEL_ID = "drg_community_channel"

    fun sendSystemNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        val channelId = EMERGENCY_CHANNEL_ID
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Darurat SOS DRG",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi darurat kecelakaan dan mogok Satgas DRG"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        // Use a generic intent if MainActivity isn't resolved or use standard launcher intent
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(context.packageName) ?: Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert) // Robust reliable fallback system drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
            
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
