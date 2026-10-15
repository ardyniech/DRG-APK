package com.example.shared.atoms.cinematic.rider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

internal const val SEG_A = 0
internal const val SEG_B = 1
internal const val SEG_C = 2
internal const val SEG_D = 3
internal const val SEG_E = 4
internal const val SEG_F = 5
internal const val SEG_G = 6

internal val SegmentMasks = intArrayOf(
    0x3F, 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x07, 0x7F, 0x6F
)

internal fun isSegmentLit(digit: Int, segment: Int): Boolean =
    digit in 0..9 && (SegmentMasks[digit] shr (6 - segment)) and 1 == 1

internal fun segmentRect(segment: Int, width: Float, height: Float, thickness: Float): Rect {
    val inset = thickness * 0.9f
    val half = height / 2f
    val top = inset
    val bottom = height - inset
    val middle = half - thickness * 0.1f
    val vertTopBottom = thickness * 2.1f
    return when (segment) {
        SEG_A -> Rect(Offset(thickness, 0f), Size(width - thickness * 2f, thickness))
        SEG_B -> Rect(Offset(width - thickness, top + inset * 0.2f), Size(thickness, vertTopBottom))
        SEG_C -> Rect(Offset(width - thickness, middle), Size(thickness, vertTopBottom))
        SEG_D -> Rect(Offset(thickness, height - thickness), Size(width - thickness * 2f, thickness))
        SEG_E -> Rect(Offset(0f, middle), Size(thickness, vertTopBottom))
        SEG_F -> Rect(Offset(0f, top + inset * 0.2f), Size(thickness, vertTopBottom))
        else -> Rect(Offset(thickness, middle - thickness * 0.6f), Size(width - thickness * 2f, thickness * 0.8f))
    }
}

internal fun DrawScope.drawSevenSegmentDigit(
    digit: Int,
    topLeft: Offset,
    width: Float,
    height: Float,
    thickness: Float,
    onColor: Color,
    offColor: Color,
    glowColor: Color
) {
    for (segment in 0..6) {
        val rect = segmentRect(segment, width, height, thickness)
        if (isSegmentLit(digit, segment)) {
            drawRect(
                color = glowColor.copy(alpha = 0.16f),
                topLeft = Offset(topLeft.x + rect.left - thickness * 0.7f, topLeft.y + rect.top - thickness * 0.7f),
                size = Size(rect.width + thickness * 1.4f, rect.height + thickness * 1.4f)
            )
        }
        drawRoundRect(
            color = if (isSegmentLit(digit, segment)) onColor else offColor,
            topLeft = Offset(topLeft.x + rect.left, topLeft.y + rect.top),
            size = Size(rect.width, rect.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(thickness * 0.45f)
        )
    }
}

internal fun DrawScope.drawSevenSegmentNumber(
    value: Int,
    topLeft: Offset,
    digitWidth: Float,
    digitHeight: Float,
    thickness: Float,
    gap: Float,
    onColor: Color,
    offColor: Color,
    glowColor: Color
) {
    val digits = value.toString().padStart(3, ' ')
    var cursor = topLeft.x
    digits.forEach { char ->
        if (char == ' ') {
            cursor += digitWidth * 0.52f + gap
        } else {
            drawSevenSegmentDigit(
                digit = char - '0',
                topLeft = Offset(cursor, topLeft.y),
                width = digitWidth,
                height = digitHeight,
                thickness = thickness,
                onColor = onColor,
                offColor = offColor,
                glowColor = glowColor
            )
            cursor += digitWidth + gap
        }
    }
}