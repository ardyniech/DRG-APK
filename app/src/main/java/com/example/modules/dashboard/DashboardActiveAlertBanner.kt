package com.example.modules.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.primitives.GlassCard
import com.example.shared.models.EmergencyAlert
import com.example.ui.theme.*

@Composable
fun ActiveAlertBanner(
    alert: EmergencyAlert,
    onViewDetail: () -> Unit
) {
    GlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = DrgRedContainer.copy(alpha = 0.88f),
        borderHighlightColor = DrgRedDanger,
        contentPadding = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Darurat",
                    tint = DrgRedDanger,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "🚨 SOS: ${alert.type.label}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DrgRedDanger
                    )
                    Text(
                        text = "${alert.driverName} • ${alert.locationName}",
                        fontSize = 11.sp,
                        color = DrgTextPrimary
                    )
                }
            }
            Button(
                onClick = onViewDetail,
                colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Respon", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}
