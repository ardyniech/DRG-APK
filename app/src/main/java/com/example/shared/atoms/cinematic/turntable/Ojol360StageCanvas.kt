package com.example.shared.atoms.cinematic.turntable

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

fun DrawScope.drawOjol360TurntableScene(
    context: Context,
    state: Ojol360TurntableState,
    bounceY: Float
) {
    // 1. Draw High-Resolution Malang Highway Background
    val bgBmp = Ojol360AssetLoader.loadBackground(context)
    if (bgBmp != null) {
        drawImage(
            image = bgBmp,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(bgBmp.width, bgBmp.height),
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }

    // 2. Center positioning for motorcycle
    val centerX = size.width * 0.5f
    val centerY = size.height * 0.64f + bounceY

    scale(scale = state.zoom, pivot = Offset(centerX, centerY)) {
        // Ground Contact Shadow on Asphalt
        val shadowW = size.width * 0.62f
        val shadowH = size.height * 0.045f
        drawOval(
            color = Color.Black.copy(alpha = 0.55f),
            topLeft = Offset(centerX - shadowW * 0.5f, centerY + size.height * 0.16f),
            size = Size(shadowW, shadowH)
        )

        // 3. Draw Photorealistic Transparent Ojol Turntable Frame
        val frameBmp = Ojol360AssetLoader.loadFrame(context, state.frameIndex)
        if (frameBmp != null) {
            val targetW = (size.width * 0.72f).toInt()
            val targetH = (targetW * (frameBmp.height.toFloat() / frameBmp.width.toFloat())).toInt()
            val left = (centerX - targetW * 0.5f).toInt()
            val top = (centerY - targetH * 0.5f + size.height * 0.05f).toInt()

            drawImage(
                image = frameBmp,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(frameBmp.width, frameBmp.height),
                dstOffset = IntOffset(left, top),
                dstSize = IntSize(targetW, targetH)
            )
        }
    }
}
