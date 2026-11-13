package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun EmergencyTabSelectorRow(
    selectedTab: Int,
    activeAlertsCount: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Button(
                onClick = { onTabSelected(0) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 0) DrgRedDanger else Color.Transparent,
                    contentColor = if (selectedTab == 0) Color.White else DrgTextSecondary
                ),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f).height(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) { Text("🚨 Kirim SOS", fontWeight = FontWeight.Bold, fontSize = 12.sp) }

            Button(
                onClick = { onTabSelected(1) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 1) theme.primaryColor else Color.Transparent,
                    contentColor = if (selectedTab == 1) Color.White else DrgTextSecondary
                ),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f).height(40.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "📢 Respon SOS ($activeAlertsCount)",
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
