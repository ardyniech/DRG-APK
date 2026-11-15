package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.*

fun DrawScope.drawOjolSideView(
    centerX: Float,
    centerY: Float,
    bounceY: Float,
    facingRight: Boolean
) {
    val cy = centerY + bounceY
    val dir = if (facingRight) 1f else -1f

    val rearWheelCenter = Offset(centerX - dir * 65f, cy + 58f)
    val frontWheelCenter = Offset(centerX + dir * 75f, cy + 58f)
    drawCircle(color = DrgRubberTyreDark, center = rearWheelCenter, radius = 28f)
    drawCircle(color = DrgAlloyRimSlate, center = rearWheelCenter, radius = 14f)
    drawCircle(color = DrgRubberTyreDark, center = frontWheelCenter, radius = 28f)
    drawCircle(color = DrgAlloyRimSlate, center = frontWheelCenter, radius = 14f)

    drawRoundRect(
        color = DrgChassisSteel,
        topLeft = Offset(centerX - 42f, cy + 24f),
        size = Size(84f, 32f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
    )

    drawRoundRect(
        color = DrgExhaustSilver,
        topLeft = Offset(centerX - dir * 55f, cy + 46f),
        size = Size(50f, 10f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )

    drawRoundRect(
        brush = Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 35f, cy),
        size = Size(78f, 28f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
    )

    drawRoundRect(
        color = DrgHelmetVisorObsidian,
        topLeft = Offset(centerX - dir * 30f, cy - 8f),
        size = Size(64f, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )

    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 24f, cy - 65f),
        size = Size(48f, 58f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
    )
    drawRoundRect(
        color = DrgGrabGreenPrimary,
        topLeft = Offset(centerX + (if (facingRight) 4f else -26f), cy - 42f),
        size = Size(26f, 14f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )

    drawCircle(color = DrgChassisSteel, center = Offset(centerX, cy - 88f), radius = 22f)
    drawRoundRect(
        color = DrgHelmetVisorObsidian,
        topLeft = Offset(centerX + dir * 6f, cy - 94f),
        size = Size(16f, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
}
