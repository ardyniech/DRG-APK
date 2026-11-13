package com.example.shared.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun DrgHeaderActions(
    currentMember: DriverMember?,
    activeEmergencyCount: Int,
    onEmergencyBadgeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        if (activeEmergencyCount > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(DrgRedDanger)
                    .clickable { onEmergencyBadgeClick() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = "Emergency Alert", tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$activeEmergencyCount SOS", fontFamily = SpaceGroteskFontFamily, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(theme.pillBg.copy(alpha = 0.85f))
                .border(1.dp, Color.White, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(if (currentMember?.isOnline == true) theme.primaryColor else DrgTextMuted))
                Text(
                    text = currentMember?.role?.shortName ?: "Login",
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextDark
                )
            }
        }
    }
}
