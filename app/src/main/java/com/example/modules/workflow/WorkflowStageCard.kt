package com.example.modules.workflow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
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
fun WorkflowStageCard(stage: WorkflowStage, modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stage.iconEmoji, fontSize = 24.sp)
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(stage.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        Text(stage.timeLabel, fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = DrgRedPanic, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(stage.locationLabel, fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold)
            }
            HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))
            Surface(shape = RoundedCornerShape(12.dp), color = DrgRedBgSoft, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = DrgRedPanic, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Kondisi Nyata Lapangan (Off-App):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgRedPanic)
                        Text(stage.realChallenge, fontSize = 11.sp, color = DrgRedPanicDarker, lineHeight = 15.sp)
                    }
                }
            }
            Surface(shape = RoundedCornerShape(12.dp), color = theme.pillBg, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Solusi & Proteksi Aplikasi DRG:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                        Text(stage.drgSolution, fontSize = 11.sp, color = DrgTextDark, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}
