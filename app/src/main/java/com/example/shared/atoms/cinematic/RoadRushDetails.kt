package com.example.shared.atoms.cinematic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgHeadlightWarm
import com.example.ui.theme.DrgLaneMarker
import com.example.ui.theme.DrgRainStreak
import kotlin.math.abs

internal fun DrawScope.drawRoadEdges(horizonY: Float, bottomY: Float) {
    val edgeColor = DrgLaneMarker.copy(alpha = 0.34f)
    listOf(0.455f, 0.545f, -0.02f, 1.02f).forEach { anchor ->
        val path = Path()
        val steps = 14
        repeat(steps) { step ->
            val t = step.toFloat() / (steps - 1)
            val depth = t * t
            val y = horizonY + (bottomY - horizonY) * depth
            val x = if (anchor > 1f) size.width * 1.02f * depth + size.width * 0.5f
            else size.width * anchor + (size.width * 0.5f - size.width * anchor) * depth
            if (step == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path = path, color = edgeColor, alpha = edgeColor.alpha)
    }
}

internal fun DrawScope.drawHeadlightCone(horizonY: Float, bottomY: Float, pulse: Float) {
    val cone = Path().apply {
        moveTo(size.width * 0.06f, bottomY)
        lineTo(size.width * 0.94f, bottomY)
        lineTo(size.width * 0.545f, horizonY)
        lineTo(size.width * 0.455f, horizonY)
        close()
    }
    drawPath(
        path = cone,
        brush = Brush.verticalGradient(
            0f to DrgHeadlightWarm.copy(alpha = 0.04f + pulse * 0.05f),
            1f to DrgHeadlightWarm.copy(alpha = 0.20f + pulse * 0.10f),
            startY = horizonY,
            endY = bottomY
        )
    )
}

internal fun DrawScope.drawRain(drops: List<RainDrop>, phase: Float) {
    drops.forEach { drop ->
        val y = wrapUnit(drop.y + phase * drop.speed) * size.height
        val x = drop.x * size.width
        val length = drop.length * size.height
        drawLine(
            color = DrgRainStreak.copy(alpha = 0.10f + abs(drop.speed - 1.2f) * 0.12f),
            start = Offset(x, y),
            end = Offset(x - length * 0.18f, y + length)
        )
    }
}