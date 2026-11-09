package com.example.shared.atoms.cinematic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgAsphaltDeep
import com.example.ui.theme.DrgAsphaltNear
import com.example.ui.theme.DrgDuskHorizon
import com.example.ui.theme.DrgHeadlightWarm
import com.example.ui.theme.DrgLaneMarker
import com.example.ui.theme.DrgNightSkyMid
import com.example.ui.theme.DrgNightSkyTop
import com.example.ui.theme.DrgRainStreak
import com.example.ui.theme.DrgSkylineFar
import com.example.ui.theme.DrgSkylineNear
import com.example.ui.theme.DrgWindowGlow
import kotlin.math.abs

internal fun DrawScope.drawNightSky(horizonY: Float) {
    drawRect(
        brush = Brush.verticalGradient(
            0f to DrgNightSkyTop,
            0.55f to DrgNightSkyMid,
            1f to DrgDuskHorizon,
            startY = 0f,
            endY = horizonY
        ),
        size = Size(size.width, horizonY)
    )
    drawRect(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            0.65f to Color.Transparent,
            1f to Color(0x3800FF99),
            startY = 0f,
            endY = horizonY
        ),
        size = Size(size.width, horizonY)
    )
}



internal fun DrawScope.drawAsphalt(horizonY: Float, bottomY: Float, steer: Float = 0f) {
    val vanishX = size.width * (0.5f - steer * 0.16f)
    val road = Path().apply {
        moveTo(vanishX - size.width * 0.045f, horizonY)
        lineTo(vanishX + size.width * 0.045f, horizonY)
        lineTo(size.width * 1.05f, bottomY)
        lineTo(-size.width * 0.05f, bottomY)
        close()
    }
    drawPath(
        path = road,
        brush = Brush.verticalGradient(
            0f to DrgAsphaltDeep,
            1f to DrgAsphaltNear,
            startY = horizonY,
            endY = bottomY
        )
    )

    // Wet road specular reflection streak
    val reflection = Path().apply {
        moveTo(vanishX - 4f, horizonY)
        lineTo(vanishX + 4f, horizonY)
        lineTo(size.width * 0.58f, bottomY)
        lineTo(size.width * 0.42f, bottomY)
        close()
    }
    drawPath(
        path = reflection,
        brush = Brush.verticalGradient(
            0f to Color(0x22FFFFFF),
            0.6f to Color(0x14FFE3A8),
            1f to Color.Transparent,
            startY = horizonY,
            endY = bottomY
        )
    )
}

internal fun DrawScope.drawLaneDashes(phase: Float, horizonY: Float, bottomY: Float, steer: Float = 0f) {
    val dashCount = 8
    val vanishX = size.width * (0.5f - steer * 0.16f)
    val bottomCenterX = size.width * 0.5f

    repeat(dashCount) { index ->
        val t = wrapUnit(phase + index.toFloat() / dashCount)
        val depth = t * t
        val y = horizonY + (bottomY - horizonY) * depth
        val centerX = vanishX + (bottomCenterX - vanishX) * depth
        val markerWidth = size.width * 0.020f * depth
        val markerHeight = (bottomY - horizonY) * 0.055f * depth
        drawRect(
            color = DrgLaneMarker.copy(alpha = 0.18f + depth * 0.65f),
            topLeft = Offset(centerX - markerWidth / 2f, y),
            size = Size(markerWidth.coerceAtLeast(1f), markerHeight.coerceAtLeast(1.5f))
        )
    }
}
