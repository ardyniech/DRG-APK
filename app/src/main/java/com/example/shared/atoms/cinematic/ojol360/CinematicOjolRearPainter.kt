package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgRedPanic

fun DrawScope.drawOjolRearView(centerX: Float, centerY: Float, bounceY: Float) {
    val cy = centerY + bounceY

    // 0. Red Taillight Glow Bloom on road
    drawOval(
        color = DrgRedPanic.copy(alpha = 0.28f),
        topLeft = Offset(centerX - 60f, cy + 28f),
        size = Size(120f, 36f)
    )

    // 1. Wide Rear Tyre & Tread
    drawRoundRect(
        color = Color(0xFF14181B),
        topLeft = Offset(centerX - 20f, cy + 34f),
        size = Size(40f, 84f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
    )
    drawRect(color = Color(0xFF334155), topLeft = Offset(centerX - 4f, cy + 42f), size = Size(8f, 68f))

    // 2. Mudguard & License Plate ("N 1928 DRG")
    drawRoundRect(
        color = Color(0xFF1E293B),
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

    // 3. LED Taillight Bar
    drawRoundRect(
        color = DrgRedPanic,
        topLeft = Offset(centerX - 28f, cy + 6f),
        size = Size(56f, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )
    drawCircle(color = Color(0xFFFF8A80), center = Offset(centerX, cy + 12f), radius = 4f)

    // 4. Rider Back (Iconic DRG Green Jacket)
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
        topLeft = Offset(centerX - 52f, cy - 88f),
        size = Size(104f, 74f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
    )

    // DRG Reflective Back Shield Logo
    drawCircle(color = Color.White, center = Offset(centerX, cy - 52f), radius = 16f)
    drawCircle(color = DrgGrabGreenPrimary, center = Offset(centerX, cy - 52f), radius = 12f)
    drawRect(color = Color.White, topLeft = Offset(centerX - 6f, cy - 55f), size = Size(12f, 6f))

    // 5. Rider Helmet (Rear View with Safety Reflector)
    drawCircle(color = Color(0xFF1E293B), center = Offset(centerX, cy - 114f), radius = 24f)
    drawRoundRect(
        color = Color(0xFFFACC15),
        topLeft = Offset(centerX - 14f, cy - 108f),
        size = Size(28f, 6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
    )
}
