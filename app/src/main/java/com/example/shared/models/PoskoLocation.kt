package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posko_locations")
data class PoskoLocation(
    @PrimaryKey val id: String,
    val name: String,
    val area: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val coordinatorName: String,
    val phone: String,
    val facilities: String,
    val activeDriversCount: Int = 5,
    val isMainPosko: Boolean = false
)
