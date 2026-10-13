package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.modules.radar.googlemaps.GoogleMapsDriverScreen
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation

@Composable
fun RadarMapContentHost(
    isGoogleMapsActive: Boolean,
    displayMembers: List<DriverMember>,
    alerts: List<EmergencyAlert>,
    poskoList: List<PoskoLocation>,
    hazards: List<HazardArea>,
    selectedFilter: String,
    isConsentOn: Boolean,
    focusedDriver: DriverMember?,
    onSelectDriver: (DriverMember) -> Unit,
    onSelectHazard: (HazardArea) -> Unit,
    onSelectPosko: (PoskoLocation) -> Unit,
    onCallDriver: (String) -> Unit,
    onSwitchToFreeMap: () -> Unit,
    currentMember: DriverMember?,
    isPowerSaverEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (isGoogleMapsActive) {
            GoogleMapsDriverScreen(
                members = displayMembers,
                currentMember = currentMember,
                onCallDriver = onCallDriver,
                onSwitchToFreeMap = onSwitchToFreeMap
            )
        } else {
            FreeMapContainer(
                members = displayMembers,
                alerts = alerts,
                poskoList = poskoList,
                hazards = hazards,
                selectedFilter = selectedFilter,
                isConsentGranted = isConsentOn,
                onSelectDriver = onSelectDriver,
                focusedDriver = focusedDriver,
                onSelectHazard = onSelectHazard,
                onSelectPosko = onSelectPosko,
                isPowerSaverEnabled = isPowerSaverEnabled
            )
        }
    }
}
