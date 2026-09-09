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
import androidx.compose.ui.input.pointer.pointerInput
import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.primitives.MapMarkerPainterUtils.drawDriverMarker
import com.example.modules.radar.primitives.MapMarkerPainterUtils.drawHazardMarker
import com.example.modules.radar.primitives.MapMarkerPainterUtils.drawMyGpsPosition
import com.example.modules.radar.primitives.MapMarkerPainterUtils.drawPoskoMarker
import com.example.modules.radar.primitives.MapMarkerPainterUtils.drawRadarSweepEffect
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation
import kotlin.math.sqrt

@Composable
fun MapMarkersLayer(
    members: List<DriverMember>,
    alerts: List<EmergencyAlert>,
    poskoList: List<PoskoLocation>,
    hazards: List<HazardArea>,
    selectedFilter: String,
    centerLat: Double,
    centerLng: Double,
    zoom: Int,
    screenWidth: Float,
    screenHeight: Float,
    isConsentGranted: Boolean,
    showRadarSweep: Boolean,
    onSelectDriver: (DriverMember) -> Unit,
    focusedDriver: DriverMember?,
    modifier: Modifier = Modifier
) {
    if (screenWidth <= 0f || screenHeight <= 0f) return

    val infiniteTransition = rememberInfiniteTransition(label = "markers")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2400, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "radarPulse"
    )

    // Precompute posko locations on screen
    val projectedPoskos = remember(poskoList, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        poskoList.map { posko ->
            MapProjection.latLngToScreen(posko.lat, posko.lng, centerLat, centerLng, zoom, screenWidth, screenHeight)
        }
    }

    // Precompute hazard locations on screen
    val projectedHazards = remember(hazards, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        hazards.map { hazard ->
            Pair(
                MapProjection.latLngToScreen(hazard.lat, hazard.lng, centerLat, centerLng, zoom, screenWidth, screenHeight),
                hazard
            )
        }
    }

    // Precompute driver locations on screen
    val projectedDrivers = remember(members, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        members.map { driver ->
            Pair(
                MapProjection.latLngToScreen(driver.currentLat, driver.currentLng, centerLat, centerLng, zoom, screenWidth, screenHeight),
                driver
            )
        }
    }

    // Precompute my location on screen
    val myPt = remember(centerLat, centerLng, zoom, screenWidth, screenHeight) {
        MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, screenWidth, screenHeight)
    }

    Canvas(
        modifier = modifier.fillMaxSize().pointerInput(projectedDrivers, selectedFilter) {
            detectTapGestures { offset ->
                var bestMatch: DriverMember? = null
                var bestDist = Float.MAX_VALUE

                projectedDrivers.filter { it.second.isOnline }.forEach { (pt, driver) ->
                    val dx = offset.x - pt.x
                    val dy = offset.y - pt.y
                    val dist = sqrt(dx * dx + dy * dy)
                    if (dist < 48f && dist < bestDist) {
                        bestDist = dist
                        bestMatch = driver
                    }
                }
                bestMatch?.let { onSelectDriver(it) }
            }
        }
    ) {
        if (showRadarSweep) {
            val maxR = minOf(screenWidth, screenHeight) * 0.45f
            drawRadarSweepEffect(maxR, Offset(screenWidth / 2f, screenHeight / 2f), pulseRadius)
        }

        if (selectedFilter == "Semua" || selectedFilter == "Posko") {
            projectedPoskos.forEach { pt ->
                drawPoskoMarker(pt)
            }
        }

        if (selectedFilter == "Semua" || selectedFilter == "Area Rawan") {
            projectedHazards.forEach { (pt, hazard) ->
                drawHazardMarker(pt, hazard)
            }
        }

        if (selectedFilter != "Posko" && selectedFilter != "Area Rawan") {
            projectedDrivers.filter { it.second.isLocationSharingConsent }.forEach { (pt, driver) ->
                val isTargeted = focusedDriver?.id == driver.id
                drawDriverMarker(pt, driver, isTargeted, myPt, pulseRadius)
            }
        }

        drawMyGpsPosition(myPt, isConsentGranted, pulseRadius)
    }
}
