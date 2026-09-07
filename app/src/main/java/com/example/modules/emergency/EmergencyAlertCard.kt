package com.example.modules.emergency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.ui.theme.*

@Composable
fun EmergencyAlertCard(
    alert: EmergencyAlert,
    currentMember: DriverMember?,
    onResolve: () -> Unit,
    onRespond: () -> Unit,
    onCall: () -> Unit
) {
    val isLeaderOrSatgas = currentMember?.role?.name in listOf("SATGAS", "KETUA", "WAKIL_KETUA")

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (alert.isActive) DrgRedDanger.copy(alpha = 0.5f) else DrgOutline.copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = alert.type.label,
                        tint = if (alert.isActive) DrgRedDanger else DrgTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = alert.type.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (alert.isActive) DrgRedDanger else DrgTextSecondary
                    )
                }
                Text(
                    text = if (alert.isActive) "🚨 AKTIF" else "✅ SELESAI",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (alert.isActive) DrgRedDanger else DrgGreenPrimary
                )
            }

            Text(
                text = "${alert.driverName} (${alert.plateNumber})",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DrgTextPrimary
            )
            Text(text = "📍 ${alert.locationName}", fontSize = 11.sp, color = DrgTextSecondary)
            Text(text = "💬 \"${alert.message}\"", fontSize = 12.sp, color = DrgTextPrimary)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCall,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Hubungi", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Telepon", fontSize = 11.sp)
                }

                if (alert.isActive) {
                    Button(
                        onClick = onRespond,
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        Text("Bantu (${alert.responderCount})", fontSize = 11.sp)
                    }

                    if (isLeaderOrSatgas) {
                        Button(
                            onClick = onResolve,
                            colors = ButtonDefaults.buttonColors(containerColor = DrgGreenDark),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(vertical = 6.dp)
                        ) {
                            Text("Selesai", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
