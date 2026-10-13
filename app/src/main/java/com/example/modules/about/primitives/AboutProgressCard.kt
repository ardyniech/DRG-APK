package com.example.modules.about.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutSummary
import com.example.ui.theme.*

@Composable
fun AboutProgressCard(summary: AboutSummary) {
    val cardShape = RoundedCornerShape(26.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.25f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Progres Pengembangan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Dihitung otomatis dari kode nyata", fontSize = 10.sp, color = DrgTextMuted)
                }
                Text("${summary.overallProgress}%", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = DrgGreenPrimary)
            }
            LinearProgressIndicator(
                progress = { summary.overallProgress / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = DrgGreenPrimary,
                trackColor = DrgGreenContainer.copy(alpha = 0.5f)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CountPill("Selesai", summary.done, DrgGreenPrimary, Modifier.weight(1f))
                CountPill("Dibangun", summary.inProgress, DrgAmberWarning, Modifier.weight(1f))
                CountPill("Rencana", summary.planned, DrgTextMuted, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CountPill(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier.border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$count", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, fontSize = 10.sp, color = DrgTextSecondary)
        }
    }
}
