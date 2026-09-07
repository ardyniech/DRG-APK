package com.example.modules.radar

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.primitives.DriverRadarCard
import com.example.modules.radar.primitives.FreeMapContainer
import com.example.modules.radar.primitives.RadarConsentAndHazardBar
import com.example.modules.radar.primitives.RadarRecommendationCard
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation
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
    val filters = listOf("Semua", "Satgas", "Posko", "Area Rawan", "Sedang Narik")

    val displayMembers = remember(members, selectedFilter) {
        when (selectedFilter) {
            "Satgas" -> members.filter { it.role.name == "SATGAS" }
            "Sedang Narik" -> members.filter { it.currentStatus == "Sedang Narik" }
            else -> members.filter { it.isOnline }
        }
    }

    val isConsentOn = currentMember?.isLocationSharingConsent ?: true
    val isVerified = currentMember?.verificationStatus?.name == "VERIFIED"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // 1. Smart Recommendation Card (Just-in-Time Otoritas)
        RadarRecommendationCard(isConsentOn = isConsentOn, isVerified = isVerified)

        // 2. Live Consent & Add Hazard Quick Action
        RadarConsentAndHazardBar(
            isConsentOn = isConsentOn,
            onToggleConsent = onToggleConsent,
            onAddHazardClick = onAddHazardClick
        )

        // 3. Horizontal Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DrgGreenPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // 4. Free Map Canvas (Satellite, Default Road OSM & Real-Time Traffic Layering)
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            FreeMapContainer(
                members = displayMembers,
                alerts = alerts,
                poskoList = poskoList,
                hazards = hazards,
                selectedFilter = selectedFilter,
                isConsentGranted = isConsentOn,
                onSelectDriver = { focusedDriver = it },
                focusedDriver = focusedDriver,
                onTriggerEmergency = onTriggerEmergency,
                isPowerSaverEnabled = isPowerSaverEnabled
            )
        }

        // 5. Selected Driver Radar Bottom Card
        val target = focusedDriver ?: displayMembers.firstOrNull { it.isOnline && it.id != currentMember?.id }
        if (target != null) {
            DriverRadarCard(driver = target, onCall = { onCallDriver(target.phone) })
        }
    }
}
