package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_logs")
data class AdminLog(
    @PrimaryKey val id: String,
    val actorName: String,
    val actorRole: String,
    val action: String,
    val targetName: String,
    val timestamp: String,
    val timestampMillis: Long = System.currentTimeMillis()
)
