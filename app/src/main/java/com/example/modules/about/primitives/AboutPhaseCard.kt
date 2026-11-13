package com.example.modules.about.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.example.modules.about.AboutFeature
import com.example.modules.about.AboutPhase
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun AboutPhaseCard(
    phase: AboutPhase,
    features: List<AboutFeature>,
    plannedFeatures: List<AboutFeature>
) {
    val theme = LocalAppVisualTheme.current
    val phaseColor = phaseStateColor(phase.state, theme.primaryColor)
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(phase.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Target ${phase.target} · ${phase.doneCount}/${phase.featureCount} fitur", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextMuted)
                }
                Text("${phase.progress}%", fontFamily = SpaceGroteskFontFamily, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = phaseColor)
            }
            LinearProgressIndicator(
                progress = { phase.progress / 100f },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
                color = phaseColor,
                trackColor = theme.pillBg
            )
            Text(phase.summary, fontSize = 10.5.sp, color = DrgTextSecondary)
            HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.4f))
            features.forEach { AboutFeatureRow(it) }
            plannedFeatures.forEach { AboutFeatureRow(it) }
        }
    }
}

internal fun phaseStateColor(state: String, primary: Color = DrgGreenPrimary): Color = when (state) {
    "shipped" -> primary
    "active" -> DrgAmberWarning
    else -> DrgTextMuted
}
