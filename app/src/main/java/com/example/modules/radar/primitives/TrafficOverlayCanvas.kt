package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.remember
import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.models.TrafficIncident
import com.example.modules.radar.models.TrafficRoadSegment

@Composable
fun TrafficOverlayCanvas(
    roadSegments: List<TrafficRoadSegment>,
    incidents: List<TrafficIncident>,
    centerLat: Double,
    centerLng: Double,
    zoom: Int,
    isTrafficLayerEnabled: Boolean,
    screenWidth: Float,
    screenHeight: Float,
    isPowerSaver: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (!isTrafficLayerEnabled || screenWidth <= 0f || screenHeight <= 0f) return

    val flowPhase: Float = if (isPowerSaver) {
        0f
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "trafficFlow")
        val phase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 24f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "trafficPhase"
        )
        phase
    }

    // Precompute projected road segment paths to bypass heavy math calculations on every single draw frame
    val projectedRoads = remember(roadSegments, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        roadSegments.mapNotNull { road ->
            if (road.points.size < 2) return@mapNotNull null
            val path = Path()
            val first = road.points.first()
            val p0 = MapProjection.latLngToScreen(first.first, first.second, centerLat, centerLng, zoom, screenWidth, screenHeight)
            path.moveTo(p0.x, p0.y)

            for (i in 1 until road.points.size) {
                val pt = road.points[i]
                val pScreen = MapProjection.latLngToScreen(pt.first, pt.second, centerLat, centerLng, zoom, screenWidth, screenHeight)
                path.lineTo(pScreen.x, pScreen.y)
            }
            Pair(path, road.status.color)
        }
    }

    // Precompute incident screen coordinates to avoid redundant projections
    val projectedIncidents = remember(incidents, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        incidents.map { inc ->
            val pt = MapProjection.latLngToScreen(inc.lat, inc.lng, centerLat, centerLng, zoom, screenWidth, screenHeight)
            Pair(pt, inc.status.color)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), flowPhase)

        for ((path, color) in projectedRoads) {
            // Glow casing underlay
            drawPath(
                path = path,
                color = color.copy(alpha = 0.28f),
                style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Solid base traffic line
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Animated directional dash overlay
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.85f),
                style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = dashEffect)
            )
        }

        // Render precomputed traffic incident and congestion hotspots
        for ((pt, color) in projectedIncidents) {
            // Outer pulse circle
            drawCircle(
                color = color.copy(alpha = 0.35f),
                radius = 18f,
                center = pt
            )
            drawCircle(
                color = color,
                radius = 8f,
                center = pt
            )
            drawCircle(
                color = Color.White,
                radius = 3.5f,
                center = pt
            )
        }
    }
}
