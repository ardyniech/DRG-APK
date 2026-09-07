package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.shared.models.EmergencyAlert
import com.example.ui.theme.*

@Composable
fun ActiveAlertBanner(
    alert: EmergencyAlert,
    onViewDetail: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgRedDanger.copy(alpha = 0.1f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgRedDanger, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
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
                    modifier = Modifier.size(20.dp)
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
            TextButton(onClick = onViewDetail) {
                Text("Respon", color = DrgRedDanger, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
