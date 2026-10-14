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
}

internal fun DrawScope.drawSkylineLayer(
    blocks: List<SkylineBlock>,
    drift: Float,
    horizonY: Float,
    silhouette: Color,
    maxHeightRatio: Float,
    windows: Boolean
) {
    val layerWidth = size.width * 1.25f
    blocks.forEach { block ->
        val x = (wrapUnit(block.offset - drift * block.layerSpeed)) * layerWidth - size.width * 0.12f
        val height = block.height * maxHeightRatio * size.height
        drawRect(
            color = silhouette,
            topLeft = Offset(x, horizonY - height),
            size = Size(block.width * size.width, height)
        )
        if (windows) {
            drawWindows(x, horizonY - height, block)
        }
    }
}

private fun DrawScope.drawWindows(x: Float, top: Float, block: SkylineBlock) {
    if (block.litWindows < 0.35f) return
    val width = block.width * size.width
    val rows = (block.height * size.height / 14f).toInt().coerceIn(1, 7)
    val columns = (width / 12f).toInt().coerceIn(1, 4)
    repeat(rows) { row ->
        repeat(columns) { column ->
            if ((row + column) % 3 != 0) return@repeat
            drawRect(
                color = DrgWindowGlow.copy(alpha = 0.16f + block.litWindows * 0.2f),
                topLeft = Offset(x + 4f + column * (width / columns), top + 6f + row * 13f),
                size = Size(3f, 4f)
            )
        }
    }
}

internal fun DrawScope.drawAsphalt(horizonY: Float, bottomY: Float) {
    val road = Path().apply {
        moveTo(size.width * 0.46f, horizonY)
        lineTo(size.width * 0.54f, horizonY)
        lineTo(size.width * 1.02f, bottomY)
        lineTo(-size.width * 0.02f, bottomY)
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
}

internal fun DrawScope.drawLaneDashes(phase: Float, horizonY: Float, bottomY: Float) {
    val dashCount = 7
    repeat(dashCount) { index ->
        val t = wrapUnit(phase + index.toFloat() / dashCount)
        val depth = t * t
        val y = horizonY + (bottomY - horizonY) * depth
        val markerWidth = size.width * 0.018f * depth
        val markerHeight = (bottomY - horizonY) * 0.05f * depth
        drawRect(
            color = DrgLaneMarker.copy(alpha = 0.16f + depth * 0.62f),
            topLeft = Offset(size.width * 0.5f - markerWidth / 2f, y),
            size = Size(markerWidth, markerHeight.coerceAtLeast(1.5f))
        )
    }
}
