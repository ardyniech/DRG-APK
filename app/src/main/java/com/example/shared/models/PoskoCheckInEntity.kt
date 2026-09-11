package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posko_check_ins",
    indices = [
        Index(value = ["poskoId"]),
        Index(value = ["memberId"]),
        Index(value = ["checkInTimestamp"])
    ]
)
data class PoskoCheckInEntity(
    @PrimaryKey val id: String,
    val poskoId: String,
    val poskoName: String,
    val memberId: String,
    val memberName: String,
    val driverPlate: String,
    val checkInTimestamp: Long = System.currentTimeMillis(),
    val pointsAwarded: Int = 15,
    val checkInMethod: String = "GEOFENCE_AUTO"
)
