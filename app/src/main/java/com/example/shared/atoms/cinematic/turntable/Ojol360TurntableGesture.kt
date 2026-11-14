package com.example.shared.atoms.cinematic.turntable

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.ojolTurntableGestures(
    state: Ojol360TurntableState,
    onTap: () -> Unit = {}
): Modifier = this
    .pointerInput(state) {
        detectTransformGestures { _, pan, zoomChange, _ ->
            if (pan.x != 0f) {
                state.setAutoSpinActive(false)
                val sensitivity = 0.65f
                state.rotateBy(-pan.x * sensitivity)
            }
            if (zoomChange != 1.0f) {
                state.zoomBy((zoomChange - 1.0f) * 1.35f)
            }
        }
    }
    .pointerInput(state) {
        detectTapGestures(
            onDoubleTap = {
                state.animateToAngle(0f)
                state.zoomBy(-1.0f)
            },
            onTap = { onTap() }
        )
    }
