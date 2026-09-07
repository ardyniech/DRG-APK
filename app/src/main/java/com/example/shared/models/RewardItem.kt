package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class RewardItem(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val requiredPoints: Int,
    val category: String, // BENZIN, KAS, MERCH, BENGKEL
    val iconEmoji: String,
    val stockAvailable: Int = 10,
    val isRedeemed: Boolean = false
)
