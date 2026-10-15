package com.example.shared.atoms.cinematic.rider

import com.example.ui.theme.DrgSegmentOff
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSegmentOn

internal fun DrawScope.drawUnitLabel(
    topLeft: Offset,
    faceW: Float,
    faceH: Float,
    thickness: Float
) {
    drawRoundRect(
        color = DrgSegmentOn.copy(alpha = 0.62f),
        topLeft = Offset(topLeft.x + faceW * 0.60f, topLeft.y + faceH * 0.44f),
        size = Size(faceW * 0.075f, thickness * 0.85f),
        cornerRadius = CornerRadius(thickness * 0.4f)
    )
    repeat(4) { tick ->
        drawRoundRect(
            color = DrgSegmentOn.copy(alpha = 0.34f),
            topLeft = Offset(topLeft.x + faceW * (0.70f + tick * 0.055f), topLeft.y + faceH * 0.30f),
            size = Size(faceW * 0.014f, faceH * 0.10f),
            cornerRadius = CornerRadius(thickness * 0.3f)
        )
    }
}

internal fun DrawScope.drawGearBadge(
    topLeft: Offset,
    faceW: Float,
    faceH: Float,
    state: RiderSteeringState
) {
    val gearLevel = when {
        state.speed < 1 -> 0
        state.speed < 32 -> 1
        state.speed < 58 -> 2
        state.speed < 86 -> 3
        else -> 4
    }
    val badgeW = faceW * 0.115f
    val badgeH = faceH * 0.26f
    drawRoundRect(
        color = DrgSegmentOn.copy(alpha = 0.12f),
        topLeft = Offset(topLeft.x + faceW * 0.795f, topLeft.y + faceH * 0.16f),
        size = Size(badgeW, badgeH),
        cornerRadius = CornerRadius(faceH * 0.05f)
    )
    repeat(4) { index ->
        val barW = badgeW * 0.16f
        val lit = index < gearLevel
        drawRoundRect(
            color = DrgSegmentOn.copy(alpha = if (lit) 0.9f else 0.16f),
            topLeft = Offset(
                topLeft.x + faceW * 0.795f + badgeW * (0.12f + index * 0.22f),
                topLeft.y + faceH * 0.24f
            ),
            size = Size(barW, badgeH * 0.52f),
            cornerRadius = CornerRadius(barW * 0.3f)
        )
    }
}

internal fun DrawScope.drawEcoBar(
    topLeft: Offset,
    faceW: Float,
    faceH: Float,
    state: RiderSteeringState
) {
    val barW = faceW * 0.26f
    val barH = faceH * 0.052f
    val originY = topLeft.y + faceH * 0.80f
    drawRoundRect(
        color = DrgSegmentOff,
        topLeft = Offset(topLeft.x + faceW * 0.085f, originY),
        size = Size(barW, barH),
        cornerRadius = CornerRadius(barH * 0.5f)
    )
    val fill = (state.speed / 90f).coerceIn(0.05f, 1f)
    val ecoColor = if (fill > 0.78f) Color(0xFFF5B301) else DrgSegmentOn
    drawRoundRect(
        color = ecoColor.copy(alpha = 0.86f),
        topLeft = Offset(topLeft.x + faceW * 0.085f, originY),
        size = Size(barW * fill, barH),
        cornerRadius = CornerRadius(barH * 0.5f)
    )
}

internal fun DrawScope.drawClusterReflection(
    topLeft: Offset,
    faceW: Float,
    faceH: Float
) {
    drawRoundRect(
        brush = Brush.linearGradient(
            0f to Color.White.copy(alpha = 0.07f),
            0.35f to Color.Transparent,
            1f to Color.Transparent
        ),
        topLeft = Offset(topLeft.x + faceW * 0.04f, topLeft.y + faceH * 0.04f),
        size = Size(faceW * 0.52f, faceH * 0.90f),
        cornerRadius = CornerRadius(faceH * 0.14f)
    )
}