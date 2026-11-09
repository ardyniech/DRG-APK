package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.shared.atoms.cinematic.rider.RiderSteeringState
import com.example.ui.theme.DrgSkylineFar
import com.example.ui.theme.DrgSkylineNear

@Composable
fun RoadRushBackdrop(
    modifier: Modifier = Modifier,
    horizonRatio: Float = 0.335f,
    showRain: Boolean = true,
    riderState: RiderSteeringState? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val transition = rememberInfiniteTransition(label = "road-rush")
    val rushSpeedMillis = if (riderState != null && riderState.speed > 5) {
        (2600 - (riderState.speed * 22)).coerceIn(700, 2600)
    } else 2200

    val rush by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(rushSpeedMillis, easing = LinearEasing), RepeatMode.Restart),
        label = "rush"
    )
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(46000, easing = LinearEasing), RepeatMode.Restart),
        label = "drift"
    )
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    val farSkyline = rememberSkyline(count = 30, minHeight = 0.45f, maxHeight = 1f)
    val nearSkyline = rememberSkyline(count = 18, minHeight = 0.3f, maxHeight = 0.62f)
    val rain = rememberRain(count = 32)
    val currentSteer = riderState?.steer ?: 0f
    val currentLean = riderState?.leanAngle ?: 0f

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val horizonY = size.height * horizonRatio
            val bottomY = size.height * 1.05f

            rotate(degrees = -currentLean * 0.38f, pivot = Offset(size.width * 0.5f, horizonY)) {
                drawNightSky(horizonY)
                drawSkylineLayer(farSkyline, drift * 0.35f, horizonY, DrgSkylineFar, 0.22f, windows = true)
                drawSkylineLayer(nearSkyline, drift, horizonY, DrgSkylineNear, 0.30f, windows = false)
                drawAsphalt(horizonY, bottomY, currentSteer)
                drawStreetLightPoles(rush * 0.85f, horizonY, bottomY, currentSteer)
                drawTrafficLights(rush, horizonY, bottomY, currentSteer)
                drawLaneDashes(rush, horizonY, bottomY, currentSteer)
                drawHeadlightCone(horizonY, bottomY, pulse)
                if (showRain) drawRain(rain, rush)
            }
        }
        content()
    }
}