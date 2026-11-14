package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgAsphaltDeep
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgLaneMarker
import com.example.ui.theme.DrgWarmGradientEnd
import com.example.ui.theme.DrgWarmGradientStart

fun DrawScope.drawCinematicRoadEnvironment(
    rushProgress: Float,
    angle: Float
) {
    val horizonY = size.height * 0.42f
    val cosYaw = calcYawCos(angle)
    val sinYaw = calcYawSin(angle)

    // 1. Dynamic Atmospheric Sky
    drawRect(
        brush = Brush.verticalGradient(
            0f to DrgWarmGradientStart.copy(alpha = 0.85f),
            0.65f to DrgWarmGradientEnd,
            1f to Color(0xFFD4EBD9)
        ),
        topLeft = Offset.Zero,
        size = Size(size.width, horizonY)
    )

    // 2. Distant mountain & horizon ridge
    val ridgePath = Path().apply {
        moveTo(0f, horizonY)
        lineTo(size.width * 0.25f, horizonY - 32f)
        lineTo(size.width * 0.50f, horizonY - 14f)
        lineTo(size.width * 0.72f, horizonY - 45f)
        lineTo(size.width, horizonY - 8f)
        lineTo(size.width, horizonY)
        close()
    }
    drawPath(
        path = ridgePath,
        color = DrgGrabGreenPrimary.copy(alpha = 0.16f)
    )

    // 3. Asphalt Highway Surface
    val asphaltPath = Path().apply {
        val roadTopWidth = size.width * 0.45f
        val roadTopLeft = (size.width - roadTopWidth) * 0.5f - sinYaw * 40f
        val roadBotLeft = -size.width * 0.15f - sinYaw * 90f
        val roadBotRight = size.width * 1.15f - sinYaw * 90f

        moveTo(roadTopLeft, horizonY)
        lineTo(roadTopLeft + roadTopWidth, horizonY)
        lineTo(roadBotRight, size.height)
        lineTo(roadBotLeft, size.height)
        close()
    }
    drawPath(
        path = asphaltPath,
        brush = Brush.verticalGradient(
            0f to Color(0xFF2B3A33),
            1f to DrgAsphaltDeep
        )
    )

    // 4. Animated Lane Markings (reacts to 360 yaw)
    drawDynamicLaneDashes(rushProgress, cosYaw, sinYaw, horizonY)

    // 5. Motorcycle Contact Shadow on Asphalt
    val shadowCenter = Offset(size.width * 0.5f, size.height * 0.76f)
    drawOval(
        color = Color.Black.copy(alpha = 0.52f),
        topLeft = Offset(shadowCenter.x - 90f, shadowCenter.y - 18f),
        size = Size(180f, 36f)
    )
}

private fun DrawScope.drawDynamicLaneDashes(
    progress: Float,
    cosYaw: Float,
    sinYaw: Float,
    horizonY: Float
) {
    val roadHeight = size.height - horizonY
    val dashCount = 7
    val centerX = size.width * 0.5f - sinYaw * 55f

    for (i in 0 until dashCount) {
        val step = (i.toFloat() / dashCount + progress) % 1.0f
        // Perspective distribution: dashes spread out as they approach the camera
        val depthT = step * step
        val y = if (cosYaw >= 0) horizonY + depthT * roadHeight else size.height - depthT * roadHeight
        val dashLength = 12f + depthT * 42f
        val dashWidth = 3f + depthT * 8f

        drawRect(
            color = DrgLaneMarker.copy(alpha = 0.45f + depthT * 0.50f),
            topLeft = Offset(centerX - dashWidth * 0.5f, y),
            size = Size(dashWidth, dashLength)
        )
    }
}
