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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.modules.radar.logic.MapProjection
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation
import com.example.ui.theme.*
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
        initialValue = 0.1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(members, selectedFilter, centerLat, centerLng, zoom) {
                detectTapGestures { offset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    var bestMatch: DriverMember? = null
                    var bestDist = Float.MAX_VALUE

                    members.filter { it.isOnline }.forEach { driver ->
                        val pt = MapProjection.latLngToScreen(
                            driver.currentLat, driver.currentLng,
                            centerLat, centerLng, zoom, w, h
                        )
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

        // Optional Tactical Radar Beam Sweep
        if (showRadarSweep) {
            val maxR = minOf(w, h) * 0.45f
            val centerOffset = Offset(w / 2f, h / 2f)
            drawCircle(
                color = DrgRadarGlow.copy(alpha = 0.15f),
                radius = maxR * 0.5f,
                center = centerOffset,
                style = Stroke(width = 1f)
            )
            drawCircle(
                color = DrgRadarGlow.copy(alpha = 0.22f),
                radius = maxR,
                center = centerOffset,
                style = Stroke(width = 1.2f)
            )
            drawCircle(
                color = DrgRadarGlow.copy(alpha = (1f - pulseRadius) * 0.35f),
                radius = maxR * pulseRadius,
                center = centerOffset,
                style = Stroke(width = 2f)
            )
        }

        // Draw Posko Basecamps
        if (selectedFilter == "Semua" || selectedFilter == "Posko") {
            poskoList.forEach { posko ->
                val pt = MapProjection.latLngToScreen(posko.lat, posko.lng, centerLat, centerLng, zoom, w, h)
                drawCircle(color = Color(0xFF0288D1).copy(alpha = 0.25f), radius = 22f, center = pt)
                drawCircle(color = Color(0xFF0288D1), radius = 8.5f, center = pt)
                drawCircle(color = Color.White, radius = 3.5f, center = pt)
            }
        }

        // Draw Hazard Danger Areas
        if (selectedFilter == "Semua" || selectedFilter == "Area Rawan") {
            hazards.forEach { hazard ->
                val pt = MapProjection.latLngToScreen(hazard.lat, hazard.lng, centerLat, centerLng, zoom, w, h)
                val hazardColor = Color(hazard.hazardType.colorHex)
                drawCircle(color = hazardColor.copy(alpha = 0.3f), radius = 24f, center = pt)
                drawCircle(color = hazardColor, radius = 9f, center = pt)
                drawCircle(color = Color.White, radius = 3.5f, center = pt)
            }
        }

        // Draw Active Driver Members
        val myPt = MapProjection.latLngToScreen(centerLat, centerLng, centerLat, centerLng, zoom, w, h)
        if (selectedFilter != "Posko" && selectedFilter != "Area Rawan") {
            members.filter { it.isLocationSharingConsent }.forEach { driver ->
                val pt = MapProjection.latLngToScreen(
                    driver.currentLat, driver.currentLng,
                    centerLat, centerLng, zoom, w, h
                )
                val isTargeted = focusedDriver?.id == driver.id
                val pinColor = when {
                    driver.role.name == "SATGAS" -> Color(0xFFE53935)
                    driver.currentStatus == "Sedang Narik" -> Color(0xFFFF8F00)
                    else -> DrgGreenPrimary
                }

                if (isTargeted) {
                    drawCircle(
                        color = pinColor.copy(alpha = 0.4f),
                        radius = 24f + (pulseRadius * 10f),
                        center = pt,
                        style = Stroke(width = 2f)
                    )
                    // Tactical dotted line
                    drawLine(
                        color = pinColor.copy(alpha = 0.7f),
                        start = myPt,
                        end = pt,
                        strokeWidth = 2.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                    )
                }

                drawCircle(color = pinColor.copy(alpha = 0.28f), radius = 16f, center = pt)
                drawCircle(color = pinColor, radius = 8.5f, center = pt)
                drawCircle(color = Color.White, radius = 3.5f, center = pt)
            }
        }

        // Draw My GPS Position
        if (isConsentGranted) {
            drawCircle(
                color = DrgGreenPrimary.copy(alpha = 0.25f),
                radius = 26f * (0.6f + (pulseRadius * 0.4f)),
                center = myPt,
                style = Stroke(width = 2f)
            )
            drawCircle(color = Color.White, radius = 9.5f, center = myPt)
            drawCircle(color = DrgGreenPrimary, radius = 6f, center = myPt)
        } else {
            drawCircle(color = Color.Gray, radius = 7f, center = myPt)
        }
    }
}
