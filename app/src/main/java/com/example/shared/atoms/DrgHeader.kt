package com.example.shared.atoms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.models.DriverMember

@Composable
fun DrgHeader(
    currentMember: DriverMember?,
    activeEmergencyCount: Int,
    onEmergencyBadgeClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
