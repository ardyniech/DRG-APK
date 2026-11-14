package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class CyberOjolCameraState(
    initialYaw: Float = 180f,
    initialPitch: Float = 6f,
    initialZoom: Float = 1.0f,
    private val scope: CoroutineScope
) {
    var yaw by mutableFloatStateOf(normalizeAngle(initialYaw))
        private set
    var pitch by mutableFloatStateOf(clampPitch(initialPitch))
        private set
    var zoom by mutableFloatStateOf(clampCamZoom(initialZoom))
        private set
    var isAutoOrbiting by mutableStateOf(true)
        private set

    private val animYaw = Animatable(yaw)
    private val animPitch = Animatable(pitch)

    fun orbitBy(deltaYaw: Float, deltaPitch: Float) {
        yaw = normalizeAngle(yaw + deltaYaw)
        pitch = clampPitch(pitch + deltaPitch)
    }

    fun zoomBy(delta: Float) {
        zoom = clampCamZoom(zoom + delta)
    }

    fun applyPreset(preset: CameraPreset) {
        scope.launch {
            animYaw.animateTo(preset.yaw, tween(400)) { yaw = normalizeAngle(value) }
        }
        scope.launch {
            animPitch.animateTo(preset.pitch, tween(400)) { pitch = clampPitch(value) }
        }
    }

    fun toggleAutoOrbit() {
        isAutoOrbiting = !isAutoOrbiting
    }

    fun setAutoOrbit(active: Boolean) {
        isAutoOrbiting = active
    }

    fun asCameraTransform(): CameraTransform = CameraTransform(yaw, pitch, zoom)
}

@Composable
fun rememberCyberCameraState(): CyberOjolCameraState {
    val scope = rememberCoroutineScope()
    return remember { CyberOjolCameraState(scope = scope) }
}

fun Modifier.cyberCameraGestures(
    state: CyberOjolCameraState,
    onTap: () -> Unit = {}
): Modifier = this
    .pointerInput(state) {
        detectTransformGestures { _, pan, zoomChange, _ ->
            if (pan.x != 0f || pan.y != 0f) {
                state.setAutoOrbit(false)
                state.orbitBy(-pan.x * 0.45f, -pan.y * 0.25f)
            }
            if (zoomChange != 1.0f) {
                state.zoomBy((zoomChange - 1.0f) * 1.35f)
            }
        }
    }
    .pointerInput(state) {
        detectTapGestures(
            onDoubleTap = { state.applyPreset(CameraPreset.CHASE_REAR) },
            onTap = { onTap() }
        )
    }
