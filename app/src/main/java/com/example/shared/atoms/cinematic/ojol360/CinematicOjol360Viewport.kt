package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun CinematicOjol360Viewport(
    state: CinematicOjol360State,
    modifier: Modifier = Modifier,
    showHud: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "ojol360-motion")

    val rush by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rush"
    )

    val bounce by transition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 220, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Smooth auto-orbit rotation when enabled
    LaunchedEffect(state.isAutoRotating) {
        while (isActive && state.isAutoRotating) {
            delay(16)
            state.rotateBy(0.40f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .cinematicOjolGesture(state)
            .testTag("cinematic_ojol_360_viewport")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCinematicRoadEnvironment(rushProgress = rush, angle = state.angle)
            drawCinematicOjol360(angle = state.angle, zoom = state.zoom, bounceY = bounce)
        }

        if (showHud) {
            CinematicOjol360OverlayHud(
                state = state,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
