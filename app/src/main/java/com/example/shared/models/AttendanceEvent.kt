package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_events")
data class AttendanceEvent(
    @PrimaryKey val id: String,
    val title: String,
    val dateText: String,
    val timeText: String,
    val location: String,
    val pointsReward: Int = 50,
    val checkInCode: String = "DRG2026",
    val description: String,
    val attendedCount: Int = 24,
    val isCheckedInByMe: Boolean = false
)
