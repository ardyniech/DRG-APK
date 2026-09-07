package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_preferences")
data class NotificationPreference(
    @PrimaryKey val memberId: String,
    val sosAlerts: Boolean = true,
    val kopdarReminders: Boolean = true,
    val kasUpdates: Boolean = true,
    val forumActivity: Boolean = true,
    val peerPointsNotif: Boolean = true,
    val taskAssignments: Boolean = true
)
