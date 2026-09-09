package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
    screenWidth: Float,
    screenHeight: Float,
    focusedDriverId: String?,
    onSelectDriver: (DriverMember) -> Unit,
    modifier: Modifier = Modifier
) {
    if (screenWidth <= 0f || screenHeight <= 0f) return

    val infiniteTransition = rememberInfiniteTransition(label = "liveDriverPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "pulseScale"
    )

    // Precompute nearby driver locations
    val projectedDrivers = remember(membersWithDistance, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        membersWithDistance.map { (driver, dist) ->
            Triple(
                MapProjection.latLngToScreen(driver.currentLat, driver.currentLng, centerLat, centerLng, zoom, screenWidth, screenHeight),
                driver,
                dist
            )
        }
    }

    // Precompute my location
    val myPt = remember(centerLat, centerLng, zoom, screenWidth, screenHeight) {
        MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, screenWidth, screenHeight)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(projectedDrivers) {
                detectTapGestures { tapOffset ->
                    var closestDriver: DriverMember? = null
                    var minDistance = Float.MAX_VALUE

                    projectedDrivers.forEach { (pt, driver, _) ->
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
        drawLiveMapGrid()

        drawCircle(color = DrgGreenPrimary.copy(alpha = 0.25f), radius = 24f * pulseScale, center = myPt)
        drawCircle(color = DrgGreenPrimary, radius = 9f, center = myPt)
        drawCircle(color = Color.White, radius = 4f, center = myPt)

        projectedDrivers.forEach { (pt, driver, _) ->
            drawDriverMarker(pt = pt, driver = driver, isFocused = driver.id == focusedDriverId, pulseScale = pulseScale)
        }
    }
}
