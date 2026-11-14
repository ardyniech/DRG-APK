package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.cinematicOjolGesture(
    state: CinematicOjol360State,
    onTap: () -> Unit = {},
    onDoubleTap: () -> Unit = { state.toggleFullscreen() }
): Modifier = this
    .pointerInput(state) {
        detectTransformGestures { _, pan, zoomChange, _ ->
            if (pan.x != 0f) {
                state.setAutoRotateActive(false)
                val sensitivity = 0.55f
                state.rotateBy(-pan.x * sensitivity)
            }
            if (zoomChange != 1.0f) {
                state.zoomBy((zoomChange - 1.0f) * 1.25f)
            }
        }
    }
    .pointerInput(state) {
        detectTapGestures(
            onDoubleTap = { onDoubleTap() },
            onTap = {
                onTap()
            }
        )
    }
