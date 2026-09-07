package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EmergencyType(val label: String, val severity: String) {
    MOGOK("Motor Mogok / Mesin Rusak", "Sedang"),
    KECELAKAAN("Insiden / Kecelakaan", "Tinggi"),
    DAERAH_RAWAN("Pantauan Jalur Rawan / Escort", "Siaga"),
    BEGAL("Ancaman Bahaya / Begal", "Kritis")
}

@Entity(tableName = "emergency_alerts")
data class EmergencyAlert(
    @PrimaryKey val id: String,
    val driverId: String,
    val driverName: String,
    val driverPhone: String,
    val plateNumber: String,
    val type: EmergencyType,
    val message: String,
    val lat: Double,
    val lng: Double,
    val locationName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val responderCount: Int = 0,
    val resolvedBy: String? = null
)
