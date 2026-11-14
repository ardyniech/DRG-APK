package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun CinematicOjol360OverlayHud(
    state: CinematicOjol360State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top HUD Bar: Angle Badge + Zoom Indicator + Fullscreen Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DrgGlassSurfaceStrong)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("ojol_angle_badge")
            ) {
                Icon(Icons.Default.Explore, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(16.dp))
                Text(
                    text = "${state.angle.toInt()}° • ${quadrantLabel(state.angle)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextDark
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(DrgGlassSurfaceStrong)
                        .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "%.1fx".format(state.zoom),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgGrabGreenPrimary
                    )
                }

                IconButton(
                    onClick = { state.toggleFullscreen() },
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(DrgGlassSurfaceStrong)
                        .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                        .testTag("btn_toggle_fullscreen")
                ) {
                    Icon(
                        imageVector = if (state.isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen",
                        tint = DrgTextDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Bottom HUD Bar: Quick Angle Presets & Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                HudPresetChip("Depan", active = state.angle in 135f..225f) { state.animateToAngle(180f) }
                HudPresetChip("Samping", active = state.angle in 45f..135f || state.angle in 225f..315f) { state.animateToAngle(90f) }
                HudPresetChip("Belakang", active = state.angle >= 315f || state.angle < 45f) { state.animateToAngle(0f) }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                HudIconButton(icon = Icons.Default.ZoomOut, onClick = { state.zoomBy(-0.25f) })
                HudIconButton(icon = Icons.Default.ZoomIn, onClick = { state.zoomBy(0.25f) })
                HudIconButton(
                    icon = Icons.Default.Sync,
                    onClick = { state.toggleAutoRotate() },
                    active = state.isAutoRotating
                )
            }
        }
    }
}
