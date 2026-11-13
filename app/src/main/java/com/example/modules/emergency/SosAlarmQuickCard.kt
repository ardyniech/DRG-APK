package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun SosAlarmQuickCard(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = if (isSosAlarmEnabled) DrgRedContainer.copy(alpha = 0.86f) else theme.glassBg,
        borderHighlightColor = if (isSosAlarmEnabled) DrgRedDanger.copy(alpha = 0.55f) else theme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Sirine SOS Desibel Tinggi (100 dB)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (isSosAlarmEnabled) "Aktif • Sirine frekuensi tinggi membahana saat tombol SOS dipancarkan."
                    else "Nonaktif (Mode Senyap) • Hanya sinyal visual disiarkan ke radar.",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )
            }
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = isSosAlarmEnabled,
                onCheckedChange = onToggleSosAlarm,
                colors = SwitchDefaults.colors(checkedThumbColor = DrgRedDanger, checkedTrackColor = DrgRedDanger.copy(alpha = 0.3f))
            )
        }
    }
}
