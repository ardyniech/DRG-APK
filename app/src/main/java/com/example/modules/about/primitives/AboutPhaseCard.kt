package com.example.modules.about.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.example.modules.about.AboutFeature
import com.example.modules.about.AboutPhase
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgSurfaceVariant
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AboutPhaseCard(
    phase: AboutPhase,
    features: List<AboutFeature>,
    plannedFeatures: List<AboutFeature>
) {
    val phaseColor = phaseStateColor(phase.state)
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DrgSurface,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(phase.title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Target ${phase.target} · ${phase.doneCount}/${phase.featureCount} fitur", fontSize = 10.sp, color = DrgTextMuted)
                }
                Text("${phase.progress}%", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = phaseColor)
            }
            LinearProgressIndicator(
                progress = { phase.progress / 100f },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)),
                color = phaseColor,
                trackColor = DrgSurfaceVariant
            )
            Text(phase.summary, fontSize = 10.5.sp, color = DrgTextSecondary)
            HorizontalDivider(color = DrgOutline.copy(alpha = 0.4f))
            features.forEach { AboutFeatureRow(it) }
            plannedFeatures.forEach { AboutFeatureRow(it) }
        }
    }
}

internal fun phaseStateColor(state: String): Color = when (state) {
    "shipped" -> DrgGreenPrimary
    "active" -> DrgAmberWarning
    else -> DrgTextMuted
}
