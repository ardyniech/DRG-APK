package com.example.modules.radar.primitives

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.logic.TrafficDataRepository
import com.example.modules.radar.models.FreeMapMode
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation

@Composable
fun FreeMapContainer(
    members: List<DriverMember>,
    alerts: List<EmergencyAlert>,
    poskoList: List<PoskoLocation>,
    hazards: List<HazardArea>,
    selectedFilter: String,
    isConsentGranted: Boolean,
    onSelectDriver: (DriverMember) -> Unit,
    focusedDriver: DriverMember?,
    onTriggerEmergency: () -> Unit,
    isPowerSaverEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    var mapMode by remember { mutableStateOf(FreeMapMode.ROAD) }
    var isTrafficEnabled by remember { mutableStateOf(true) }
    var isRadarSweepEnabled by remember { mutableStateOf(false) }

    val defaultLat = -7.9822
    val defaultLng = 112.6303
    var centerLat by remember { mutableDoubleStateOf(defaultLat) }
    var centerLng by remember { mutableDoubleStateOf(defaultLng) }
    var zoom by remember { mutableIntStateOf(14) }

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val roadSegments = remember { TrafficDataRepository.getRoadSegments() }
    val trafficIncidents = remember { TrafficDataRepository.getIncidents() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(18.dp))
            .onSizeChanged { containerSize = it }
            .pointerInput(centerLat, centerLng, zoom, containerSize) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    if (containerSize.width > 0 && containerSize.height > 0) {
                        val currentPair = MapProjection.screenToLatLng(
                            containerSize.width / 2f - dragAmount.x,
                            containerSize.height / 2f - dragAmount.y,
                            centerLat, centerLng, zoom,
                            containerSize.width.toFloat(), containerSize.height.toFloat()
                        )
                        centerLat = currentPair.first.coerceIn(-8.5, -7.5)
                        centerLng = currentPair.second.coerceIn(112.0, 113.2)
                    }
                }
            }
    ) {
        val w = containerSize.width.toFloat()
        val h = containerSize.height.toFloat()

        FreeMapTileLayer(
            mapMode = mapMode, centerLat = centerLat, centerLng = centerLng,
            zoom = zoom, screenWidth = w, screenHeight = h
        )

        TrafficOverlayCanvas(
            roadSegments = roadSegments, incidents = trafficIncidents,
            centerLat = centerLat, centerLng = centerLng, zoom = zoom,
            isTrafficLayerEnabled = isTrafficEnabled, isPowerSaver = isPowerSaverEnabled
        )

        MapMarkersLayer(
            members = members, alerts = alerts, poskoList = poskoList, hazards = hazards,
            selectedFilter = selectedFilter, centerLat = centerLat, centerLng = centerLng,
            zoom = zoom, isConsentGranted = isConsentGranted,
            showRadarSweep = isRadarSweepEnabled && !isPowerSaverEnabled,
            onSelectDriver = onSelectDriver, focusedDriver = focusedDriver
        )

        // Top Control Bar (Peta/Satelit & Traffic Toggle)
        MapTopControlBar(
            mapMode = mapMode,
            onToggleMapMode = { mapMode = it },
            isTrafficEnabled = isTrafficEnabled,
            onToggleTraffic = { isTrafficEnabled = it },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )

        // Traffic Speed Legend (Top Start)
        if (isTrafficEnabled) {
            TrafficLegendCard(
                avgSpeedKmh = 27,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
        }

        // Side Control Column (Recenter GPS, Radar Sweep, Zoom In/Out) at Center End
        MapSideControlColumn(
            isRadarSweepEnabled = isRadarSweepEnabled,
            onToggleRadarSweep = { isRadarSweepEnabled = it },
            onZoomIn = { if (zoom < mapMode.maxZoom) zoom++ },
            onZoomOut = { if (zoom > mapMode.minZoom) zoom-- },
            onRecenterGps = { centerLat = defaultLat; centerLng = defaultLng; zoom = 14 },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        )
    }
}
