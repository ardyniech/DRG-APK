package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.cinematic.ojol360.CinematicOjol360State
import com.example.shared.atoms.cinematic.ojol360.CinematicOjol360Viewport
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun LandingHeroOjol360Card(
    state: CinematicOjol360State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
                .testTag("hero_ojol_360_card")
        ) {
            CinematicOjol360Viewport(
                state = state,
                modifier = Modifier.fillMaxSize(),
                showHud = true
            )
        }

        // Gesture Guidance Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(DrgGlassSurfaceStrong)
                .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = DrgGrabGreenPrimary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Geser 360° • Cubit zoom in/out • Ketuk 2x fullscreen",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = DrgTextDark
            )
        }
    }
}
