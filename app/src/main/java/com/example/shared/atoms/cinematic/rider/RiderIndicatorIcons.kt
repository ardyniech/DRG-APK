package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.DrgIndicatorDim
import com.example.ui.theme.DrgIndicatorLit
import com.example.ui.theme.DrgIndicatorWarn
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSegmentOn

internal fun DrawScope.indicatorColor(lit: Boolean, warn: Boolean = false): Color = when {
    lit && warn -> DrgIndicatorWarn
    lit -> DrgIndicatorLit
    else -> DrgIndicatorDim
}

internal fun DrawScope.drawArrow(center: Offset, unit: Float, lit: Boolean, mirrored: Boolean) {
    val color = indicatorColor(lit)
    val direction = if (mirrored) -1f else 1f
    val path = Path().apply {
        moveTo(center.x + direction * unit * 0.55f, center.y)
        lineTo(center.x - direction * unit * 0.10f, center.y - unit * 0.46f)
        lineTo(center.x - direction * unit * 0.10f, center.y - unit * 0.16f)
        lineTo(center.x - direction * unit * 0.58f, center.y - unit * 0.16f)
        lineTo(center.x - direction * unit * 0.58f, center.y + unit * 0.16f)
        lineTo(center.x - direction * unit * 0.10f, center.y + unit * 0.16f)
        lineTo(center.x - direction * unit * 0.10f, center.y + unit * 0.46f)
        close()
    }
    if (lit) {
        drawCircle(color = DrgSegmentGlow.copy(alpha = 0.16f), radius = unit * 0.62f, center = center)
    }
    drawPath(path = path, color = color)
}

internal fun DrawScope.drawBeamIndicator(center: Offset, unit: Float, high: Boolean) {
    val color = indicatorColor(high)
    repeat(4) { ray ->
        val x = center.x - unit * 0.42f + ray * unit * 0.28f
        drawLine(
            color = color,
            start = Offset(x, center.y - unit * 0.30f),
            end = Offset(x, center.y - unit * 0.52f),
            strokeWidth = unit * 0.075f,
            cap = StrokeCap.Round
        )
    }
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - unit * 0.48f, center.y - unit * 0.10f),
        size = Size(unit * 0.96f, unit * 0.42f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.10f)
    )
}

internal fun DrawScope.drawLowBeamIndicator(center: Offset, unit: Float) {
    val color = indicatorColor(true)
    drawLine(
        color = color,
        start = Offset(center.x - unit * 0.44f, center.y - unit * 0.46f),
        end = Offset(center.x - unit * 0.10f, center.y - unit * 0.16f),
        strokeWidth = unit * 0.085f,
        cap = StrokeCap.Round
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - unit * 0.48f, center.y - unit * 0.06f),
        size = Size(unit * 0.96f, unit * 0.40f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.10f)
    )
}
