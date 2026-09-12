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
import com.example.shared.models.HazardType
import com.example.shared.models.PoskoLocation

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
    isTrafficEnabled: Boolean = false,
    onSelectHazard: (HazardArea) -> Unit = {},
    onSelectPosko: (PoskoLocation) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (screenWidth <= 0f || screenHeight <= 0f) return

    val infiniteTransition = rememberInfiniteTransition(label = "markers")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2400, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "radarPulse"
    )

    val displayedHazards = remember(hazards, selectedFilter) {
        if (selectedFilter == "Lalu Lintas") {
            hazards.filter { it.hazardType == HazardType.TRAFFIC_JAM }
        } else {
            hazards
        }
    }

    val projectedPoskos = remember(poskoList, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        poskoList.map { Pair(MapProjection.latLngToScreen(it.lat, it.lng, centerLat, centerLng, zoom, screenWidth, screenHeight), it) }
    }

    val projectedHazards = remember(displayedHazards, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        displayedHazards.map { Pair(MapProjection.latLngToScreen(it.lat, it.lng, centerLat, centerLng, zoom, screenWidth, screenHeight), it) }
    }

    val projectedDrivers = remember(members, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        members.map { Pair(MapProjection.latLngToScreen(it.currentLat, it.currentLng, centerLat, centerLng, zoom, screenWidth, screenHeight), it) }
    }

    val myPt = remember(centerLat, centerLng, zoom, screenWidth, screenHeight) {
        MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, screenWidth, screenHeight)
    }

    Canvas(
        modifier = modifier.fillMaxSize().pointerInput(projectedDrivers, projectedHazards, projectedPoskos, selectedFilter) {
            detectTapGestures { offset ->
                MapTapHandler.findDriverAtTap(offset, projectedDrivers)?.let { onSelectDriver(it); return@detectTapGestures }
                MapTapHandler.findHazardAtTap(offset, projectedHazards)?.let { onSelectHazard(it); return@detectTapGestures }
                MapTapHandler.findPoskoAtTap(offset, projectedPoskos)?.let { onSelectPosko(it); return@detectTapGestures }
            }
        }
    ) {
        if (showRadarSweep) {
            val maxR = minOf(screenWidth, screenHeight) * 0.45f
            drawRadarSweepEffect(maxR, Offset(screenWidth / 2f, screenHeight / 2f), pulseRadius)
        }

        if (selectedFilter == "Semua" || selectedFilter == "Posko") {
            projectedPoskos.forEach { (pt, _) -> drawPoskoMarker(pt) }
        }

        if (selectedFilter == "Semua" || selectedFilter == "Area Rawan" || selectedFilter == "Lalu Lintas") {
            projectedHazards.forEach { (pt, hazard) -> drawHazardMarker(pt, hazard) }
        }

        if (selectedFilter != "Posko" && selectedFilter != "Area Rawan") {
            projectedDrivers.filter { it.second.isLocationSharingConsent }.forEach { (pt, driver) ->
                drawDriverMarker(pt, driver, focusedDriver?.id == driver.id, myPt, pulseRadius)
            }
        }

        drawMyGpsPosition(myPt, isConsentGranted, pulseRadius)
    }
}
