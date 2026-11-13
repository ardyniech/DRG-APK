package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
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
fun ProfileSettingsCard(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = "Pengaturan", tint = theme.primaryColor, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Pengaturan Cepat & Keamanan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                }
                TextButton(onClick = onOpenSettings, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("Semua Menu", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                    Spacer(Modifier.width(2.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = "Buka", modifier = Modifier.size(16.dp), tint = theme.primaryColor)
                }
            }
            DrgLuminousGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (isSosAlarmEnabled) DrgRedContainer.copy(alpha = 0.65f) else theme.pillBg,
                borderHighlightColor = if (isSosAlarmEnabled) DrgRedDanger.copy(alpha = 0.45f) else theme.glassBorderColor,
                contentPadding = 12.dp
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSosAlarmEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = "Alarm Desibel",
                            tint = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Sirine SOS Desibel Tinggi",
                                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary
                            )
                            Text(
                                text = if (isSosAlarmEnabled) "100 dB Aktif • Memperingatkan sekitar" else "Mute • Hanya notifikasi visual",
                                fontFamily = SpaceGroteskFontFamily,
                                fontSize = 10.sp, color = DrgTextSecondary
                            )
                        }
                    }
                    Switch(
                        checked = isSosAlarmEnabled,
                        onCheckedChange = onToggleSosAlarm,
                        colors = SwitchDefaults.colors(checkedThumbColor = DrgRedDanger, checkedTrackColor = DrgRedDanger.copy(alpha = 0.3f))
                    )
                }
            }
        }
    }
}
