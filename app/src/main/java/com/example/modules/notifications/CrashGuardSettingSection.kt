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
import com.example.shared.models.CrashSensitivity
import com.example.ui.theme.*

@Composable
fun CrashGuardSettingSection(
    isCrashGuardEnabled: Boolean,
    onToggleCrashGuard: (Boolean) -> Unit,
    sensitivity: CrashSensitivity,
    onSelectSensitivity: (CrashSensitivity) -> Unit,
    onSimulateCrash: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isCrashGuardEnabled) DrgSurfaceVariant else DrgSurfaceVariant.copy(alpha = 0.4f),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isCrashGuardEnabled) DrgGreenPrimary.copy(alpha = 0.6f) else DrgOutline.copy(alpha = 0.4f),
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
                        imageVector = Icons.Default.Sensors,
                        contentDescription = "Deteksi Benturan",
                        tint = if (isCrashGuardEnabled) DrgGreenPrimary else DrgTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Deteksi Benturan Keras (Crash Guard)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DrgTextPrimary
                        )
                        Text(
                            text = if (isCrashGuardEnabled) "Aktif • Pantau sensor G-Force motor" else "Nonaktif",
                            fontSize = 10.sp,
                            color = DrgTextSecondary
                        )
                    }
                }
                Switch(
                    checked = isCrashGuardEnabled,
                    onCheckedChange = onToggleCrashGuard,
                    colors = SwitchDefaults.colors(checkedThumbColor = DrgGreenPrimary)
                )
            }

            if (isCrashGuardEnabled) {
                Text("Sensitivitas Sensor:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CrashSensitivity.entries.forEach { item ->
                        FilterChip(
                            selected = sensitivity == item,
                            onClick = { onSelectSensitivity(item) },
                            label = { Text(item.displayName, fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DrgGreenPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = DrgGreenPrimary
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = onSimulateCrash,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgOrangeWarning)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Uji Simulasi Benturan (Countdown 10s)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
