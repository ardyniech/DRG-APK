package com.example.shared.atoms.cinematic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgTextDark
import kotlin.math.cos
import kotlin.math.sin

internal const val GaugeStartAngle = 150f
internal const val GaugeSweepAngle = 240f

internal fun DrawScope.drawGaugeTicks(strokeWidth: Float, tickCount: Int = 7) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f - strokeWidth * 0.9f
    repeat(tickCount) { index ->
        val fraction = index.toFloat() / (tickCount - 1)
        val angleRad = Math.toRadians((GaugeStartAngle + GaugeSweepAngle * fraction).toDouble())
        val outer = radius
        val inner = radius - strokeWidth * 0.62f
        drawLine(
            color = DrgTextDark.copy(alpha = 0.34f),
            start = Offset(center.x + cos(angleRad).toFloat() * inner, center.y + sin(angleRad).toFloat() * inner),
            end = Offset(center.x + cos(angleRad).toFloat() * outer, center.y + sin(angleRad).toFloat() * outer),
            strokeWidth = strokeWidth * 0.16f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

internal fun DrawScope.drawGaugeNeedle(progress: Float, strokeWidth: Float, needleColor: Color) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val angleRad = Math.toRadians((GaugeStartAngle + GaugeSweepAngle * progress).toDouble())
    val length = size.minDimension / 2f - strokeWidth * 0.35f
    drawLine(
        color = needleColor,
        start = center,
        end = Offset(center.x + cos(angleRad).toFloat() * length, center.y + sin(angleRad).toFloat() * length),
        strokeWidth = strokeWidth * 0.30f,
        cap = androidx.compose.ui.graphics.StrokeCap.Round
    )
    drawCircle(color = needleColor.copy(alpha = 0.30f), radius = strokeWidth * 0.52f, center = center)
    drawCircle(color = needleColor, radius = strokeWidth * 0.24f, center = center)
}