package com.example.modules.auth

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.cinematic.ojol360.CinematicOjol360State
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun LandingCinematicControlsPill(
    state: CinematicOjol360State,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DrgGlassSurfaceStrong)
            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PresetChip("Depan", active = state.angle in 135f..225f) { state.animateToAngle(180f) }
        PresetChip("Samping", active = state.angle in 45f..135f || state.angle in 225f..315f) { state.animateToAngle(90f) }
        PresetChip("Belakang", active = state.angle >= 315f || state.angle < 45f) { state.animateToAngle(0f) }

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
private fun PresetChip(label: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) DrgGrabGreenPrimary else Color.White.copy(alpha = 0.8f))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (active) Color.White else DrgTextDark
        )
    }
}
