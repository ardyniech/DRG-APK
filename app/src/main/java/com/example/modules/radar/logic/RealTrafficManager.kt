package com.example.modules.radar.logic

import com.example.shared.models.HazardArea
import com.example.shared.models.HazardType

data class RealTrafficSummary(
    val totalJamReports: Int,
    val activeAreas: List<String>,
    val googleMapsTrafficUrl: String
)

object RealTrafficManager {

    const val DEFAULT_MALANG_LAT = -7.9822
    const val DEFAULT_MALANG_LNG = 112.6303
    const val DEFAULT_TRAFFIC_ZOOM = 14

    fun getGoogleMapsTrafficUrl(
        lat: Double = DEFAULT_MALANG_LAT,
        lng: Double = DEFAULT_MALANG_LNG,
        zoom: Int = DEFAULT_TRAFFIC_ZOOM
    ): String {
        return "https://www.google.com/maps/@$lat,$lng,${zoom}z/data=!5m1!1e1"
    }

    fun getActiveTrafficJams(hazards: List<HazardArea>): List<HazardArea> {
        return hazards.filter { it.hazardType == HazardType.TRAFFIC_JAM }
    }

    fun generateTrafficSummary(hazards: List<HazardArea>): RealTrafficSummary {
        val jamHazards = getActiveTrafficJams(hazards)
        val areas = jamHazards.map { it.description }.take(3)
        return RealTrafficSummary(
            totalJamReports = jamHazards.size,
            activeAreas = areas,
            googleMapsTrafficUrl = getGoogleMapsTrafficUrl()
        )
    }
}
