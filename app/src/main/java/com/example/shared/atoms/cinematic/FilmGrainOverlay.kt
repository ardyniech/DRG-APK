package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun FilmGrainOverlay(
    modifier: Modifier = Modifier,
    speckCount: Int = 90,
    grainSpeed: Float = 1f,
    vignetteStrength: Float = 0.55f
) {
    val transition = rememberInfiniteTransition(label = "film-grain")
    val jitter by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Restart),
        label = "grain-jitter"
    )
    val specks = rememberGrain(count = speckCount)

    Canvas(modifier = modifier) {
        drawVignette(vignetteStrength)
        drawGrain(specks, jitter, grainSpeed)
    }
}

private fun DrawScope.drawVignette(strength: Float) {
    drawRect(
        brush = Brush.radialGradient(
            0.55f to Color.Transparent,
            1f to Color.Black.copy(alpha = strength),
            center = Offset(size.width * 0.5f, size.height * 0.45f),
            radius = size.maxDimension * 0.78f
        ),
        size = Size(size.width, size.height)
    )
}

private fun DrawScope.drawGrain(specks: List<GrainSpeck>, jitter: Float, grainSpeed: Float) {
    specks.forEach { speck ->
        val jitterX = (jitter * grainSpeed * (0.4f + speck.size * 0.2f)) % 1f
        val jitterY = (jitter * grainSpeed * (0.6f + speck.size * 0.15f)) % 1f
        val x = wrapUnit(speck.x + jitterX) * size.width
        val y = wrapUnit(speck.y + jitterY) * size.height
        val dotSize = speck.size
        drawRect(
            color = if (speck.alpha > 0.11f) Color.Black.copy(alpha = speck.alpha)
            else Color.White.copy(alpha = speck.alpha),
            topLeft = Offset(x, y),
            size = Size(dotSize, dotSize)
        )
    }
}