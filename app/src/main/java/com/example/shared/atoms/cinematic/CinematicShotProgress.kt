package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgGrabGreenPrimary

@Composable
internal fun CinematicShotProgress(
    shotCount: Int,
    activeIndex: Int,
    running: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(shotCount) { index ->
            val active = running && (index == activeIndex)
            val fill by animateFloatAsState(
                targetValue = if (active) 1f else if (!running || index < activeIndex) 1f else 0f,
                animationSpec = tween(320),
                label = "shot-fill-$index"
            )
            Box(
                modifier = Modifier
                    .width(if (active) 42.dp else 22.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.22f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fill)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(50))
                        .background(if (active) DrgGrabGreenPrimary else Color.White.copy(alpha = 0.55f))
                )
            }
        }
    }
}
