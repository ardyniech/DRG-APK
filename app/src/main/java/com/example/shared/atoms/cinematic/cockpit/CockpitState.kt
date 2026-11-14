package com.example.shared.atoms.cinematic.cockpit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Stable
class CockpitState(
    private val scope: CoroutineScope
) {
    var speedKmh by mutableFloatStateOf(52.5f)
        internal set
    var rpm by mutableIntStateOf(5800)
        internal set
    var steerAngle by mutableFloatStateOf(0f)
        private set
    var panX by mutableFloatStateOf(0f)
        private set
    var panY by mutableFloatStateOf(0f)
        private set
    var zoom by mutableFloatStateOf(1.0f)
        private set

    private val animSteer = Animatable(0f)
    private val animZoom = Animatable(1.0f)

    fun panBy(dx: Float, dy: Float) {
        panX = (panX + dx * 0.45f).coerceIn(-120f, 120f)
        panY = (panY + dy * 0.25f).coerceIn(-60f, 60f)
        steerAngle = clampSteerAngle(panX / 120f)
    }

    fun zoomBy(delta: Float) {
        zoom = clampCockpitZoom(zoom + delta)
    }

    fun resetView() {
        scope.launch {
            panX = 0f
            panY = 0f
            animSteer.animateTo(0f, tween(300)) { steerAngle = value }
            animZoom.animateTo(1.0f, tween(300)) { zoom = value }
        }
    }
}

@Composable
fun rememberCockpitState(): CockpitState {
    val scope = rememberCoroutineScope()
    val state = remember { CockpitState(scope) }

    LaunchedEffect(Unit) {
        var elapsed = 0f
        while (isActive) {
            delay(32) // ~30 fps state updates for organic speed
            elapsed += 0.032f
            val currentSpeed = calculateOrganicSpeed(elapsed)
            state.speedKmh = currentSpeed
            state.rpm = calculateClusterRpm(currentSpeed)
        }
    }

    return state
}
