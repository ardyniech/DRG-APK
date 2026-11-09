package com.example.shared.atoms.cinematic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgRedPanic

internal fun DrawScope.drawTrafficLights(
    phase: Float,
    horizonY: Float,
    bottomY: Float,
    steer: Float
) {
    val vanishX = size.width * (0.5f - steer * 0.16f)

    // Preceding motorcycle taillight ahead in our lane
    val leadPhase = wrapUnit(phase * 0.45f + 0.35f)
    val leadDepth = (leadPhase * leadPhase).coerceIn(0.04f, 0.75f)
    val leadY = horizonY + (bottomY - horizonY) * leadDepth
    val leadX = vanishX + (size.width * 0.10f * leadDepth)

    val redRadius = (3.5f + leadDepth * 6.5f)
    drawCircle(
        color = DrgRedPanic.copy(alpha = 0.88f),
        radius = redRadius,
        center = Offset(leadX, leadY)
    )
    drawCircle(
        brush = Brush.radialGradient(
            0f to DrgRedPanic.copy(alpha = 0.32f * leadDepth),
            1f to Color.Transparent,
            center = Offset(leadX, leadY)
        ),
        radius = redRadius * 3.5f,
        center = Offset(leadX, leadY)
    )

    // Oncoming vehicle headlights in opposing lane (left)
    val oncomingPhase = wrapUnit(phase * 0.85f + 0.72f)
    val onDepth = (oncomingPhase * oncomingPhase).coerceIn(0.02f, 0.88f)
    val onY = horizonY + (bottomY - horizonY) * onDepth
    val onBaseX = vanishX - (size.width * 0.28f * onDepth)
    val beamSpread = 4f + onDepth * 14f

    listOf(onBaseX - beamSpread / 2f, onBaseX + beamSpread / 2f).forEach { hx ->
        drawCircle(
            color = Color.White.copy(alpha = 0.92f),
            radius = 2.5f + onDepth * 4.5f,
            center = Offset(hx, onY)
        )
        drawCircle(
            brush = Brush.radialGradient(
                0f to Color.White.copy(alpha = 0.35f * onDepth),
                1f to Color.Transparent,
                center = Offset(hx, onY)
            ),
            radius = 6f + onDepth * 18f,
            center = Offset(hx, onY)
        )
    }
}
