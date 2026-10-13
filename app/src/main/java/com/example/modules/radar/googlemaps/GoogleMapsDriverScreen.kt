package com.example.modules.radar.googlemaps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modules.radar.googlemaps.models.DriverMapFilter
import com.example.modules.radar.googlemaps.primitives.*
import com.example.modules.radar.logic.CoordinateUtils
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.DrgBackground
import com.google.maps.android.compose.MapType

@Composable
fun GoogleMapsDriverScreen(
    members: List<DriverMember>,
    currentMember: DriverMember?,
    onCallDriver: (String) -> Unit,
    onSwitchToFreeMap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(DriverMapFilter.ALL) }
    var selectedDriver by remember { mutableStateOf<DriverMember?>(null) }
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    var isTrafficEnabled by remember { mutableStateOf(true) }

    val defaultLat = currentMember?.currentLat ?: -7.9822
    val defaultLng = currentMember?.currentLng ?: 112.6303

    val filteredDrivers = remember(members, selectedFilter) {
        members.filter { it.isOnline && it.isLocationSharingConsent }.filter {
            when (selectedFilter) {
                DriverMapFilter.ALL -> true
                DriverMapFilter.ACTIVE_DUTY -> it.currentStatus == "Sedang Narik"
                DriverMapFilter.SATGAS -> it.role == MemberRole.SATGAS
                DriverMapFilter.STANDBY -> it.currentStatus == "Tersedia" || it.currentStatus == "Siaga"
            }
        }
    }

    val distanceKm = remember(selectedDriver, defaultLat, defaultLng) {
        selectedDriver?.let {
            CoordinateUtils.calculateDistanceKm(defaultLat, defaultLng, it.currentLat, it.currentLng)
        } ?: 0.0
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
    ) {
        GoogleMapsViewContainer(
            drivers = filteredDrivers,
            selectedDriver = selectedDriver,
            onSelectDriver = { selectedDriver = it },
            mapType = mapType,
            isTrafficEnabled = isTrafficEnabled,
            centerLat = defaultLat,
            centerLng = defaultLng
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            GoogleMapsApiKeyNoticeBanner(onSwitchToFreeMap = onSwitchToFreeMap)

            GoogleMapsTopControlBar(
                mapType = mapType,
                onCycleMapType = {
                    mapType = when (mapType) {
                        MapType.NORMAL -> MapType.HYBRID
                        MapType.HYBRID -> MapType.TERRAIN
                        else -> MapType.NORMAL
                    }
                },
                isTrafficEnabled = isTrafficEnabled,
                onToggleTraffic = { isTrafficEnabled = !isTrafficEnabled },
                onlineDriverCount = filteredDrivers.size
            )

            GoogleMapsDriverFilterBar(
                selectedFilter = selectedFilter,
                onSelectFilter = {
                    selectedFilter = it
                    selectedDriver = null
                }
            )
        }

        selectedDriver?.let { driver ->
            GoogleMapsSelectedDriverCard(
                driver = driver,
                distanceKm = distanceKm,
                onCallDriver = onCallDriver,
                onFocusLocation = { lat, lng ->
                    // Re-trigger location selection
                    selectedDriver = driver.copy(currentLat = lat, currentLng = lng)
                },
                onDismiss = { selectedDriver = null },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
