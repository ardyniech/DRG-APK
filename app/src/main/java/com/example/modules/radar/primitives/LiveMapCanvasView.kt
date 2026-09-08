package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
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
                        val pt = MapProjection.latLngToScreen(driver.currentLat, driver.currentLng, centerLat, centerLng, zoom, w, h)
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

        drawLiveMapGrid()

        val myPt = MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, w, h)
        drawCircle(color = DrgGreenPrimary.copy(alpha = 0.25f), radius = 24f * pulseScale, center = myPt)
        drawCircle(color = DrgGreenPrimary, radius = 9f, center = myPt)
        drawCircle(color = Color.White, radius = 4f, center = myPt)

        membersWithDistance.forEach { (driver, _) ->
            val pt = MapProjection.latLngToScreen(driver.currentLat, driver.currentLng, centerLat, centerLng, zoom, w, h)
            drawDriverMarker(pt = pt, driver = driver, isFocused = driver.id == focusedDriverId, pulseScale = pulseScale)
        }
    }
}
