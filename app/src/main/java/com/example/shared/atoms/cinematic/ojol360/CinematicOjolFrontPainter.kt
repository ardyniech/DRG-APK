package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgHeadlightWarm

fun DrawScope.drawOjolFrontView(centerX: Float, centerY: Float, bounceY: Float) {
    val cy = centerY + bounceY

    // 1. Front Tyre & Alloy Rim
    drawRoundRect(
        color = Color(0xFF14181B),
        topLeft = Offset(centerX - 16f, cy + 42f),
        size = Size(32f, 74f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
    )
    drawCircle(
        color = Color(0xFF4A5568),
        center = Offset(centerX, cy + 78f),
        radius = 12f
    )

    // 2. Dual Front Suspension Forks
    drawRect(color = Color(0xFFCBD5E1), topLeft = Offset(centerX - 24f, cy + 24f), size = Size(6f, 48f))
    drawRect(color = Color(0xFFCBD5E1), topLeft = Offset(centerX + 18f, cy + 24f), size = Size(6f, 48f))

    // 3. Front Fairing & Cowl (DRG Green)
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 42f, cy - 8f),
        size = Size(84f, 52f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
    )

    // 4. Dual LED Headlights (Luminous Glow)
    drawCircle(color = DrgHeadlightWarm, center = Offset(centerX - 20f, cy + 14f), radius = 9f)
    drawCircle(color = DrgHeadlightWarm, center = Offset(centerX + 20f, cy + 14f), radius = 9f)
    drawCircle(color = Color.White, center = Offset(centerX - 20f, cy + 14f), radius = 5f)
    drawCircle(color = Color.White, center = Offset(centerX + 20f, cy + 14f), radius = 5f)

    // 5. Windscreen
    drawRoundRect(
        color = Color(0x991E293B),
        topLeft = Offset(centerX - 28f, cy - 38f),
        size = Size(56f, 36f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
    )

    // 6. Rider Jacket (Chest & Shoulders)
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 50f, cy - 90f),
        size = Size(100f, 62f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(18f, 18f)
    )
    // Silver Reflective Chest Bands
    drawRect(color = Color(0xFFE2E8F0), topLeft = Offset(centerX - 36f, cy - 65f), size = Size(72f, 6f))

    // 7. Rider Helmet & Tinted Visor
    drawCircle(color = Color(0xFF1E293B), center = Offset(centerX, cy - 114f), radius = 24f)
    drawRoundRect(
        color = Color(0xFF0F172A),
        topLeft = Offset(centerX - 16f, cy - 120f),
        size = Size(32f, 14f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
    )
}
