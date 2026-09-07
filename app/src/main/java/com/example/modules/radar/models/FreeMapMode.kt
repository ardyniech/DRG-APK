package com.example.modules.radar.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.ui.graphics.vector.ImageVector

enum class FreeMapMode(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val tileUrlTemplate: String,
    val attribution: String,
    val maxZoom: Int = 18,
    val minZoom: Int = 11
) {
    ROAD(
        title = "Jalan Raya",
        subtitle = "OpenStreetMap Standard",
        icon = Icons.Default.Map,
        tileUrlTemplate = "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
        attribution = "© OpenStreetMap contributors"
    ),
    SATELLITE(
        title = "Satelit",
        subtitle = "ESRI World Imagery Earth",
        icon = Icons.Default.Satellite,
        tileUrlTemplate = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
        attribution = "© Esri, Maxar, Earthstar Geographics"
    );

    fun buildTileUrl(z: Int, x: Int, y: Int): String {
        return tileUrlTemplate
            .replace("{z}", z.toString())
            .replace("{x}", x.toString())
            .replace("{y}", y.toString())
    }
}
