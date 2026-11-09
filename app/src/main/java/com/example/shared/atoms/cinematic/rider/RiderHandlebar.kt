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

internal fun DrawScope.drawHandlebar(steer: Float, barY: Float, speed: Int = 45) {
    val centerX = size.width / 2f
    val sweep = barY - size.height * 0.05f
    val gripSpread = size.width * (0.34f + steer * 0.035f)

    rotate(degrees = steer * 4.2f, pivot = Offset(centerX, barY)) {
        drawBarTube(centerX, barY, sweep, gripSpread)
        drawGrip(centerX - gripSpread, barY, -1f)
        drawGrip(centerX + gripSpread, barY, 1f)
        drawBrakeLevers(centerX, barY, gripSpread)
        drawSmartphoneNav(centerX, barY - sweep * 0.40f, speed)
    }

    drawMirrorStalk(centerX - gripSpread, barY, sweep, -1f, steer)
    drawMirrorStalk(centerX + gripSpread, barY, sweep, 1f, steer)
}

private fun DrawScope.drawBarTube(centerX: Float, barY: Float, sweep: Float, spread: Float) {
    val tube = Path().apply {
        moveTo(centerX - spread, barY - sweep * 0.28f)
        quadraticTo(centerX - spread * 0.42f, barY - sweep * 0.62f, centerX, barY - sweep * 0.68f)
        quadraticTo(centerX + spread * 0.42f, barY - sweep * 0.62f, centerX + spread, barY - sweep * 0.28f)
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
        color = Color.White.copy(alpha = 0.25f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = size.width * 0.005f)
    )
}

private fun DrawScope.drawGrip(x: Float, barY: Float, side: Float) {
    val width = size.width * 0.118f
    val height = size.height * 0.064f
    val topLeft = Offset(x - width / 2f, barY - height * 0.72f)
    drawRoundRect(
        brush = Brush.verticalGradient(
            0f to DrgGripRubber.copy(alpha = 0.98f),
            1f to DrgGripRubber.copy(alpha = 0.75f)
        ),
        topLeft = topLeft,
        size = Size(width, height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(height * 0.42f)
    )
    repeat(4) { rib ->
        val ribX = topLeft.x + width * (0.18f + rib * 0.21f)
        drawLine(
            color = Color.Black.copy(alpha = 0.35f),
            start = Offset(ribX, topLeft.y + height * 0.20f),
            end = Offset(ribX, topLeft.y + height * 0.80f),
            strokeWidth = size.width * 0.005f
        )
    }
    // Bar end weight
    drawCircle(
        color = Color(0xFF1E252D),
        radius = size.width * 0.020f,
        center = Offset(x + side * width * 0.52f, barY - height * 0.34f)
    )
}

private fun DrawScope.drawBrakeLevers(centerX: Float, barY: Float, gripSpread: Float) {
    val leverColor = Color(0xFF3B444E)
    // Left lever (clutch / rear brake)
    val leftLever = Path().apply {
        moveTo(centerX - gripSpread * 0.82f, barY - size.height * 0.022f)
        quadraticTo(
            centerX - gripSpread * 0.60f, barY - size.height * 0.040f,
            centerX - gripSpread * 0.45f, barY - size.height * 0.032f
        )
    }
    drawPath(path = leftLever, color = leverColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))

    // Right lever (front brake)
    val rightLever = Path().apply {
        moveTo(centerX + gripSpread * 0.82f, barY - size.height * 0.022f)
        quadraticTo(
            centerX + gripSpread * 0.60f, barY - size.height * 0.040f,
            centerX + gripSpread * 0.45f, barY - size.height * 0.032f
        )
    }
    drawPath(path = rightLever, color = leverColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))
}
