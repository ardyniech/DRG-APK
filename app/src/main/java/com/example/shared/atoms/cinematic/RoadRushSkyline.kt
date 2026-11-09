package com.example.shared.atoms.cinematic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgWindowGlow

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
