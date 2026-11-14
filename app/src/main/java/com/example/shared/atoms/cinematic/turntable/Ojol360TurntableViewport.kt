package com.example.shared.atoms.cinematic.turntable

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun Ojol360TurntableViewport(
    state: Ojol360TurntableState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {}
) {
    val context = LocalContext.current
    val transition = rememberInfiniteTransition(label = "ojol360-heave")

    val bounce by transition.animateFloat(
        initialValue = -2.2f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 180, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Continuous smooth 360 rotation when auto-spin enabled
    LaunchedEffect(state.isAutoSpinning) {
        while (isActive && state.isAutoSpinning) {
            delay(16)
            state.rotateBy(0.45f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .ojolTurntableGestures(state, onTap = onTap)
            .testTag("ojol_360_turntable_viewport")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawOjol360TurntableScene(context, state, bounce)
        }
    }
}
