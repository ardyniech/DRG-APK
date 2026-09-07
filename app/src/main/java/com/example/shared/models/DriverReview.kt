package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_reviews")
data class DriverReview(
    @PrimaryKey val id: String,
    val targetDriverId: String,
    val reviewerName: String,
    val reviewerRole: MemberRole,
    val rating: Float,
    val tag: String,
    val comment: String,
    val dateText: String,
    val timestamp: Long = System.currentTimeMillis()
)
