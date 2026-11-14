package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary

fun DrawScope.drawOjolSideView(
    centerX: Float,
    centerY: Float,
    bounceY: Float,
    facingRight: Boolean
) {
    val cy = centerY + bounceY
    val dir = if (facingRight) 1f else -1f

    // 1. Rear & Front Wheels with alloy disc
    val rearWheelCenter = Offset(centerX - dir * 65f, cy + 58f)
    val frontWheelCenter = Offset(centerX + dir * 75f, cy + 58f)
    drawCircle(color = Color(0xFF14181B), center = rearWheelCenter, radius = 28f)
    drawCircle(color = Color(0xFF475569), center = rearWheelCenter, radius = 14f)
    drawCircle(color = Color(0xFF14181B), center = frontWheelCenter, radius = 28f)
    drawCircle(color = Color(0xFF475569), center = frontWheelCenter, radius = 14f)

    // 2. Chassis Frame & Engine Crankcase
    drawRoundRect(
        color = Color(0xFF1E293B),
        topLeft = Offset(centerX - 42f, cy + 24f),
        size = Size(84f, 32f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
    )

    // 3. Exhaust Pipe & Muffler
    drawRoundRect(
        color = Color(0xFF64748B),
        topLeft = Offset(centerX - dir * 55f, cy + 46f),
        size = Size(50f, 10f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )

    // 4. Motorcycle Bodywork & Fuel Tank (DRG Green)
    drawRoundRect(
        brush = Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 35f, cy),
        size = Size(78f, 28f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
    )

    // 5. Dual Seat
    drawRoundRect(
        color = Color(0xFF0F172A),
        topLeft = Offset(centerX - dir * 30f, cy - 8f),
        size = Size(64f, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )

    // 6. Rider Body (Side Riding Posture)
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

    // 7. Rider Helmet (Profile View with Visor)
    drawCircle(color = Color(0xFF1E293B), center = Offset(centerX, cy - 88f), radius = 22f)
    drawRoundRect(
        color = Color(0xFF0F172A),
        topLeft = Offset(centerX + dir * 6f, cy - 94f),
        size = Size(16f, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
}
