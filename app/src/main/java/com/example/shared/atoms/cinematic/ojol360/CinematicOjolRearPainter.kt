package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.*

fun DrawScope.drawOjolRearView(centerX: Float, centerY: Float, bounceY: Float) {
    val cy = centerY + bounceY

    drawOval(
        color = DrgRedPanic.copy(alpha = 0.28f),
        topLeft = Offset(centerX - 60f, cy + 28f),
        size = Size(120f, 36f)
    )

    drawRoundRect(
        color = DrgRubberTyreDark,
        topLeft = Offset(centerX - 20f, cy + 34f),
        size = Size(40f, 84f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
    )
    drawRect(color = DrgTreadSlate, topLeft = Offset(centerX - 4f, cy + 42f), size = Size(8f, 68f))

    drawRoundRect(
        color = DrgChassisSteel,
        topLeft = Offset(centerX - 26f, cy + 22f),
        size = Size(52f, 22f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(centerX - 18f, cy + 26f),
        size = Size(36f, 14f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
    )
    drawRect(color = Color.Black, topLeft = Offset(centerX - 14f, cy + 30f), size = Size(28f, 5f))

    drawRoundRect(
        color = DrgRedPanic,
        topLeft = Offset(centerX - 28f, cy + 6f),
        size = Size(56f, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )
    drawCircle(color = DrgTaillightCore, center = Offset(centerX, cy + 12f), radius = 4f)

    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 52f, cy - 88f),
        size = Size(104f, 74f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
    )

    drawCircle(color = Color.White, center = Offset(centerX, cy - 52f), radius = 16f)
    drawCircle(color = DrgGrabGreenPrimary, center = Offset(centerX, cy - 52f), radius = 12f)
    drawRect(color = Color.White, topLeft = Offset(centerX - 6f, cy - 55f), size = Size(12f, 6f))

    drawCircle(color = DrgChassisSteel, center = Offset(centerX, cy - 114f), radius = 24f)
    drawRoundRect(
        color = DrgSafetyReflectorYellow,
        topLeft = Offset(centerX - 14f, cy - 108f),
        size = Size(28f, 6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
}
