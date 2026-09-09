package com.example.modules.radar.logic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.shared.models.HazardArea
import com.example.shared.models.HazardType
import com.example.ui.theme.DrgTrafficGreen
import com.example.ui.theme.DrgTrafficOrange
import com.example.ui.theme.DrgTrafficRed

data class TrafficCorridor(
    val id: String,
    val name: String,
    val startLat: Double,
    val startLng: Double,
    val endLat: Double,
    val endLng: Double,
    val baseCongestion: CongestionLevel
)

enum class CongestionLevel(val label: String, val color: Color, val averageSpeedKmH: Int) {
    CLEAR("Lancar", DrgTrafficGreen, 42),
    MODERATE("Padat", DrgTrafficOrange, 22),
    HEAVY("Macet Total", DrgTrafficRed, 8)
}

object TrafficCorridorPainter {

    val mainCorridors = listOf(
        TrafficCorridor("C1", "Jl. Basuki Rahmat (Kayutangan)", -7.9785, 112.6300, -7.9850, 112.6280, CongestionLevel.HEAVY),
        TrafficCorridor("C2", "Jl. Besar Ijen", -7.9720, 112.6220, -7.9820, 112.6240, CongestionLevel.CLEAR),
        TrafficCorridor("C3", "Jl. Soekarno-Hatta (Suhat)", -7.9450, 112.6160, -7.9580, 112.6180, CongestionLevel.MODERATE),
        TrafficCorridor("C4", "Jl. Pasar Besar / Pecinan", -7.9830, 112.6310, -7.9900, 112.6330, CongestionLevel.HEAVY),
        TrafficCorridor("C5", "Jl. MT Haryono (Dinoyo)", -7.9420, 112.6080, -7.9490, 112.6150, CongestionLevel.MODERATE)
    )

    fun DrawScope.drawTrafficCorridors(
        hazards: List<HazardArea>,
        centerLat: Double,
        centerLng: Double,
        zoom: Int,
        screenWidth: Float,
        screenHeight: Float,
        pulseProgress: Float
    ) {
        val hasReportedJam = hazards.any { it.hazardType == HazardType.TRAFFIC_JAM }

        mainCorridors.forEach { corridor ->
            val startPt = MapProjection.latLngToScreen(corridor.startLat, corridor.startLng, centerLat, centerLng, zoom, screenWidth, screenHeight)
            val endPt = MapProjection.latLngToScreen(corridor.endLat, corridor.endLng, centerLat, centerLng, zoom, screenWidth, screenHeight)

            val level = if (hasReportedJam && corridor.id in listOf("C1", "C4")) CongestionLevel.HEAVY else corridor.baseCongestion
            val strokeColor = level.color

            // Glow underlay
            drawLine(
                color = strokeColor.copy(alpha = 0.25f),
                start = startPt,
                end = endPt,
                strokeWidth = 14f
            )

            // Main traffic road line
            drawLine(
                color = strokeColor,
                start = startPt,
                end = endPt,
                strokeWidth = 5.5f
            )

            // Animated directional traffic dashes
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = startPt,
                end = endPt,
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 16f), pulseProgress * 26f)
            )

            // Corridor midpoint badge
            val midPt = Offset((startPt.x + endPt.x) / 2f, (startPt.y + endPt.y) / 2f)
            drawCircle(color = strokeColor, radius = 7f, center = midPt)
            drawCircle(color = Color.White, radius = 3f, center = midPt)
        }
    }
}
