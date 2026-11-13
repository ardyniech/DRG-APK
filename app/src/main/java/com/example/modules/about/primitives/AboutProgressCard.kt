package com.example.modules.about.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutSummary
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun AboutProgressCard(summary: AboutSummary) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(26.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 18.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Progres Pengembangan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Dihitung otomatis dari kode nyata", fontSize = 10.sp, color = DrgTextMuted)
                }
                Text("${summary.overallProgress}%", fontFamily = SpaceGroteskFontFamily, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = theme.primaryColor)
            }
            LinearProgressIndicator(
                progress = { summary.overallProgress / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = theme.primaryColor,
                trackColor = theme.pillBg
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CountPill("Selesai", summary.done, theme.primaryColor, Modifier.weight(1f))
                CountPill("Dibangun", summary.inProgress, DrgAmberWarning, Modifier.weight(1f))
                CountPill("Rencana", summary.planned, DrgTextMuted, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CountPill(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(14.dp),
        backgroundColor = color.copy(alpha = 0.12f),
        borderHighlightColor = color.copy(alpha = 0.45f),
        contentPadding = 8.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$count", fontFamily = SpaceGroteskFontFamily, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, fontSize = 10.sp, color = DrgTextSecondary)
        }
    }
}
