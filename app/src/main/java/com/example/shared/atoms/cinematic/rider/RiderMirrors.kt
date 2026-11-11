package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgDialogDarkBg
import com.example.ui.theme.DrgHandlebarMetal
import com.example.ui.theme.DrgMirrorGlass
import com.example.ui.theme.DrgMirrorHousing
import com.example.ui.theme.DrgMirrorRoadLine
import com.example.ui.theme.DrgMirrorSkyBot
import com.example.ui.theme.DrgMirrorSkyMid
import com.example.ui.theme.DrgSlateBorder
import com.example.ui.theme.DrgTrafficRed

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
        color = DrgHandlebarMetal.copy(alpha = 0.88f),
        start = Offset(gripX, barY - sweep * 0.62f),
        end = Offset(headX, headY + size.height * 0.030f),
        strokeWidth = size.width * 0.014f
    )
    val mirrorW = size.width * 0.155f
    val mirrorH = size.height * 0.062f
    val topLeft = Offset(headX - mirrorW / 2f, headY - mirrorH / 2f)

    // Mirror housing
    drawRoundRect(
        color = DrgMirrorHousing,
        topLeft = topLeft,
        size = Size(mirrorW, mirrorH),
        cornerRadius = CornerRadius(mirrorH * 0.32f)
    )

    // Convex glass body showing dark rear night sky & asphalt
    val glassTopLeft = Offset(topLeft.x + mirrorW * 0.05f, topLeft.y + mirrorH * 0.08f)
    val glassW = mirrorW * 0.90f
    val glassH = mirrorH * 0.84f

    drawRoundRect(
        brush = Brush.verticalGradient(
            0f to DrgDialogDarkBg,
            0.5f to DrgMirrorSkyMid,
            1f to DrgMirrorSkyBot
        ),
        topLeft = glassTopLeft,
        size = Size(glassW, glassH),
        cornerRadius = CornerRadius(glassH * 0.28f)
    )

    // Receding road lines in mirror
    drawLine(
        color = DrgMirrorRoadLine,
        start = Offset(glassTopLeft.x + glassW * 0.35f, glassTopLeft.y + glassH),
        end = Offset(glassTopLeft.x + glassW * 0.48f, glassTopLeft.y + glassH * 0.45f),
        strokeWidth = 1.2f
    )

    // Distant traffic light reflections in mirror
    drawCircle(
        color = DrgSlateBorder.copy(alpha = 0.85f),
        radius = 2.2f,
        center = Offset(glassTopLeft.x + glassW * 0.52f, glassTopLeft.y + glassH * 0.48f)
    )
    drawCircle(
        color = DrgTrafficRed.copy(alpha = 0.70f),
        radius = 1.8f,
        center = Offset(glassTopLeft.x + glassW * 0.62f, glassTopLeft.y + glassH * 0.52f)
    )

    // Specular glass glare
    drawRoundRect(
        brush = Brush.linearGradient(
            0f to Color.White.copy(alpha = 0.25f),
            0.4f to Color.Transparent
        ),
        topLeft = glassTopLeft,
        size = Size(glassW * 0.55f, glassH),
        cornerRadius = CornerRadius(glassH * 0.28f)
    )
}