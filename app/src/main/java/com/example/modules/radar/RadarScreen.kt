package com.example.modules.radar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modules.radar.primitives.*
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun RadarScreen(
    members: List<DriverMember>,
    alerts: List<EmergencyAlert>,
    poskoList: List<PoskoLocation>,
    hazards: List<HazardArea> = emptyList(),
    currentMember: DriverMember? = null,
    onToggleConsent: (Boolean) -> Unit = {},
    onAddHazardClick: () -> Unit = {},
    onConfirmHazard: (String) -> Unit = {},
    onTriggerEmergency: () -> Unit = {},
    onCallDriver: (String) -> Unit = {},
    isPowerSaverEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    var focusedDriver by remember { mutableStateOf<DriverMember?>(null) }
    var selectedHazard by remember { mutableStateOf<HazardArea?>(null) }
    var selectedPosko by remember { mutableStateOf<PoskoLocation?>(null) }

    val filters = listOf("Semua", "Lalu Lintas", "Satgas", "Posko", "Area Rawan", "Sedang Narik")

    val displayMembers = remember(members, selectedFilter) {
        when (selectedFilter) {
            "Satgas" -> members.filter { it.role.name == "SATGAS" }
            "Sedang Narik" -> members.filter { it.currentStatus == "Sedang Narik" }
            else -> members.filter { it.isOnline }
        }
    }

    val isConsentOn = currentMember?.isLocationSharingConsent ?: true

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        RadarConsentAndHazardBar(
            isConsentOn = isConsentOn,
            onToggleConsent = onToggleConsent,
            onAddHazardClick = onAddHazardClick
        )

        RadarFilterChipsRow(
            filters = filters,
            selectedFilter = selectedFilter,
            onSelectFilter = {
                selectedFilter = it
                selectedHazard = null
                selectedPosko = null
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            FreeMapContainer(
                members = displayMembers,
                alerts = alerts,
                poskoList = poskoList,
                hazards = hazards,
                selectedFilter = selectedFilter,
                isConsentGranted = isConsentOn,
                onSelectDriver = {
                    focusedDriver = it
                    selectedHazard = null
                    selectedPosko = null
                },
                focusedDriver = focusedDriver,
                onSelectHazard = {
                    selectedHazard = it
                    focusedDriver = null
                    selectedPosko = null
                },
                onSelectPosko = {
                    selectedPosko = it
                    focusedDriver = null
                    selectedHazard = null
                },
                isPowerSaverEnabled = isPowerSaverEnabled
            )
        }

        RadarBottomDetailCard(
            selectedHazard = selectedHazard,
            selectedPosko = selectedPosko,
            focusedDriver = focusedDriver,
            displayMembers = displayMembers,
            currentMember = currentMember,
            selectedFilter = selectedFilter,
            poskoList = poskoList,
            onConfirmHazard = {
                onConfirmHazard(it)
                selectedHazard = null
            },
            onCallDriver = onCallDriver
        )
    }
}
