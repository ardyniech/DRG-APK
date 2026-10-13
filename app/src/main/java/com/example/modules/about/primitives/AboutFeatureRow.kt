package com.example.modules.about.primitives

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutFeature
import com.example.modules.about.FeatureStatus
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AboutFeatureRow(feature: AboutFeature) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(feature.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary, modifier = Modifier.weight(1f))
            StatusBadge(status = feature.status, progress = feature.progress)
        }
        if (feature.description.isNotBlank()) {
            Text(feature.description, fontSize = 10.sp, color = DrgTextSecondary)
        }
    }
}

@Composable
private fun StatusBadge(status: String, progress: Int) {
    val color = statusColor(status)
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.14f)) {
        Text(
            text = "${FeatureStatus.label(status)} ${progress}%",
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

internal fun statusColor(status: String): Color = when (status) {
    FeatureStatus.DONE -> DrgGreenPrimary
    FeatureStatus.IN_PROGRESS -> DrgAmberWarning
    else -> DrgTextMuted
}
