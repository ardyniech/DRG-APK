package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.DrgGripRubber
import com.example.ui.theme.DrgHandlebarMetal
import com.example.ui.theme.DrgMirrorGlass

internal fun DrawScope.drawHandlebar(steer: Float, barY: Float) {
    val centerX = size.width / 2f
    val sweep = barY - size.height * 0.05f
    val gripSpread = size.width * (0.34f + steer * 0.035f)

    rotate(degrees = steer * 3.2f, pivot = Offset(centerX, barY)) {
        drawBarTube(centerX, barY, sweep, gripSpread)
        drawGrip(centerX - gripSpread, barY, -1f)
        drawGrip(centerX + gripSpread, barY, 1f)
        drawPhoneMount(centerX, barY - sweep * 0.42f)
    }

    drawMirrorStalk(centerX - gripSpread, barY, sweep, -1f, steer)
    drawMirrorStalk(centerX + gripSpread, barY, sweep, 1f, steer)
}

private fun DrawScope.drawBarTube(centerX: Float, barY: Float, sweep: Float, spread: Float) {
    val tube = Path().apply {
        moveTo(centerX - spread, barY - sweep * 0.30f)
        quadraticTo(centerX - spread * 0.42f, barY - sweep * 0.66f, centerX, barY - sweep * 0.72f)
        quadraticTo(centerX + spread * 0.42f, barY - sweep * 0.66f, centerX + spread, barY - sweep * 0.30f)
    }
    drawPath(
        path = tube,
        brush = Brush.verticalGradient(
            0f to DrgHandlebarMetal.copy(alpha = 0.95f),
            1f to DrgHandlebarMetal.copy(alpha = 0.55f)
        ),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = size.width * 0.026f)
    )
    drawPath(
        path = tube,
        color = Color.White.copy(alpha = 0.22f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = size.width * 0.006f)
    )
}

private fun DrawScope.drawGrip(x: Float, barY: Float, side: Float) {
    val width = size.width * 0.115f
    val height = size.height * 0.062f
    val topLeft = Offset(x - width / 2f, barY - height * 0.72f)
    drawRoundRect(
        brush = Brush.verticalGradient(
            0f to DrgGripRubber.copy(alpha = 0.98f),
            1f to DrgGripRubber.copy(alpha = 0.72f)
        ),
        topLeft = topLeft,
        size = Size(width, height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(height * 0.42f)
    )
    repeat(4) { rib ->
        val ribX = topLeft.x + width * (0.18f + rib * 0.21f)
        drawLine(
            color = Color.Black.copy(alpha = 0.32f),
            start = Offset(ribX, topLeft.y + height * 0.22f),
            end = Offset(ribX, topLeft.y + height * 0.78f),
            strokeWidth = size.width * 0.005f
        )
    }
    drawCircle(
        color = DrgHandlebarMetal.copy(alpha = 0.9f),
        radius = size.width * 0.019f,
        center = Offset(x + side * width * 0.5f, barY - height * 0.34f)
    )
}

private fun DrawScope.drawPhoneMount(centerX: Float, centerY: Float) {
    val width = size.width * 0.20f
    val height = width * 0.52f
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.88f),
        topLeft = Offset(centerX - width / 2f, centerY - height / 2f),
        size = Size(width, height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(width * 0.06f)
    )
    drawRoundRect(
        color = DrgMirrorGlass.copy(alpha = 0.10f),
        topLeft = Offset(centerX - width / 2f, centerY - height / 2f),
        size = Size(width, height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(width * 0.06f)
    )
}
