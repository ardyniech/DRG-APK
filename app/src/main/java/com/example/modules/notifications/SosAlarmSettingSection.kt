package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun SosAlarmSettingSection(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    isTestingSound: Boolean,
    onTestToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isSosAlarmEnabled) DrgRedPanic.copy(alpha = 0.08f) else DrgGlassSurfaceSoft,
        borderHighlightColor = if (isSosAlarmEnabled) DrgRedPanic.copy(alpha = 0.65f) else theme.glassBorderColor.copy(alpha = 0.6f),
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSosAlarmEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sirine SOS",
                        tint = if (isSosAlarmEnabled) DrgRedPanic else DrgTextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Sirine SOS Desibel Tinggi (100 dB)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSosAlarmEnabled) DrgRedPanic else DrgTextDark
                        )
                        Text(
                            text = if (isSosAlarmEnabled) "Alarm nyaring aktif saat tombol darurat ditekan" else "Alarm hening (radar visual saja)",
                            fontSize = 10.sp,
                            color = DrgTextSecondary
                        )
                    }
                }
                Switch(
                    checked = isSosAlarmEnabled,
                    onCheckedChange = onToggleSosAlarm,
                    colors = SwitchDefaults.colors(checkedThumbColor = DrgRedPanic, checkedTrackColor = DrgRedPanic.copy(alpha = 0.3f))
                )
            }

            if (isSosAlarmEnabled) {
                OutlinedButton(
                    onClick = onTestToggle,
                    modifier = Modifier.fillMaxWidth().height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedPanic)
                ) {
                    Icon(if (isTestingSound) Icons.Default.Stop else Icons.Default.PlayArrow, contentDescription = "Tes Sirine", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isTestingSound) "Hentikan Suara Sirine" else "Uji Suara Sirine (3 Detik)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
