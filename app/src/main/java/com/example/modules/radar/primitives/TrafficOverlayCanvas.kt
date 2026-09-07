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
    isPowerSaver: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (!isTrafficLayerEnabled) return

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

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0 || h <= 0) return@Canvas

        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), flowPhase)

        for (road in roadSegments) {
            if (road.points.size < 2) continue
            val path = Path()
            val first = road.points.first()
            val p0 = MapProjection.latLngToScreen(first.first, first.second, centerLat, centerLng, zoom, w, h)
            path.moveTo(p0.x, p0.y)

            for (i in 1 until road.points.size) {
                val pt = road.points[i]
                val pScreen = MapProjection.latLngToScreen(pt.first, pt.second, centerLat, centerLng, zoom, w, h)
                path.lineTo(pScreen.x, pScreen.y)
            }

            // Glow casing underlay
            drawPath(
                path = path,
                color = road.status.color.copy(alpha = 0.28f),
                style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Solid base traffic line
            drawPath(
                path = path,
                color = road.status.color,
                style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Animated directional dash overlay
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.85f),
                style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = dashEffect)
            )
        }

        // Render traffic incident and congestion hotspots
        for (inc in incidents) {
            val pt = MapProjection.latLngToScreen(inc.lat, inc.lng, centerLat, centerLng, zoom, w, h)
            // Outer pulse circle
            drawCircle(
                color = inc.status.color.copy(alpha = 0.35f),
                radius = 18f,
                center = pt
            )
            drawCircle(
                color = inc.status.color,
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
