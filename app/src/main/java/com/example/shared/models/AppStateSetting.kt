package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_state_settings")
data class AppStateSetting(
    @PrimaryKey val key: String,
    val stringValue: String? = null,
    val longValue: Long? = null,
    val doubleValue: Double? = null,
    val boolValue: Boolean? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
