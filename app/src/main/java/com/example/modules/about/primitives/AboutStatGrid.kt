package com.example.modules.about.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutSummary
import com.example.ui.theme.*

@Composable
fun AboutStatGrid(summary: AboutSummary) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            StatTile("Total Fitur", "${summary.totalFeatures}", Modifier.weight(1f))
            StatTile("Modul", "${summary.modules}", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            StatTile("Baris Kode", formatNumber(summary.totalLoc), Modifier.weight(1f))
            StatTile("Test Suite", "${summary.testSuites}", Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(18.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.2f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = modifier
            .shadow(3.dp, cardShape, ambientColor = Color(0x12003617), spotColor = Color(0x0A003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Column(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = DrgGreenPrimary)
            Text(label, fontSize = 11.sp, color = DrgTextMuted)
        }
    }
}

private fun formatNumber(value: Int): String {
    val digits = value.toString()
    return digits.reversed().chunked(3).joinToString(".").reversed()
}
