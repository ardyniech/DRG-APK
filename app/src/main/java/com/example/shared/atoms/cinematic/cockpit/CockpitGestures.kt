package com.example.shared.atoms.cinematic.cockpit

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.cockpitGestures(
    state: CockpitState,
    onTap: () -> Unit = {}
): Modifier = this
    .pointerInput(state) {
        detectTransformGestures { _, pan, zoomChange, _ ->
            if (pan.x != 0f || pan.y != 0f) {
                state.panBy(pan.x, pan.y)
            }
            if (zoomChange != 1.0f) {
                state.zoomBy((zoomChange - 1.0f) * 1.35f)
            }
        }
    }
    .pointerInput(state) {
        detectTapGestures(
            onDoubleTap = { state.resetView() },
            onTap = { onTap() }
        )
    }
