package com.example.modules.about.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutSummary
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.SpaceGroteskFontFamily

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
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text(value, fontFamily = SpaceGroteskFontFamily, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = theme.primaryColor)
            Text(label, fontSize = 11.sp, color = DrgTextMuted)
        }
    }
}

private fun formatNumber(value: Int): String = value.toString().reversed().chunked(3).joinToString(".").reversed()
