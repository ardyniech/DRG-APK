package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.graphics.Brush
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSegmentOn
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

internal fun DrawScope.drawBatteryIndicator(center: Offset, unit: Float, level: Float) {
    val color = indicatorColor(true)
    val bodyW = unit * 0.92f
    val bodyH = unit * 0.46f
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - bodyW / 2f, center.y - bodyH / 2f),
        size = Size(bodyW, bodyH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.08f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = unit * 0.075f)
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x + bodyW / 2f, center.y - unit * 0.13f),
        size = Size(unit * 0.13f, unit * 0.26f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.04f)
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - bodyW / 2f + unit * 0.10f, center.y - bodyH / 2f + unit * 0.10f),
        size = Size((bodyW - unit * 0.20f) * level, bodyH - unit * 0.20f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.05f)
    )
}

internal fun DrawScope.drawFuelIndicator(center: Offset, unit: Float, level: Float) {
    val color = indicatorColor(level < 0.2f)
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - unit * 0.46f, center.y - unit * 0.44f),
        size = Size(unit * 0.72f, unit * 0.88f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.08f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = unit * 0.075f)
    )
    drawLine(
        color = color,
        start = Offset(center.x + unit * 0.26f, center.y - unit * 0.30f),
        end = Offset(center.x + unit * 0.44f, center.y - unit * 0.30f),
        strokeWidth = unit * 0.07f
    )
    repeat(3) { bar ->
        drawRoundRect(
            color = color.copy(alpha = 0.85f),
            topLeft = Offset(center.x - unit * 0.34f + bar * unit * 0.24f, center.y + unit * 0.32f - unit * 0.22f * (bar + 1) * level),
            size = Size(unit * 0.14f, unit * 0.22f * (bar + 1) * level + unit * 0.04f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(unit * 0.03f)
        )
    }
}

internal fun DrawScope.drawAbsBadge(center: Offset, unit: Float) {
    drawBadge(center, unit, "ABS", indicatorColor(true), Brush.horizontalGradient(listOf(DrgSegmentOn, DrgSegmentGlow)))
}

internal fun DrawScope.drawP2pBadge(center: Offset, unit: Float) {
    drawBadge(center, unit, "P2P", indicatorColor(true), Brush.horizontalGradient(listOf(DrgSegmentOn, DrgSegmentGlow)))
}

internal fun DrawScope.drawBadge(
    center: Offset,
    unit: Float,
    label: String,
    color: Color,
    brush: Brush
) {
    val badgeW = unit * 1.02f
    val badgeH = unit * 0.44f
    drawRoundRect(
        color = color.copy(alpha = 0.14f),
        topLeft = Offset(center.x - badgeW / 2f, center.y - badgeH / 2f),
        size = Size(badgeW, badgeH),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(badgeH * 0.4f)
    )
    val barW = badgeW * 0.18f
    val gap = badgeW * 0.10f
    val totalW = label.length * barW + (label.length - 1) * gap
    repeat(label.length) { index ->
        drawRoundRect(
            color = color,
            topLeft = Offset(center.x - totalW / 2f + index * (barW + gap), center.y - badgeH * 0.22f),
            size = Size(barW, badgeH * 0.44f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(barW * 0.3f)
        )
    }
}