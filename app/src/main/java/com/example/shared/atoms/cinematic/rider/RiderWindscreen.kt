package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

import com.example.ui.theme.DrgWindscreenTintBot
import com.example.ui.theme.DrgWindscreenTintMid
import com.example.ui.theme.DrgWindscreenTintTop

internal fun DrawScope.drawWindscreen(topY: Float, bottomY: Float, steer: Float) {
    val centerX = size.width / 2f
    val halfW = size.width * 0.38f
    val tipY = topY

    val shieldPath = Path().apply {
        moveTo(centerX - halfW, bottomY)
        cubicTo(
            centerX - halfW * 0.85f, (topY + bottomY) * 0.52f,
            centerX - halfW * 0.40f, tipY,
            centerX, tipY
        )
        cubicTo(
            centerX + halfW * 0.40f, tipY,
            centerX + halfW * 0.85f, (topY + bottomY) * 0.52f,
            centerX + halfW, bottomY
        )
        close()
    }

    // Tinted windscreen body
    drawPath(
        path = shieldPath,
        brush = Brush.verticalGradient(
            0f to DrgWindscreenTintTop,
            0.65f to DrgWindscreenTintMid,
            1f to DrgWindscreenTintBot
        )
    )

    // Aerodynamic curved edge specular bevel highlight
    drawPath(
        path = shieldPath,
        brush = Brush.horizontalGradient(
            0f to Color.White.copy(alpha = 0.08f),
            0.45f to Color.White.copy(alpha = 0.32f),
            1f to Color.White.copy(alpha = 0.08f)
        ),
        style = Stroke(width = size.width * 0.007f)
    )

    // Glass reflection streak
    val streakPath = Path().apply {
        moveTo(centerX - halfW * 0.28f, tipY + (bottomY - tipY) * 0.12f)
        lineTo(centerX - halfW * 0.12f, tipY + (bottomY - tipY) * 0.12f)
        lineTo(centerX - halfW * 0.55f, bottomY)
        lineTo(centerX - halfW * 0.72f, bottomY)
        close()
    }
    drawPath(
        path = streakPath,
        brush = Brush.linearGradient(
            0f to Color.White.copy(alpha = 0.16f),
            1f to Color.Transparent
        )
    )

    // Subtle rain beading on screen
    repeat(8) { i ->
        val beadX = centerX - halfW * 0.6f + (i * halfW * 0.16f) + (steer * 8f)
        val beadY = tipY + (bottomY - tipY) * (0.22f + (i % 4) * 0.18f)
        drawCircle(
            color = Color.White.copy(alpha = 0.45f),
            radius = 1.8f + (i % 3) * 0.8f,
            center = Offset(beadX, beadY)
        )
    }
}
