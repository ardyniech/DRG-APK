package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class BadgeItem(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // KOPDAR, HELP_PATROL, FORUM, TASK, KAS
    val description: String,
    val iconEmoji: String,
    val requiredXp: Int,
    val isEarned: Boolean = false,
    val earnedDate: String? = null
)
