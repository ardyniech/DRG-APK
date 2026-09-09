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
import com.example.modules.radar.models.FreeMapMode
import com.example.shared.models.*

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
    isPowerSaverEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    var mapMode by remember { mutableStateOf(FreeMapMode.ROAD) }
    var isRadarSweepEnabled by remember { mutableStateOf(false) }
    var isTrafficEnabled by remember { mutableStateOf(false) }

    val defaultLat = -7.9822
    val defaultLng = 112.6303
    var centerLat by remember { mutableDoubleStateOf(defaultLat) }
    var centerLng by remember { mutableDoubleStateOf(defaultLng) }
    var zoom by remember { mutableIntStateOf(14) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

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

        FreeMapTileLayer(mapMode = mapMode, centerLat = centerLat, centerLng = centerLng, zoom = zoom, screenWidth = w, screenHeight = h)

        MapMarkersLayer(
            members = members, alerts = alerts, poskoList = poskoList, hazards = hazards,
            selectedFilter = selectedFilter, centerLat = centerLat, centerLng = centerLng, zoom = zoom,
            screenWidth = w, screenHeight = h, isConsentGranted = isConsentGranted,
            showRadarSweep = isRadarSweepEnabled && !isPowerSaverEnabled, onSelectDriver = onSelectDriver,
            focusedDriver = focusedDriver, isTrafficEnabled = isTrafficEnabled
        )

        MapTopControlBar(
            mapMode = mapMode,
            onToggleMapMode = { mapMode = it },
            isTrafficEnabled = isTrafficEnabled,
            onToggleTraffic = { isTrafficEnabled = it },
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
        )

        MapSideControlColumn(
            isRadarSweepEnabled = isRadarSweepEnabled,
            onToggleRadarSweep = { isRadarSweepEnabled = it },
            onZoomIn = { if (zoom < mapMode.maxZoom) zoom++ },
            onZoomOut = { if (zoom > mapMode.minZoom) zoom-- },
            onRecenterGps = { centerLat = defaultLat; centerLng = defaultLng; zoom = 14 },
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)
        )
    }
}
