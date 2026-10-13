package com.example.shared.atoms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgGlassBorderBrush

@Composable
fun DrgHeader(
    currentMember: DriverMember?,
    activeEmergencyCount: Int,
    onEmergencyBadgeClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    DrgGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = Color.White.copy(alpha = 0.88f),
        borderBrush = DrgGlassBorderBrush,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DrgHeaderBrand(member = currentMember)
            DrgHeaderActions(
                currentMember = currentMember,
                activeEmergencyCount = activeEmergencyCount,
                onEmergencyBadgeClick = onEmergencyBadgeClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}
