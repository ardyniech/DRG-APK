package com.example.core.firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.shared.utils.NotificationService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DRGFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Store token locally for later sync with backend
        val sharedPrefs = getSharedPreferences("drg_fcm_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("fcm_token", token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val title = data["title"] ?: message.notification?.title ?: "DRG Notification"
        val body = data["body"] ?: message.notification?.body ?: ""
        val type = data["type"] ?: "general"
        val priority = data["priority"] ?: "normal"

        when (type) {
            "sos_emergency" -> handleSosEmergency(title, body, data)
            "hazard_alert" -> handleHazardAlert(title, body, data)
            "community_post" -> handleCommunityNotification(title, body)
            "task_assigned" -> handleTaskNotification(title, body)
            else -> sendGenericNotification(title, body, priority)
        }
    }

    private fun handleSosEmergency(title: String, body: String, data: Map<String, String>) {
        val channelId = NotificationService.EMERGENCY_CHANNEL_ID
        sendHighPriorityNotification(title, body, channelId)
    }

    private fun handleHazardAlert(title: String, body: String, data: Map<String, String>) {
        val channelId = NotificationService.EMERGENCY_CHANNEL_ID
        sendHighPriorityNotification(title, body, channelId)
    }

    private fun handleCommunityNotification(title: String, body: String) {
        sendGenericNotification(title, body, "normal")
    }

    private fun handleTaskNotification(title: String, body: String) {
        sendGenericNotification(title, body, "normal")
    }

    private fun sendHighPriorityNotification(title: String, body: String, channelId: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("open_tab", "EMERGENCY")
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun sendGenericNotification(title: String, body: String, priority: String) {
        val channelId = NotificationService.COMMUNITY_CHANNEL_ID
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val priorityLevel = if (priority == "high") NotificationCompat.PRIORITY_HIGH
                           else NotificationCompat.PRIORITY_DEFAULT

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(priorityLevel)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
