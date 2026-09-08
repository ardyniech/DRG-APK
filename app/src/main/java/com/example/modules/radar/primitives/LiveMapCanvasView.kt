package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.modules.radar.logic.MapProjection
import com.example.shared.models.DriverMember
import com.example.ui.theme.*
import kotlin.math.sqrt

@Composable
fun LiveMapCanvasView(
    membersWithDistance: List<Pair<DriverMember, Double>>,
    centerLat: Double,
    centerLng: Double,
    zoom: Int,
    focusedDriverId: String?,
    onSelectDriver: (DriverMember) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liveDriverPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(membersWithDistance, centerLat, centerLng, zoom) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    var closestDriver: DriverMember? = null
                    var minDistance = Float.MAX_VALUE

                    membersWithDistance.forEach { (driver, _) ->
                        val pt = MapProjection.latLngToScreen(
                            driver.currentLat, driver.currentLng,
                            centerLat, centerLng, zoom, w, h
                        )
                        val dx = tapOffset.x - pt.x
                        val dy = tapOffset.y - pt.y
                        val dist = sqrt(dx * dx + dy * dy)
                        if (dist < 48f && dist < minDistance) {
                            minDistance = dist
                            closestDriver = driver
                        }
                    }
                    closestDriver?.let { onSelectDriver(it) }
                }
            }
    ) {
        val w = size.width
        val h = size.height
        if (w <= 0 || h <= 0) return@Canvas

        // Draw Coordinate Grid Overlay
        val gridStep = 80f
        var x = 0f
        while (x < w) {
            drawLine(
                color = DrgOutline.copy(alpha = 0.2f),
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
            x += gridStep
        }
        var y = 0f
        while (y < h) {
            drawLine(
                color = DrgOutline.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
            y += gridStep
        }

        // Draw My Center Position Marker
        val myPt = MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, w, h)
        drawCircle(color = DrgGreenPrimary.copy(alpha = 0.25f), radius = 24f * pulseScale, center = myPt)
        drawCircle(color = DrgGreenPrimary, radius = 9f, center = myPt)
        drawCircle(color = Color.White, radius = 4f, center = myPt)

        // Draw Nearby DRG Drivers
        membersWithDistance.forEach { (driver, _) ->
            val pt = MapProjection.latLngToScreen(
                driver.currentLat, driver.currentLng,
                centerLat, centerLng, zoom, w, h
            )
            val isFocused = driver.id == focusedDriverId
            val markerColor = when {
                driver.role.name == "SATGAS" -> DrgRedDanger
                driver.currentStatus == "Sedang Narik" -> Color(0xFFFF8F00)
                else -> DrgGreenPrimary
            }

            if (isFocused) {
                drawCircle(
                    color = markerColor.copy(alpha = 0.35f),
                    radius = 28f * pulseScale,
                    center = pt,
                    style = Stroke(width = 3f)
                )
            }

            drawCircle(color = markerColor.copy(alpha = 0.3f), radius = 18f, center = pt)
            drawCircle(color = markerColor, radius = 10f, center = pt)
            drawCircle(color = Color.White, radius = 4f, center = pt)
        }
    }
}
