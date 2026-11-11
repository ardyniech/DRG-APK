package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSegmentOff
import com.example.ui.theme.DrgSegmentOn
import com.example.ui.theme.DrgTrafficOrange
import com.example.ui.theme.DrgTrafficRed

internal fun DrawScope.drawTachometerBar(
    topLeft: Offset,
    faceW: Float,
    faceH: Float,
    rpm: Int
) {
    val totalSegments = 18
    val activeSegments = ((rpm / 8500f) * totalSegments).toInt().coerceIn(1, totalSegments)
    val barW = (faceW * 0.84f) / totalSegments
    val barH = faceH * 0.08f
    val gap = barW * 0.22f
    val segW = barW - gap

    val originX = topLeft.x + faceW * 0.08f
    val originY = topLeft.y + faceH * 0.06f

    repeat(totalSegments) { i ->
        val x = originX + i * barW
        val isLit = i < activeSegments
        val isRedline = i >= (totalSegments - 3)
        val segColor = when {
            !isLit -> DrgSegmentOff
            isRedline -> DrgTrafficRed
            i >= (totalSegments - 6) -> DrgTrafficOrange
            else -> DrgSegmentOn
        }

        drawRoundRect(
            color = segColor,
            topLeft = Offset(x, originY),
            size = Size(segW, barH),
            cornerRadius = CornerRadius(barH * 0.35f)
        )

        if (isLit) {
            drawRoundRect(
                color = segColor.copy(alpha = 0.20f),
                topLeft = Offset(x - 1f, originY - 1f),
                size = Size(segW + 2f, barH + 2f),
                cornerRadius = CornerRadius(barH * 0.45f)
            )
        }
    }
}
