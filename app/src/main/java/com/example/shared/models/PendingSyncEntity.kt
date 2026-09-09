package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_sync_queue")
data class PendingSyncEntity(
    @PrimaryKey
    val id: String,
    val entityType: String,
    val payloadJson: String,
    val timestamp: Long
)
