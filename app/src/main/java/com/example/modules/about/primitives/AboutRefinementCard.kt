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
import com.example.modules.about.AboutRefinement
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AboutRefinementCard(refinements: List<AboutRefinement>) {
    if (refinements.isEmpty()) return
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DrgSurface,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Rencana Penyempurnaan", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextPrimary)
            refinements.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(item.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                        Text(item.detail, fontSize = 10.sp, color = DrgTextSecondary)
                    }
                    RefinementStateBadge(state = item.state)
                }
            }
        }
    }
}

@Composable
private fun RefinementStateBadge(state: String) {
    val color = when (state) {
        "done" -> DrgGreenPrimary
        "in-progress" -> DrgAmberWarning
        else -> DrgTextMuted
    }
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.14f)) {
        Text(
            text = when (state) {
                "done" -> "Selesai"
                "in-progress" -> "Berjalan"
                else -> "Menunggu"
            },
            fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = color,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}