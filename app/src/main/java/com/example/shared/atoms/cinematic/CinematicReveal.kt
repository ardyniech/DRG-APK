package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CinematicReveal(
    index: Int = 0,
    modifier: Modifier = Modifier,
    riseDistance: Dp = 26.dp,
    staggerMillis: Long = 70L,
    content: @Composable () -> Unit
) {
    val reveal = remember { Animatable(0f) }
    LaunchedEffect(index) {
        delay(index * staggerMillis)
        reveal.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Box(
        modifier = modifier.graphicsLayer {
            alpha = reveal.value.coerceIn(0f, 1f)
            translationY = riseDistance.toPx() * (1f - reveal.value)
        },
        contentAlignment = Alignment.TopStart
    ) {
        content()
    }
}