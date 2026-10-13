package com.example.modules.radar.googlemaps.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun GoogleMapsViewContainer(
    drivers: List<DriverMember>,
    selectedDriver: DriverMember?,
    onSelectDriver: (DriverMember) -> Unit,
    mapType: MapType,
    isTrafficEnabled: Boolean,
    centerLat: Double,
    centerLng: Double,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 14f)
    }

    LaunchedEffect(selectedDriver) {
        selectedDriver?.let {
            if (!it.currentLat.isNaN() && !it.currentLng.isNaN()) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(LatLng(it.currentLat, it.currentLng), 16f)
                )
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isTrafficEnabled = isTrafficEnabled, mapType = mapType),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, compassEnabled = true,
                rotationGesturesEnabled = true, scrollGesturesEnabled = true,
                tiltGesturesEnabled = true, zoomGesturesEnabled = true
            )
        ) {
            drivers.forEach { driver ->
                if (!driver.currentLat.isNaN() && !driver.currentLng.isNaN()) {
                    val hue = when {
                        driver.role == MemberRole.SATGAS -> BitmapDescriptorFactory.HUE_RED
                        driver.currentStatus == "Sedang Narik" -> BitmapDescriptorFactory.HUE_GREEN
                        driver.currentStatus == "Tersedia" -> BitmapDescriptorFactory.HUE_AZURE
                        else -> BitmapDescriptorFactory.HUE_ORANGE
                    }
                    Marker(
                        state = rememberMarkerState(key = driver.id, position = LatLng(driver.currentLat, driver.currentLng)),
                        title = "${driver.name} (${driver.motorcyclePlate})",
                        snippet = "${driver.currentStatus} • ${driver.role.name}",
                        icon = BitmapDescriptorFactory.defaultMarker(hue),
                        onClick = { onSelectDriver(driver); false }
                    )
                }
            }
        }

        GoogleMapsSideControls(
            onZoomIn = { coroutineScope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomIn()) } },
            onZoomOut = { coroutineScope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomOut()) } },
            onRecenter = {
                coroutineScope.launch {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(centerLat, centerLng), 15f))
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
