package com.example.modules.profile

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
fun ProfileSettingsCard(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Pengaturan",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Pengaturan Cepat & Keamanan",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextPrimary
                    )
                }

                TextButton(
                    onClick = onOpenSettings,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Semua Menu",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Buka",
                        modifier = Modifier.size(16.dp),
                        tint = DrgGreenPrimary
                    )
                }
            }

            // Quick-Access Toggle for High-Decibel SOS Alarm
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSosAlarmEnabled) DrgRedContainer.copy(alpha = 0.4f) else DrgSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSosAlarmEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Alarm Desibel",
                            tint = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Sirine SOS Desibel Tinggi",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary
                            )
                            Text(
                                text = if (isSosAlarmEnabled) "100 dB Aktif • Memperingatkan sekitar" else "Mute • Hanya notifikasi visual",
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
            }
        }
    }
}
