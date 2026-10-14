package com.example.shared.atoms

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.example.ui.theme.DrgGlassAuraGold
import com.example.ui.theme.DrgGlassAuraGreen
import com.example.ui.theme.DrgGlassAuraTeal
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DrgAtmosphericGlow(modifier: Modifier = Modifier, cinematic: Boolean = true) {
    val floatAnim = remember { Animatable(0f) }
    LaunchedEffect(cinematic) {
        if (cinematic) {
            floatAnim.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(14000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                if (cinematic) {
                    rotationZ = floatAnim.value * 2f
                    alpha = 0.85f + floatAnim.value * 0.12f
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val t = floatAnim.value

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraGreen.copy(alpha = 0.30f + t * 0.06f),
                    DrgGlassAuraTeal.copy(alpha = 0.14f + t * 0.04f),
                    Color.Transparent
                ),
                center = Offset(w * (0.12f + t * 0.03f), h * (0.08f + t * 0.02f)),
                radius = w * 0.68f
            ),
            radius = w * 0.68f,
            center = Offset(w * (0.12f + t * 0.03f), h * (0.08f + t * 0.02f))
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraGold.copy(alpha = 0.24f + t * 0.05f),
                    Color(0xFFFEF08A).copy(alpha = 0.10f + t * 0.03f),
                    Color.Transparent
                ),
                center = Offset(w * (0.88f - t * 0.04f), h * (0.22f + t * 0.05f)),
                radius = w * 0.58f
            ),
            radius = w * 0.58f,
            center = Offset(w * (0.88f - t * 0.04f), h * (0.22f + t * 0.05f))
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraGreen.copy(alpha = 0.18f + t * 0.04f),
                    Color.Transparent
                ),
                center = Offset(w * (0.38f - t * 0.02f), h * (0.62f + t * 0.03f)),
                radius = w * 0.64f
            ),
            radius = w * 0.64f,
            center = Offset(w * (0.38f - t * 0.02f), h * (0.62f + t * 0.03f))
        )

        val orbCenter = Offset(
            w * (0.72f + t * 0.05f),
            h * (0.48f + sin(t * 2 * PI) * 0.05f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraTeal.copy(alpha = 0.12f + t * 0.03f),
                    Color.Transparent
                ),
                center = orbCenter,
                radius = w * 0.22f
            ),
            radius = w * 0.22f,
            center = orbCenter
        )
    }
}