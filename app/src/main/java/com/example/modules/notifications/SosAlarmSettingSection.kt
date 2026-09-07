package com.example.modules.notifications

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
fun SosAlarmSettingSection(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    isTestingSound: Boolean,
    onTestToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSosAlarmEnabled) DrgRedContainer.copy(alpha = 0.7f) else DrgSurfaceVariant.copy(alpha = 0.5f),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isSosAlarmEnabled) DrgRedDanger else DrgOutline.copy(alpha = 0.4f),
                RoundedCornerShape(14.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSosAlarmEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sirine SOS",
                        tint = if (isSosAlarmEnabled) DrgRedDanger else DrgTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Sirine SOS Desibel Tinggi (100 dB)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary
                        )
                        Text(
                            text = if (isSosAlarmEnabled) "Alarm nyaring aktif saat tombol darurat ditekan" else "Alarm hening (hanya sinyal radar visual)",
                            fontSize = 10.sp,
                            color = DrgTextSecondary
                        )
                    }
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

            if (isSosAlarmEnabled) {
                OutlinedButton(
                    onClick = onTestToggle,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedDanger)
                ) {
                    Icon(
                        imageVector = if (isTestingSound) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = "Tes Sirine",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isTestingSound) "Hentikan Suara Sirine" else "Uji Suara Sirine (3 Detik)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
