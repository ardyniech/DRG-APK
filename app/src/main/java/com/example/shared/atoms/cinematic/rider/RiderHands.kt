package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.DrgGloveMain
import com.example.ui.theme.DrgGloveShade

internal fun DrawScope.drawRiderHands(steer: Float, barY: Float) {
    val centerX = size.width / 2f
    val spread = size.width * (0.34f + steer * 0.035f)
    val sweep = barY - size.height * 0.05f

    rotate(degrees = steer * 3.2f, pivot = Offset(centerX, barY)) {
        drawGlove(centerX - spread, barY - sweep * 0.22f, -1f)
        drawGlove(centerX + spread, barY - sweep * 0.22f, 1f)
    }
}

private fun DrawScope.drawGlove(centerX: Float, centerY: Float, side: Float) {
    val palmW = size.width * 0.132f
    val palmH = size.height * 0.086f
    val topLeft = Offset(centerX - palmW / 2f, centerY - palmH / 2f)

    drawRoundRect(
        brush = Brush.verticalGradient(
            0f to DrgGloveShade.copy(alpha = 0.98f),
            1f to DrgGloveMain.copy(alpha = 0.94f)
        ),
        topLeft = topLeft,
        size = Size(palmW, palmH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(palmH * 0.34f)
    )

    repeat(4) { finger ->
        val fingerY = topLeft.y + palmH * (0.20f + finger * 0.19f)
        val fingerW = palmW * (0.62f - finger * 0.05f)
        drawRoundRect(
            color = DrgGloveMain.copy(alpha = 0.95f),
            topLeft = Offset(centerX - fingerW / 2f + side * palmW * 0.10f, fingerY),
            size = Size(fingerW, palmH * 0.115f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(palmH * 0.06f)
        )
    }

    drawRoundRect(
        color = Color.White.copy(alpha = 0.10f),
        topLeft = Offset(topLeft.x + palmW * 0.10f, topLeft.y + palmH * 0.10f),
        size = Size(palmW * 0.52f, palmH * 0.20f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(palmH * 0.10f)
    )

    drawRoundRect(
        color = DrgGloveShade.copy(alpha = 0.85f),
        topLeft = Offset(centerX + side * palmW * 0.34f, centerY - palmH * 0.06f),
        size = Size(palmW * 0.26f, palmH * 0.30f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(palmH * 0.10f)
    )
}