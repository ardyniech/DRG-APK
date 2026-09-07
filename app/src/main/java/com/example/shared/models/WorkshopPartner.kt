package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workshop_partners")
data class WorkshopPartner(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val area: String,
    val distanceKm: Double,
    val rating: Float,
    val reviewCount: Int,
    val discountPercent: Int,
    val services: String,
    val phone: String,
    val openHours: String = "08:00 - 20:00",
    val isVerifiedPartner: Boolean = true
)
