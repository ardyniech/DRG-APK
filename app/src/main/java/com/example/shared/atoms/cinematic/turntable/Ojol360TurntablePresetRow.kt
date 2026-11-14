package com.example.shared.atoms.cinematic.turntable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun Ojol360TurntablePresetRow(
    state: Ojol360TurntableState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(DrgGlassSurfaceStrong)
            .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(18.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TurntablePresetChip("DEPAN", target = 0f, current = state.angle) { state.animateToAngle(0f) }
        TurntablePresetChip("KIRI", target = 90f, current = state.angle) { state.animateToAngle(90f) }
        TurntablePresetChip("BELAKANG", target = 180f, current = state.angle) { state.animateToAngle(180f) }
        TurntablePresetChip("KANAN", target = 270f, current = state.angle) { state.animateToAngle(270f) }

        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = { state.zoomBy(-0.25f) },
            modifier = Modifier.size(28.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.8f))
        ) {
            Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = DrgTextDark, modifier = Modifier.size(16.dp))
        }

        IconButton(
            onClick = { state.zoomBy(0.25f) },
            modifier = Modifier.size(28.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.8f))
        ) {
            Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = DrgTextDark, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun TurntablePresetChip(
    label: String,
    target: Float,
    current: Float,
    onClick: () -> Unit
) {
    val active = kotlin.math.abs(normalizeTurntableAngle(current) - target) < 22.5f
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) DrgGrabGreenPrimary else Color.White.copy(alpha = 0.75f))
            .clickable(onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = if (active) Color.White else DrgTextDark
        )
    }
}
