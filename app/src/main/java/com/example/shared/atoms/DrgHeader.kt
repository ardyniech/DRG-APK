package com.example.shared.atoms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun DrgHeader(
    currentMember: DriverMember?,
    activeEmergencyCount: Int,
    onRoleSwitchClick: () -> Unit,
    onEmergencyBadgeClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = DrgOutline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DrgHeaderBrand(member = currentMember)
            DrgHeaderActions(
                currentMember = currentMember,
                activeEmergencyCount = activeEmergencyCount,
                onRoleSwitchClick = onRoleSwitchClick,
                onEmergencyBadgeClick = onEmergencyBadgeClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}
