package com.example.modules.emergency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val cardShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White,
            Color.White.copy(alpha = 0.5f),
            if (isSosAlarmEnabled) DrgRedDanger.copy(alpha = 0.35f) else DrgGreenPrimary.copy(alpha = 0.15f)
        )
    )

    Surface(
        shape = cardShape,
        color = if (isSosAlarmEnabled) DrgRedContainer.copy(alpha = 0.85f) else Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSosAlarmEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = "Status Sirine SOS",
                        tint = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sirine SOS Desibel Tinggi (100 dB)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isSosAlarmEnabled)
                        "Aktif • Sirine frekuensi tinggi membahana saat tombol SOS dipancarkan."
                    else
                        "Nonaktif (Mode Senyap) • Hanya sinyal visual disiarkan ke radar.",
                    fontSize = 10.sp,
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
