package com.example.modules.about.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutSummary
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary

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
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = modifier.border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = DrgGreenPrimary)
            Text(label, fontSize = 10.5.sp, color = DrgTextMuted)
        }
    }
}

private fun formatNumber(value: Int): String {
    val digits = value.toString()
    return digits.reversed().chunked(3).joinToString(".").reversed()
}
