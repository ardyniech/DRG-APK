package com.example.modules.radar.primitives

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.models.DriverMember
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation

@Composable
fun RadarBottomDetailCard(
    selectedHazard: HazardArea?,
    selectedPosko: PoskoLocation?,
    focusedDriver: DriverMember?,
    displayMembers: List<DriverMember>,
    currentMember: DriverMember?,
    selectedFilter: String,
    poskoList: List<PoskoLocation>,
    onConfirmHazard: (String) -> Unit,
    onCallDriver: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedHazard != null) {
        HazardRadarCard(
            hazard = selectedHazard,
            onConfirmHazard = onConfirmHazard
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
                onNavigateClick = {}
            )
        } else {
            val target = focusedDriver ?: displayMembers.firstOrNull { it.isOnline && it.id != currentMember?.id }
            if (target != null) {
                DriverRadarCard(driver = target, onCall = { onCallDriver(target.phone) })
            }
        }
    }
}
