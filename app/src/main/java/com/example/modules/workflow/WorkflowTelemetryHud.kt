package com.example.modules.workflow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.*

@Composable
fun WorkflowTelemetryHud(stage: WorkflowStage, modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    val isAlertStage = stage.id == 3
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(20.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = if (isAlertStage) DrgRedDanger.copy(alpha = 0.65f) else theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("TELEMETRI LAPANGAN RIIL (OFF-APP)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                }
                Surface(shape = RoundedCornerShape(6.dp), color = if (stage.speedKmh > 0) theme.primaryColor else DrgAmberDark) {
                    Text(
                        text = if (stage.speedKmh > 0) "MELAJU" else "STANDBY / REHAT",
                        fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("KECEPATAN", fontSize = 9.sp, color = DrgTextMuted, fontWeight = FontWeight.SemiBold)
                    Text("${stage.speedKmh} KM/H", fontFamily = SpaceGroteskFontFamily, fontSize = 22.sp, fontWeight = FontWeight.Black, color = theme.primaryColor)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SENSOR G-FORCE", fontSize = 9.sp, color = DrgTextMuted, fontWeight = FontWeight.SemiBold)
                    Text(stage.gForceText, fontFamily = SpaceGroteskFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (isAlertStage) DrgRedDanger else DrgTextDark)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("CUACA JALAN", fontSize = 9.sp, color = DrgTextMuted, fontWeight = FontWeight.SemiBold)
                    Text(stage.weather, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
                }
            }
            Surface(shape = RoundedCornerShape(10.dp), color = theme.pillBg, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Status Sistem DRG:", fontSize = 10.sp, color = DrgTextSecondary)
                    Text(stage.statusText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }
        }
    }
}
