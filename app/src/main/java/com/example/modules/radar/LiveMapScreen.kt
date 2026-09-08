package com.example.modules.radar

import androidx.compose.foundation.background
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
import com.example.modules.radar.logic.CoordinateUtils
import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.models.FreeMapMode
import com.example.modules.radar.primitives.*
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgBackground

@Composable
fun LiveMapScreen(
    members: List<DriverMember>,
    currentMember: DriverMember?,
    onCallDriver: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultLat = currentMember?.currentLat ?: -7.9822
    val defaultLng = currentMember?.currentLng ?: 112.6303
    var centerLat by remember { mutableDoubleStateOf(defaultLat) }
    var centerLng by remember { mutableDoubleStateOf(defaultLng) }
    var zoom by remember { mutableIntStateOf(14) }
    var selectedRadiusKm by remember { mutableDoubleStateOf(5.0) }
    var focusedDriver by remember { mutableStateOf<DriverMember?>(null) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val nearbyDrivers = remember(members, centerLat, centerLng, selectedRadiusKm) {
        CoordinateUtils.filterNearbyDrivers(members, centerLat, centerLng, selectedRadiusKm)
    }
    val currentFocusedPair = nearbyDrivers.firstOrNull { it.first.id == focusedDriver?.id } ?: nearbyDrivers.firstOrNull()

    Column(
        modifier = modifier.fillMaxSize().background(DrgBackground).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LiveMapCoordinateHeader(
            centerLat = centerLat, centerLng = centerLng,
            nearbyCount = nearbyDrivers.size, selectedRadiusKm = selectedRadiusKm,
            onSelectRadius = { selectedRadiusKm = it }
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .onSizeChanged { containerSize = it }
                .pointerInput(centerLat, centerLng, zoom, containerSize) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (containerSize.width > 0 && containerSize.height > 0) {
                            val nextPair = MapProjection.screenToLatLng(
                                containerSize.width / 2f - dragAmount.x, containerSize.height / 2f - dragAmount.y,
                                centerLat, centerLng, zoom, containerSize.width.toFloat(), containerSize.height.toFloat()
                            )
                            centerLat = nextPair.first.coerceIn(-8.5, -7.5)
                            centerLng = nextPair.second.coerceIn(112.0, 113.2)
                        }
                    }
                }
        ) {
            val w = containerSize.width.toFloat()
            val h = containerSize.height.toFloat()

            FreeMapTileLayer(
                mapMode = FreeMapMode.ROAD, centerLat = centerLat, centerLng = centerLng,
                zoom = zoom, screenWidth = w, screenHeight = h
            )

            LiveMapCanvasView(
                membersWithDistance = nearbyDrivers, centerLat = centerLat, centerLng = centerLng,
                zoom = zoom, focusedDriverId = currentFocusedPair?.first?.id,
                onSelectDriver = { focusedDriver = it }
            )

            LiveMapControlsFabColumn(
                onZoomIn = { if (zoom < 18) zoom++ },
                onZoomOut = { if (zoom > 10) zoom-- },
                onRecenterGps = { centerLat = defaultLat; centerLng = defaultLng; zoom = 14 },
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            )
        }

        currentFocusedPair?.let { (driver, dist) ->
            LiveDriverCoordinatesCard(
                driver = driver, distanceKm = dist, onCall = onCallDriver,
                onFocusCoordinates = { lat, lng -> centerLat = lat; centerLng = lng }
            )
        }
    }
}
