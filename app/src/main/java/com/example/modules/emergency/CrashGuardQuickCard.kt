package com.example.modules.emergency

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
fun CrashGuardQuickCard(
    isCrashGuardEnabled: Boolean,
    sensitivity: CrashSensitivity,
    onSimulateCrash: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isCrashGuardEnabled) DrgSurfaceVariant else DrgSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isCrashGuardEnabled) DrgGreenPrimary.copy(alpha = 0.5f) else DrgOutline.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Sensors,
                        contentDescription = "Crash Guard",
                        tint = if (isCrashGuardEnabled) DrgGreenPrimary else DrgTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Crash Guard (Deteksi Jatuh/Tabrakan)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DrgTextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isCrashGuardEnabled) DrgGreenContainer else DrgSurfaceVariant
                ) {
                    Text(
                        text = if (isCrashGuardEnabled) "Aktif • ${sensitivity.displayName}" else "Nonaktif",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCrashGuardEnabled) DrgGreenPrimary else DrgTextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = if (isCrashGuardEnabled)
                    "Sensor akselerometer siap memicu alarm & auto-SOS 10 detik jika terdeteksi g-force benturan ${sensitivity.thresholdG}G."
                else
                    "Fitur deteksi benturan dinonaktifkan. Aktifkan di pengaturan demi proteksi di jalan.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isCrashGuardEnabled) {
                    OutlinedButton(
                        onClick = onSimulateCrash,
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgOrangeWarning)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Uji Simulasi (10s)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                TextButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Text("Pengaturan Sensor", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                }
            }
        }
    }
}
