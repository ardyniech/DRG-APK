package com.example.shared.atoms.cinematic.turntable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class Ojol360TurntableState(
    initialAngle: Float = 0f,
    initialZoom: Float = DEFAULT_TURNTABLE_ZOOM,
    private val scope: CoroutineScope
) {
    var angle by mutableFloatStateOf(normalizeTurntableAngle(initialAngle))
        private set
    var zoom by mutableFloatStateOf(clampTurntableZoom(initialZoom))
        private set
    var isAutoSpinning by mutableStateOf(true)
        private set

    val frameIndex: Int
        get() = angleToFrameIndex(angle)

    private val animAngle = Animatable(angle)

    fun rotateBy(deltaDegrees: Float) {
        angle = normalizeTurntableAngle(angle + deltaDegrees)
    }

    fun zoomBy(delta: Float) {
        zoom = clampTurntableZoom(zoom + delta)
    }

    fun animateToAngle(targetAngle: Float) {
        scope.launch {
            val normalizedTarget = normalizeTurntableAngle(targetAngle)
            animAngle.animateTo(normalizedTarget, tween(400)) {
                angle = normalizeTurntableAngle(value)
            }
        }
    }

    fun toggleAutoSpin() {
        isAutoSpinning = !isAutoSpinning
    }

    fun setAutoSpinActive(active: Boolean) {
        isAutoSpinning = active
    }
}

@Composable
fun rememberOjol360TurntableState(): Ojol360TurntableState {
    val scope = rememberCoroutineScope()
    return remember { Ojol360TurntableState(scope = scope) }
}
