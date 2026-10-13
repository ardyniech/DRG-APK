package com.example.modules.about.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgSurfaceVariant
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AboutProgressCard(summary: AboutSummary) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DrgSurface,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Progres Pengembangan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Dihitung otomatis dari kode nyata", fontSize = 10.sp, color = DrgTextMuted)
                }
                Text("${summary.overallProgress}%", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = DrgGreenPrimary)
            }
            LinearProgressIndicator(
                progress = { summary.overallProgress / 100f },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
                color = DrgGreenPrimary,
                trackColor = DrgSurfaceVariant
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
    Surface(shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.12f), modifier = modifier) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$count", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, fontSize = 10.sp, color = DrgTextSecondary)
        }
    }
}
