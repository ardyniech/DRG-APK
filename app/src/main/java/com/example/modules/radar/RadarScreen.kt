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
import androidx.compose.ui.platform.LocalContext
import com.example.modules.radar.primitives.*
import com.example.shared.models.*
import com.example.shared.utils.WhatsAppLauncher
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
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("Semua") }
    var focusedDriver by remember { mutableStateOf<DriverMember?>(null) }
    var selectedHazard by remember { mutableStateOf<HazardArea?>(null) }
    var selectedPosko by remember { mutableStateOf<PoskoLocation?>(null) }
    val filters = listOf("Semua", "Lalu Lintas", "Live Map (GPS)", "Satgas", "Posko", "Area Rawan", "Sedang Narik")

    val displayMembers = remember(members, selectedFilter) {
        when (selectedFilter) {
            "Satgas" -> members.filter { it.role.name == "SATGAS" }
            "Sedang Narik" -> members.filter { it.currentStatus == "Sedang Narik" }
            else -> members.filter { it.isOnline }
        }
    }

    val isConsentOn = currentMember?.isLocationSharingConsent ?: true
    val isVerified = currentMember?.verificationStatus?.name == "VERIFIED"

    if (selectedFilter == "Live Map (GPS)") {
        LiveMapScreen(members = members, currentMember = currentMember, onCallDriver = onCallDriver, modifier = modifier)
    } else {
        Column(
            modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 14.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            RadarRecommendationCard(isConsentOn = isConsentOn, isVerified = isVerified)
            RadarConsentAndHazardBar(isConsentOn = isConsentOn, onToggleConsent = onToggleConsent, onAddHazardClick = onAddHazardClick)

            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter; selectedHazard = null; selectedPosko = null },
                        label = { Text(filter, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                FreeMapContainer(
                    members = displayMembers, alerts = alerts, poskoList = poskoList, hazards = hazards,
                    selectedFilter = selectedFilter, isConsentGranted = isConsentOn,
                    onSelectDriver = { focusedDriver = it; selectedHazard = null; selectedPosko = null },
                    focusedDriver = focusedDriver,
                    onSelectHazard = { selectedHazard = it; focusedDriver = null; selectedPosko = null },
                    onSelectPosko = { selectedPosko = it; focusedDriver = null; selectedHazard = null },
                    isPowerSaverEnabled = isPowerSaverEnabled
                )
            }

            if (selectedHazard != null) {
                HazardRadarCard(
                    hazard = selectedHazard!!,
                    onConfirmHazard = { onConfirmHazard(it); selectedHazard = null }
                )
            } else if (selectedFilter == "Lalu Lintas") {
                RealTrafficLiveCard(
                    onOpenRealTraffic = { WhatsAppLauncher.openLiveTrafficMap(context) },
                    onReportJam = onAddHazardClick,
                    trafficHazardsCount = hazards.count { it.hazardType == HazardType.TRAFFIC_JAM }
                )
            } else {
                val posko = selectedPosko ?: if (selectedFilter == "Posko") poskoList.firstOrNull() else null
                if (posko != null) {
                    ShelterRadarCard(
                        name = posko.name,
                        distanceKm = 1.2,
                        availableSlots = posko.activeDriversCount,
                        hasCoffee = true,
                        hasPowerOutlet = true,
                        onNavigateClick = { WhatsAppLauncher.openGoogleMapsAddress(context, "${posko.name} ${posko.address}") }
                    )
                } else {
                    val target = focusedDriver ?: displayMembers.firstOrNull { it.isOnline && it.id != currentMember?.id }
                    if (target != null) {
                        DriverRadarCard(driver = target, onCall = { onCallDriver(target.phone) })
                    }
                }
            }
        }
    }
}
