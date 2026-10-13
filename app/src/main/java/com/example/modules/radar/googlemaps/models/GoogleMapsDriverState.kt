package com.example.modules.radar.googlemaps.models

import com.example.shared.models.DriverMember
import com.google.maps.android.compose.MapType

enum class DriverMapFilter(val label: String) {
    ALL("Semua"),
    ACTIVE_DUTY("Sedang Narik"),
    SATGAS("Satgas"),
    STANDBY("Siaga / Tersedia")
}

data class GoogleMapsDriverUiState(
    val selectedFilter: DriverMapFilter = DriverMapFilter.ALL,
    val selectedDriver: DriverMember? = null,
    val mapType: MapType = MapType.NORMAL,
    val isTrafficEnabled: Boolean = true,
    val isSatellite: Boolean = false,
    val centerLat: Double = -7.9822,
    val centerLng: Double = 112.6303,
    val zoomLevel: Float = 14.5f
)
