package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    isConsentGranted: Boolean,
    showRadarSweep: Boolean,
    onSelectDriver: (DriverMember) -> Unit,
    focusedDriver: DriverMember?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "markers")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2400, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "radarPulse"
    )

    Canvas(
        modifier = modifier.fillMaxSize().pointerInput(members, selectedFilter, centerLat, centerLng, zoom) {
            detectTapGestures { offset ->
                val w = size.width.toFloat()
                val h = size.height.toFloat()
                var bestMatch: DriverMember? = null
                var bestDist = Float.MAX_VALUE

                members.filter { it.isOnline }.forEach { driver ->
                    val pt = MapProjection.latLngToScreen(driver.currentLat, driver.currentLng, centerLat, centerLng, zoom, w, h)
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
        val w = size.width
        val h = size.height
        if (w <= 0 || h <= 0) return@Canvas

        if (showRadarSweep) {
            val maxR = minOf(w, h) * 0.45f
            drawRadarSweepEffect(maxR, Offset(w / 2f, h / 2f), pulseRadius)
        }

        if (selectedFilter == "Semua" || selectedFilter == "Posko") {
            poskoList.forEach { posko ->
                val pt = MapProjection.latLngToScreen(posko.lat, posko.lng, centerLat, centerLng, zoom, w, h)
                drawPoskoMarker(pt)
            }
        }

        if (selectedFilter == "Semua" || selectedFilter == "Area Rawan") {
            hazards.forEach { hazard ->
                val pt = MapProjection.latLngToScreen(hazard.lat, hazard.lng, centerLat, centerLng, zoom, w, h)
                drawHazardMarker(pt, hazard)
            }
        }

        val myPt = MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, w, h)
        if (selectedFilter != "Posko" && selectedFilter != "Area Rawan") {
            members.filter { it.isLocationSharingConsent }.forEach { driver ->
                val pt = MapProjection.latLngToScreen(driver.currentLat, driver.currentLng, centerLat, centerLng, zoom, w, h)
                val isTargeted = focusedDriver?.id == driver.id
                drawDriverMarker(pt, driver, isTargeted, myPt, pulseRadius)
            }
        }

        drawMyGpsPosition(myPt, isConsentGranted, pulseRadius)
    }
}
