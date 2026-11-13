package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
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
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isCrashGuardEnabled) theme.pillBg.copy(alpha = 0.55f) else Color(0xCCFFFFFF),
        borderHighlightColor = if (isCrashGuardEnabled) theme.primaryColor.copy(alpha = 0.55f) else theme.glassBorderColor.copy(alpha = 0.5f),
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
                        imageVector = Icons.Default.Sensors,
                        contentDescription = "Deteksi Benturan",
                        tint = if (isCrashGuardEnabled) theme.primaryColor else DrgTextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Deteksi Benturan Keras (Crash Guard)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                        Text(if (isCrashGuardEnabled) "Aktif • Pantau sensor G-Force motor" else "Nonaktif", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                }
                Switch(
                    checked = isCrashGuardEnabled,
                    onCheckedChange = onToggleCrashGuard,
                    colors = SwitchDefaults.colors(checkedThumbColor = theme.primaryColor, checkedTrackColor = theme.primaryColor.copy(alpha = 0.3f))
                )
            }

            if (isCrashGuardEnabled) {
                Text("Sensitivitas Sensor:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CrashSensitivity.entries.forEach { item ->
                        FilterChip(
                            selected = sensitivity == item,
                            onClick = { onSelectSensitivity(item) },
                            label = { Text(item.displayName, fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = theme.pillBg,
                                selectedLabelColor = theme.primaryColor,
                                containerColor = Color(0xDCFFFFFF)
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = onSimulateCrash,
                    modifier = Modifier.fillMaxWidth().height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgAmberSecondary)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Uji Simulasi Benturan (Countdown 10s)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
