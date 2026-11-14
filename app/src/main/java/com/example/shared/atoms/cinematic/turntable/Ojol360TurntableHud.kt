package com.example.shared.atoms.cinematic.turntable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceBright
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun Ojol360TurntableTopBar(
    state: Ojol360TurntableState,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(DrgGlassSurfaceStrong)
                .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("turntable_telemetry_hud")
        ) {
            Text(
                text = "DRG 360° LIVE CINEMATIC",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = DrgGrabGreenPrimary
            )
            Text(
                text = "${state.angle.toInt()}° • ${turntableQuadrantLabel(state.angle)} | %.1fx".format(state.zoom),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                color = DrgTextDark
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = { state.toggleAutoSpin() },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (state.isAutoSpinning) DrgGrabGreenPrimary else DrgGlassSurfaceBright)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Auto Spin",
                    tint = if (state.isAutoSpinning) Color.White else DrgTextDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onAboutClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DrgGlassSurfaceBright)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = DrgTextDark, modifier = Modifier.size(18.dp))
            }
        }
    }
}
