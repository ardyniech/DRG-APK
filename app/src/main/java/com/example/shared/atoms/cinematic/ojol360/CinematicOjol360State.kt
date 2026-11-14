package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class CinematicOjol360State(
    initialAngle: Float = 180f,
    initialZoom: Float = DEFAULT_ZOOM,
    private val scope: CoroutineScope
) {
    var angle by mutableFloatStateOf(normalizeAngle(initialAngle))
        private set
    var zoom by mutableFloatStateOf(clampZoom(initialZoom))
        private set
    var isFullscreen by mutableStateOf(false)
        private set
    var isAutoRotating by mutableStateOf(true)
        private set
    var speedKmh by mutableIntStateOf(58)
        private set

    private val animatableAngle = Animatable(angle)

    fun rotateBy(deltaDegrees: Float) {
        val newAngle = normalizeAngle(angle + deltaDegrees)
        angle = newAngle
        scope.launch { animatableAngle.snapTo(newAngle) }
    }

    fun animateToAngle(targetAngle: Float) {
        scope.launch {
            val normalizedTarget = normalizeAngle(targetAngle)
            animatableAngle.animateTo(
                targetValue = normalizedTarget,
                animationSpec = tween(durationMillis = 450, easing = LinearEasing)
            ) {
                angle = value
            }
        }
    }

    fun zoomBy(delta: Float) {
        zoom = clampZoom(zoom + delta)
    }

    fun setZoomLevel(level: Float) {
        zoom = clampZoom(level)
    }

    fun toggleFullscreen() {
        isFullscreen = !isFullscreen
    }

    fun setFullscreenMode(fullscreen: Boolean) {
        isFullscreen = fullscreen
    }

    fun toggleAutoRotate() {
        isAutoRotating = !isAutoRotating
    }

    fun setAutoRotateActive(active: Boolean) {
        isAutoRotating = active
    }

    fun updateSpeed(newSpeed: Int) {
        speedKmh = newSpeed.coerceIn(20, 110)
    }
}

@Composable
fun rememberCinematicOjol360State(
    initialAngle: Float = 180f,
    initialZoom: Float = DEFAULT_ZOOM
): CinematicOjol360State {
    val scope = rememberCoroutineScope()
    return remember {
        CinematicOjol360State(
            initialAngle = initialAngle,
            initialZoom = initialZoom,
            scope = scope
        )
    }
}
