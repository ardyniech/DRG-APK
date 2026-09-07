package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SosAlarmQuickCard(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSosAlarmEnabled) DrgRedContainer.copy(alpha = 0.6f) else DrgSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isSosAlarmEnabled) DrgRedDanger else DrgOutline.copy(alpha = 0.6f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSosAlarmEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Status Sirine SOS",
                        tint = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sirine SOS Desibel Tinggi (100 dB)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isSosAlarmEnabled)
                        "Aktif • Sirine frekuensi tinggi membahana saat tombol SOS dipancarkan."
                    else
                        "Nonaktif (Mode Senyap) • Hanya sinyal visual disiarkan ke radar.",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onOpenSettings, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Buka Pengaturan Alarm",
                        tint = DrgTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Switch(
                    checked = isSosAlarmEnabled,
                    onCheckedChange = onToggleSosAlarm,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DrgRedDanger,
                        checkedTrackColor = DrgRedDanger.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}
