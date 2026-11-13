package com.example.shared.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import com.example.shared.models.AppVisualTheme
import kotlin.math.cos
import kotlin.math.sin

/**
 * Shared Full-Bleed Animated Luminous Atmosphere Canvas across Dashboard, Profile, and Settings.
 */
@Composable
fun DrgLuminousAtmosphere(
    theme: AppVisualTheme,
    modifier: Modifier = Modifier
) {
    val skyTop by animateColorAsState(theme.skyTop, tween(650), label = "atmSkyTop")
    val skyMid by animateColorAsState(theme.skyMid, tween(650), label = "atmSkyMid")
    val skyBottom by animateColorAsState(theme.skyBottom, tween(650), label = "atmSkyBottom")
    val orbPrimary by animateColorAsState(theme.orbPrimary, tween(650), label = "atmOrb1")
    val orbSecondary by animateColorAsState(theme.orbSecondary, tween(650), label = "atmOrb2")

    val infiniteTransition = rememberInfiniteTransition(label = "luminousAtmosphere")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (Math.PI * 2.0).toFloat(),
        animationSpec = infiniteRepeatable(tween(22000, easing = LinearEasing), RepeatMode.Restart),
        label = "atmPhase"
    )
    val drift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "atmDrift"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        drawRect(brush = Brush.verticalGradient(listOf(skyTop, skyMid, skyBottom)))

        val o1Center = Offset(x = w * (0.20f + 0.14f * cos(phase)), y = h * (0.14f + 0.05f * sin(phase)))
        val o1Radius = w * 0.70f
        drawCircle(
            brush = Brush.radialGradient(
                listOf(orbPrimary.copy(alpha = 0.30f), orbPrimary.copy(alpha = 0.08f), orbPrimary.copy(alpha = 0f)),
                center = o1Center, radius = o1Radius
            ),
            radius = o1Radius, center = o1Center
        )

        val o2Center = Offset(x = w * (0.85f - 0.14f * sin(phase)), y = h * (0.34f + 0.08f * drift))
        val o2Radius = w * 0.62f
        drawCircle(
            brush = Brush.radialGradient(
                listOf(orbSecondary.copy(alpha = 0.24f), orbSecondary.copy(alpha = 0.06f), orbSecondary.copy(alpha = 0f)),
                center = o2Center, radius = o2Radius
            ),
            radius = o2Radius, center = o2Center
        )

        val waveY = h * (0.28f + 0.04f * drift)
        val wavePath = Path().apply {
            moveTo(0f, waveY)
            cubicTo(w * 0.3f, waveY - h * 0.08f, w * 0.7f, waveY + h * 0.09f, w, waveY - h * 0.03f)
            lineTo(w, waveY + h * 0.18f)
            lineTo(0f, waveY + h * 0.14f)
            close()
        }
        drawPath(
            path = wavePath,
            brush = Brush.horizontalGradient(
                listOf(orbPrimary.copy(alpha = 0.10f), orbSecondary.copy(alpha = 0.12f), orbPrimary.copy(alpha = 0.06f))
            )
        )
    }
}
