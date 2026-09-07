package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class NotificationSeverity {
    INFO,
    SUCCESS,
    WARNING,
    DANGER,
    EMERGENCY
}

@Entity(tableName = "notifications")
data class CommunityNotification(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val senderName: String,
    val senderRole: MemberRole,
    val severity: NotificationSeverity,
    val timeAgo: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
