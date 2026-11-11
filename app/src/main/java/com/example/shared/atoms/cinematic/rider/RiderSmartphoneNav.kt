package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgPhoneBezel
import com.example.ui.theme.DrgPhoneMount
import com.example.ui.theme.DrgPhoneRoad
import com.example.ui.theme.DrgPhoneScreenBot
import com.example.ui.theme.DrgPhoneScreenTop
import com.example.ui.theme.DrgTextDark

internal fun DrawScope.drawSmartphoneNav(centerX: Float, centerY: Float, speed: Int) {
    val phoneW = size.width * 0.165f
    val phoneH = phoneW * 1.82f
    val topLeft = Offset(centerX - phoneW / 2f, centerY - phoneH / 2f)

    // Mount clamp arms
    drawRoundRect(
        color = DrgPhoneMount,
        topLeft = Offset(topLeft.x - 4f, centerY - phoneH * 0.22f),
        size = Size(phoneW + 8f, phoneH * 0.44f),
        cornerRadius = CornerRadius(4f)
    )

    // Phone casing & bezel
    drawRoundRect(
        color = DrgPhoneBezel,
        topLeft = topLeft,
        size = Size(phoneW, phoneH),
        cornerRadius = CornerRadius(phoneW * 0.12f)
    )

    // Map screen area
    val screenInset = phoneW * 0.05f
    val screenTopLeft = Offset(topLeft.x + screenInset, topLeft.y + screenInset)
    val screenW = phoneW - screenInset * 2f
    val screenH = phoneH - screenInset * 2f

    drawRoundRect(
        brush = Brush.verticalGradient(
            0f to DrgPhoneScreenTop,
            1f to DrgPhoneScreenBot
        ),
        topLeft = screenTopLeft,
        size = Size(screenW, screenH),
        cornerRadius = CornerRadius(phoneW * 0.09f)
    )

    // Subtle map roads
    drawLine(
        color = DrgPhoneRoad,
        start = Offset(screenTopLeft.x + screenW * 0.2f, screenTopLeft.y + screenH),
        end = Offset(screenTopLeft.x + screenW * 0.8f, screenTopLeft.y),
        strokeWidth = 2.5f
    )

    // Active DRG GPS green navigation route line
    val route = Path().apply {
        moveTo(screenTopLeft.x + screenW * 0.5f, screenTopLeft.y + screenH * 0.85f)
        lineTo(screenTopLeft.x + screenW * 0.5f, screenTopLeft.y + screenH * 0.45f)
        lineTo(screenTopLeft.x + screenW * 0.72f, screenTopLeft.y + screenH * 0.20f)
    }
    drawPath(
        path = route,
        color = DrgGrabGreenPrimary.copy(alpha = 0.95f),
        style = Stroke(width = screenW * 0.09f)
    )

    // Current position vehicle arrow
    val arrowX = screenTopLeft.x + screenW * 0.5f
    val arrowY = screenTopLeft.y + screenH * 0.65f
    val navArrow = Path().apply {
        moveTo(arrowX, arrowY - screenW * 0.10f)
        lineTo(arrowX + screenW * 0.08f, arrowY + screenW * 0.08f)
        lineTo(arrowX, arrowY + screenW * 0.03f)
        lineTo(arrowX - screenW * 0.08f, arrowY + screenW * 0.08f)
        close()
    }
    drawPath(path = navArrow, color = Color.White)

    // Top instruction pill (e.g. 200m belok kanan)
    drawRoundRect(
        color = DrgGrabGreenPrimary.copy(alpha = 0.88f),
        topLeft = Offset(screenTopLeft.x + screenW * 0.10f, screenTopLeft.y + screenH * 0.06f),
        size = Size(screenW * 0.80f, screenH * 0.12f),
        cornerRadius = CornerRadius(screenW * 0.04f)
    )

    // Glass reflection
    drawRoundRect(
        brush = Brush.linearGradient(
            0f to Color.White.copy(alpha = 0.12f),
            0.5f to Color.Transparent
        ),
        topLeft = screenTopLeft,
        size = Size(screenW * 0.6f, screenH),
        cornerRadius = CornerRadius(phoneW * 0.09f)
    )
}
