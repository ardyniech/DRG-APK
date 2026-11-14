package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TwoWheeler
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
import com.example.shared.atoms.cinematic.ojol360.CinematicOjol360State
import com.example.shared.atoms.cinematic.ojol360.quadrantLabel
import com.example.ui.theme.DrgGlassSurfaceBright
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSlate

@Composable
fun LandingCinematicTopBar(
    state: CinematicOjol360State,
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
        // App Brand & Online Pulse
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(DrgGlassSurfaceStrong)
                .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(20.dp))
            Column {
                Text("DRG DRIVER", fontSize = 13.sp, fontWeight = FontWeight.Black, color = DrgTextDark)
                Text("🟢 1,248 Online • Malang", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgGrabGreenPrimary)
            }
        }

        // Live 360 & Zoom Indicator + Controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(DrgGlassSurfaceBright)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("landing_topbar_angle")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Explore, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(14.dp))
                    Text("${state.angle.toInt()}°", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                }
            }

            IconButton(
                onClick = { state.toggleAutoRotate() },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (state.isAutoRotating) DrgGrabGreenPrimary else DrgGlassSurfaceBright)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Sync,
                    contentDescription = "Auto Spin",
                    tint = if (state.isAutoRotating) Color.White else DrgTextDark,
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
                    .testTag("btn_topbar_about")
            ) {
                Icon(Icons.Default.Info, contentDescription = "Tentang", tint = DrgTextDark, modifier = Modifier.size(18.dp))
            }
        }
    }
}
