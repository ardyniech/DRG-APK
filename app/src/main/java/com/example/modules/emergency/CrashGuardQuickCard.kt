package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.CrashSensitivity
import com.example.ui.theme.*

@Composable
fun CrashGuardQuickCard(
    isCrashGuardEnabled: Boolean,
    sensitivity: CrashSensitivity,
    onSimulateCrash: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = if (isCrashGuardEnabled) theme.glassBorderColor else DrgOutline,
        contentPadding = 14.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sensors, contentDescription = "Crash Guard", tint = if (isCrashGuardEnabled) theme.primaryColor else DrgTextMuted, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Crash Guard (Deteksi Jatuh)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                }
                Surface(shape = RoundedCornerShape(8.dp), color = if (isCrashGuardEnabled) theme.pillBg else DrgSurfaceVariant) {
                    Text(
                        text = if (isCrashGuardEnabled) "Aktif • ${sensitivity.displayName}" else "Nonaktif",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCrashGuardEnabled) theme.primaryColor else DrgTextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            Text(
                text = if (isCrashGuardEnabled) "Sensor siap memicu auto-SOS jika benturan terdeteksi ${sensitivity.thresholdG}G."
                else "Fitur deteksi benturan nonaktif. Aktifkan demi proteksi di jalan raya.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )
            if (isCrashGuardEnabled) {
                OutlinedButton(
                    onClick = onSimulateCrash,
                    modifier = Modifier.fillMaxWidth().height(38.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgOrangeWarning)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Uji Simulasi (10s)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text("Konfigurasi lengkap sensor tersedia di Profil -> Pengaturan.", fontSize = 10.sp, color = DrgTextMuted)
        }
    }
}
