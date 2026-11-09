package com.example.shared.atoms.cinematic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgHeadlightWarm

internal fun DrawScope.drawStreetLightPoles(
    phase: Float,
    horizonY: Float,
    bottomY: Float,
    steer: Float
) {
    val poleCount = 6
    val vanishX = size.width * (0.5f - steer * 0.16f)

    repeat(poleCount) { index ->
        val t = wrapUnit(phase + index.toFloat() / poleCount)
        val depth = t * t
        if (depth < 0.05f) return@repeat

        val y = horizonY + (bottomY - horizonY) * depth
        val poleH = (bottomY - horizonY) * 0.42f * depth
        val poleW = (size.width * 0.012f * depth).coerceAtLeast(1.5f)

        // Draw left side pole
        val leftX = vanishX - (size.width * 0.52f * depth) - (size.width * 0.04f * depth)
        drawPoleWithLamp(leftX, y, poleW, poleH, isLeft = true, depth = depth)

        // Draw right side pole
        val rightX = vanishX + (size.width * 0.52f * depth) + (size.width * 0.04f * depth)
        drawPoleWithLamp(rightX, y, poleW, poleH, isLeft = false, depth = depth)
    }
}

private fun DrawScope.drawPoleWithLamp(
    baseX: Float,
    baseY: Float,
    poleW: Float,
    poleH: Float,
    isLeft: Boolean,
    depth: Float
) {
    val topY = baseY - poleH
    val armLength = poleW * 5.5f
    val lampX = if (isLeft) baseX + armLength else baseX - armLength

    // Vertical pole
    drawRect(
        color = Color(0xFF333E48).copy(alpha = 0.55f + depth * 0.45f),
        topLeft = Offset(baseX - poleW / 2f, topY),
        size = Size(poleW, poleH)
    )

    // Overhanging curved arm towards road
    val armPath = Path().apply {
        moveTo(baseX, topY)
        quadraticTo(baseX, topY - poleH * 0.08f, lampX, topY - poleH * 0.04f)
    }
    drawPath(
        path = armPath,
        color = Color(0xFF475569).copy(alpha = 0.75f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = poleW * 0.85f)
    )

    // Glowing sodium lamp fixture
    val lampRadius = (poleW * 2.2f).coerceAtLeast(2.5f)
    val lampColor = DrgHeadlightWarm.copy(alpha = (0.35f + depth * 0.65f).coerceIn(0f, 1f))
    drawCircle(
        color = lampColor,
        radius = lampRadius,
        center = Offset(lampX, topY - poleH * 0.04f)
    )

    // Soft street ground halo illumination
    val haloRadius = poleH * 0.55f
    drawCircle(
        brush = Brush.radialGradient(
            0f to DrgHeadlightWarm.copy(alpha = 0.14f * depth),
            0.6f to DrgHeadlightWarm.copy(alpha = 0.04f * depth),
            1f to Color.Transparent,
            center = Offset(lampX, baseY)
        ),
        radius = haloRadius,
        center = Offset(lampX, baseY)
    )
}
