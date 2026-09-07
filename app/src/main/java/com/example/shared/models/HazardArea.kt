package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class HazardType(val label: String, val emoji: String, val colorHex: Long) {
    BEGAL_RISK("Jalur Rawan Begal", "🚨", 0xFFDC2626),
    FLOOD("Genangan Banjir", "🌊", 0xFF0284C7),
    HEAVY_HOLE("Jalan Berlubang Parah", "⚠️", 0xFFD97706),
    TRAFFIC_JAM("Macet Total / Penyekatan", "🚦", 0xFF475569)
}

@Entity(tableName = "hazard_areas")
data class HazardArea(
    @PrimaryKey val id: String,
    val title: String,
    val hazardType: HazardType,
    val locationName: String,
    val description: String,
    val lat: Double,
    val lng: Double,
    val reportedBy: String,
    val reporterRole: MemberRole,
    val confirmCount: Int = 1,
    val timeAgo: String = "Baru saja"
)
