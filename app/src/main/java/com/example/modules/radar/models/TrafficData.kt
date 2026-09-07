package com.example.modules.radar.models

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgTrafficGreen
import com.example.ui.theme.DrgTrafficOrange
import com.example.ui.theme.DrgTrafficRed

enum class TrafficStatus(
    val label: String,
    val speedRangeDesc: String,
    val color: Color
) {
    SMOOTH("Lancar", "> 35 km/j", DrgTrafficGreen),
    CONGESTED("Padat Merayap", "15 - 35 km/j", DrgTrafficOrange),
    JAMMED("Macet Parah", "< 15 km/j", DrgTrafficRed)
}

data class TrafficRoadSegment(
    val id: String,
    val roadName: String,
    val points: List<Pair<Double, Double>>, // Lat, Lng
    val status: TrafficStatus,
    val avgSpeedKmh: Int,
    val description: String = ""
)

data class TrafficIncident(
    val id: String,
    val locationName: String,
    val lat: Double,
    val lng: Double,
    val speedKmh: Int,
    val status: TrafficStatus,
    val notes: String
)
