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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val cardShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White,
            Color.White.copy(alpha = 0.5f),
            if (isCrashGuardEnabled) DrgGreenPrimary.copy(alpha = 0.25f) else DrgOutline.copy(alpha = 0.3f)
        )
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
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
                        text = "Crash Guard (Deteksi Jatuh)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DrgTextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCrashGuardEnabled) DrgGreenContainer.copy(alpha = 0.8f) else DrgSurfaceVariant
                ) {
                    Text(
                        text = if (isCrashGuardEnabled) "Aktif • ${sensitivity.displayName}" else "Nonaktif",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCrashGuardEnabled) DrgGreenPrimary else DrgTextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = if (isCrashGuardEnabled)
                    "Sensor siap memicu auto-SOS jika benturan terdeteksi ${sensitivity.thresholdG}G."
                else
                    "Fitur deteksi benturan nonaktif. Aktifkan demi proteksi di jalan raya.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isCrashGuardEnabled) {
                    OutlinedButton(
                        onClick = onSimulateCrash,
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(10.dp),
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
