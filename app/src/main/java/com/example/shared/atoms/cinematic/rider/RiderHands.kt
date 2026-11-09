package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.DrgGloveMain
import com.example.ui.theme.DrgGloveShade
import com.example.ui.theme.DrgGrabGreenPrimary

internal fun DrawScope.drawRiderHands(steer: Float, barY: Float) {
    val centerX = size.width / 2f
    val spread = size.width * (0.34f + steer * 0.035f)
    val sweep = barY - size.height * 0.05f

    rotate(degrees = steer * 4.2f, pivot = Offset(centerX, barY)) {
        drawGlove(centerX - spread, barY - sweep * 0.22f, -1f)
        drawGlove(centerX + spread, barY - sweep * 0.22f, 1f)
    }
}

private fun DrawScope.drawGlove(centerX: Float, centerY: Float, side: Float) {
    val palmW = size.width * 0.138f
    val palmH = size.height * 0.088f
    val topLeft = Offset(centerX - palmW / 2f, centerY - palmH / 2f)

    // Main glove leather body
    drawRoundRect(
        brush = Brush.verticalGradient(
            0f to DrgGloveShade.copy(alpha = 0.98f),
            1f to DrgGloveMain.copy(alpha = 0.96f)
        ),
        topLeft = topLeft,
        size = Size(palmW, palmH),
        cornerRadius = CornerRadius(palmH * 0.36f)
    )

    // Fingers gripping around handlebar
    repeat(4) { finger ->
        val fingerY = topLeft.y + palmH * (0.18f + finger * 0.20f)
        val fingerW = palmW * (0.64f - finger * 0.04f)
        drawRoundRect(
            color = DrgGloveMain.copy(alpha = 0.98f),
            topLeft = Offset(centerX - fingerW / 2f + side * palmW * 0.08f, fingerY),
            size = Size(fingerW, palmH * 0.125f),
            cornerRadius = CornerRadius(palmH * 0.06f)
        )
    }

    // Carbon-fiber knuckle protector plate
    val plateW = palmW * 0.58f
    val plateH = palmH * 0.26f
    drawRoundRect(
        color = Color(0xFF15191E),
        topLeft = Offset(topLeft.x + palmW * 0.08f, topLeft.y + palmH * 0.08f),
        size = Size(plateW, plateH),
        cornerRadius = CornerRadius(plateH * 0.30f)
    )

    // High-visibility DRG green reflective safety accent line
    drawRoundRect(
        color = DrgGrabGreenPrimary.copy(alpha = 0.90f),
        topLeft = Offset(topLeft.x + palmW * 0.08f, topLeft.y + palmH * 0.36f),
        size = Size(plateW * 0.90f, 3.5f),
        cornerRadius = CornerRadius(1.5f)
    )

    // Thumb wrap
    drawRoundRect(
        color = DrgGloveShade.copy(alpha = 0.90f),
        topLeft = Offset(centerX + side * palmW * 0.32f, centerY - palmH * 0.05f),
        size = Size(palmW * 0.28f, palmH * 0.32f),
        cornerRadius = CornerRadius(palmH * 0.12f)
    )
}