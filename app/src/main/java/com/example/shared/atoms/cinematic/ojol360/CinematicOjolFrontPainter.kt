package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.*

fun DrawScope.drawOjolFrontView(centerX: Float, centerY: Float, bounceY: Float) {
    val cy = centerY + bounceY

    val beamPath = Path().apply {
        moveTo(centerX - 24f, cy + 18f)
        lineTo(centerX - 130f, size.height)
        lineTo(centerX + 130f, size.height)
        lineTo(centerX + 24f, cy + 18f)
        close()
    }
    drawPath(
        path = beamPath,
        brush = Brush.verticalGradient(
            0f to DrgHeadlightWarm.copy(alpha = 0.42f),
            1f to Color.Transparent
        )
    )

    drawRoundRect(
        color = DrgRubberTyreDark,
        topLeft = Offset(centerX - 16f, cy + 42f),
        size = Size(32f, 74f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
    )
    drawCircle(color = DrgAlloyRimDeep, center = Offset(centerX, cy + 78f), radius = 12f)

    drawRect(color = DrgForkSilver, topLeft = Offset(centerX - 24f, cy + 24f), size = Size(6f, 48f))
    drawRect(color = DrgForkSilver, topLeft = Offset(centerX + 18f, cy + 24f), size = Size(6f, 48f))

    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 42f, cy - 8f),
        size = Size(84f, 52f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
    )

    drawCircle(color = DrgHeadlightWarm, center = Offset(centerX - 20f, cy + 14f), radius = 10f)
    drawCircle(color = DrgHeadlightWarm, center = Offset(centerX + 20f, cy + 14f), radius = 10f)
    drawCircle(color = Color.White, center = Offset(centerX - 20f, cy + 14f), radius = 5f)
    drawCircle(color = Color.White, center = Offset(centerX + 20f, cy + 14f), radius = 5f)

    drawRoundRect(
        color = DrgOjolWindscreenGlassTint,
        topLeft = Offset(centerX - 28f, cy - 38f),
        size = Size(56f, 36f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
    )

    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 50f, cy - 90f),
        size = Size(100f, 62f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
    )
    drawRect(color = DrgHarnessSilver, topLeft = Offset(centerX - 36f, cy - 65f), size = Size(72f, 6f))

    drawCircle(color = DrgChassisSteel, center = Offset(centerX, cy - 114f), radius = 24f)
    drawRoundRect(
        color = DrgHelmetVisorObsidian,
        topLeft = Offset(centerX - 16f, cy - 120f),
        size = Size(32f, 14f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )
    drawRect(color = Color.White.copy(alpha = 0.6f), topLeft = Offset(centerX - 12f, cy - 118f), size = Size(8f, 3f))
}
