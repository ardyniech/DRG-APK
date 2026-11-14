package com.example.shared.atoms.cinematic.cyberojol

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
fun CyberOjol3DViewport(
    cameraState: CyberOjolCameraState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {}
) {
    val transition = rememberInfiniteTransition(label = "cyber3d-motion")

    val rush by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rush"
    )

    val bounce by transition.animateFloat(
        initialValue = -2.2f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Smooth auto-orbiting rotation when enabled
    LaunchedEffect(cameraState.isAutoOrbiting) {
        while (isActive && cameraState.isAutoOrbiting) {
            delay(16)
            cameraState.orbitBy(deltaYaw = 0.35f, deltaPitch = 0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .cyberCameraGestures(cameraState, onTap = onTap)
            .testTag("cyber_ojol_3d_viewport")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            renderCyberOjol3DScene(
                cam = cameraState.asCameraTransform(),
                rushProgress = rush,
                bounceY = bounce
            )
        }
    }
}
