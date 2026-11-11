package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgClusterBezel

internal fun DrawScope.drawFairingCowl(cowlTopY: Float, cowlBottomY: Float) {
    val centerX = size.width / 2f
    val halfW = size.width * 0.44f

    val cowlPath = Path().apply {
        moveTo(centerX - halfW * 0.70f, cowlTopY)
        cubicTo(
            centerX - halfW * 0.35f, cowlTopY - size.height * 0.015f,
            centerX + halfW * 0.35f, cowlTopY - size.height * 0.015f,
            centerX + halfW * 0.70f, cowlTopY
        )
        lineTo(centerX + halfW, cowlBottomY)
        lineTo(centerX - halfW, cowlBottomY)
        close()
    }

    // Textured matte black fairing body
    drawPath(
        path = cowlPath,
        brush = Brush.verticalGradient(
            0f to DrgClusterBezel.copy(alpha = 0.98f),
            0.5f to DrgFairingMid,
            1f to DrgFairingBot
        )
    )

    // Cowl crease bevel lines
    drawLine(
        color = DrgFairingCrease,
        start = Offset(centerX - halfW * 0.65f, cowlTopY + 4f),
        end = Offset(centerX - halfW * 0.35f, cowlBottomY),
        strokeWidth = 2.5f
    )
    drawLine(
        color = DrgFairingCrease,
        start = Offset(centerX + halfW * 0.65f, cowlTopY + 4f),
        end = Offset(centerX + halfW * 0.35f, cowlBottomY),
        strokeWidth = 2.5f
    )

    // Instrument cluster hood shadow
    val shadowH = size.height * 0.035f
    drawRect(
        brush = Brush.verticalGradient(
            0f to Color.Black.copy(alpha = 0.85f),
            1f to Color.Transparent
        ),
        topLeft = Offset(centerX - halfW * 0.55f, cowlTopY),
        size = Size(halfW * 1.10f, shadowH)
    )
}
