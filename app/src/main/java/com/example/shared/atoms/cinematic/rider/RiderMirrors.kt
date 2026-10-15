package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import com.example.ui.theme.DrgHandlebarMetal
import com.example.ui.theme.DrgMirrorGlass

internal fun DrawScope.drawMirrorStalk(
    gripX: Float,
    barY: Float,
    sweep: Float,
    side: Float,
    steer: Float
) {
    val headY = barY - sweep * 1.85f
    val headX = gripX + side * size.width * (0.07f - steer * 0.02f)
    drawLine(
        color = DrgHandlebarMetal.copy(alpha = 0.82f),
        start = Offset(gripX, barY - sweep * 0.62f),
        end = Offset(headX, headY + size.height * 0.030f),
        strokeWidth = size.width * 0.014f
    )
    val mirrorW = size.width * 0.155f
    val mirrorH = size.height * 0.062f
    val topLeft = Offset(headX - mirrorW / 2f, headY - mirrorH / 2f)
    drawRoundRect(
        color = DrgHandlebarMetal.copy(alpha = 0.95f),
        topLeft = topLeft,
        size = Size(mirrorW, mirrorH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(mirrorH * 0.28f)
    )
    drawRoundRect(
        brush = Brush.horizontalGradient(
            0f to DrgMirrorGlass.copy(alpha = 0.30f),
            0.5f to DrgMirrorGlass.copy(alpha = 0.10f),
            1f to DrgMirrorGlass.copy(alpha = 0.26f)
        ),
        topLeft = Offset(topLeft.x + mirrorW * 0.06f, topLeft.y + mirrorH * 0.14f),
        size = Size(mirrorW * 0.88f, mirrorH * 0.72f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(mirrorH * 0.22f)
    )
}