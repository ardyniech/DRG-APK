package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextSecondary

@Composable
fun EmergencyTabSelectorRow(
    selectedTab: Int,
    activeAlertsCount: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.85f))
            .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Button(
            onClick = { onTabSelected(0) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTab == 0) DrgRedDanger else Color.Transparent,
                contentColor = if (selectedTab == 0) Color.White else DrgTextSecondary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f).height(40.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("🚨 Kirim SOS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Button(
            onClick = { onTabSelected(1) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTab == 1) DrgGreenPrimary else Color.Transparent,
                contentColor = if (selectedTab == 1) Color.White else DrgTextSecondary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f).height(40.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("📢 Respon SOS ($activeAlertsCount)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
