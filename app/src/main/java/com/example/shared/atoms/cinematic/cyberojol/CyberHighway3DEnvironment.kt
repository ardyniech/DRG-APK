package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgAsphaltDeep
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgHeadlightWarm
import com.example.ui.theme.DrgLaneMarker
import com.example.ui.theme.DrgWarmGradientEnd
import com.example.ui.theme.DrgWarmGradientStart
import kotlin.math.cos

fun DrawScope.drawCyber3DHighway(
    cam: CameraTransform,
    rushProgress: Float
) {
    val horizonY = size.height * 0.40f

    // 1. Sky & Atmospheric Dawn Horizon
    drawRect(
        brush = Brush.verticalGradient(
            0f to DrgWarmGradientStart.copy(alpha = 0.90f),
            0.65f to DrgWarmGradientEnd,
            1f to Color(0xFFD0EBD7)
        ),
        topLeft = Offset.Zero,
        size = Size(size.width, horizonY)
    )

    // 2. Horizon Mountain Ridge
    val ridgePath = Path().apply {
        moveTo(0f, horizonY)
        lineTo(size.width * 0.28f, horizonY - 42f)
        lineTo(size.width * 0.52f, horizonY - 18f)
        lineTo(size.width * 0.76f, horizonY - 55f)
        lineTo(size.width, horizonY - 12f)
        lineTo(size.width, horizonY)
        close()
    }
    drawPath(path = ridgePath, color = DrgGrabGreenPrimary.copy(alpha = 0.18f))

    // 3. Perspective 3D Asphalt Highway
    val roadTopWidth = size.width * 0.38f
    val roadTopLeft = (size.width - roadTopWidth) * 0.5f
    val roadPath = Path().apply {
        moveTo(roadTopLeft, horizonY)
        lineTo(roadTopLeft + roadTopWidth, horizonY)
        lineTo(size.width * 1.15f, size.height)
        lineTo(-size.width * 0.15f, size.height)
        close()
    }
    drawPath(
        path = roadPath,
        brush = Brush.verticalGradient(listOf(Color(0xFF23322B), DrgAsphaltDeep))
    )

    // 4. Dynamic Markings Rushing in 3D Perspective
    drawDynamic3DMarkings(horizonY, rushProgress, cam.yawDegrees)

    // 5. Headlight Projection Cone when facing front
    val yawRad = (normalizeAngle(cam.yawDegrees) * Math.PI / 180.0).toFloat()
    val cosVal = cos(yawRad)
    if (cosVal < -0.2f) { // Facing forward / oncoming
        val coneIntensity = (-cosVal).coerceIn(0f, 1f) * 0.45f
        val headlightPath = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.65f)
            lineTo(size.width * 0.1f, size.height)
            lineTo(size.width * 0.9f, size.height)
            close()
        }
        drawPath(
            path = headlightPath,
            brush = Brush.verticalGradient(
                0f to DrgHeadlightWarm.copy(alpha = coneIntensity),
                1f to Color.Transparent
            )
        )
    }
}

private fun DrawScope.drawDynamic3DMarkings(horizonY: Float, progress: Float, yawDeg: Float) {
    val roadHeight = size.height - horizonY
    val dashCount = 8
    val centerX = size.width * 0.5f

    for (i in 0 until dashCount) {
        val step = (i.toFloat() / dashCount + progress) % 1.0f
        val depthT = step * step
        val y = horizonY + depthT * roadHeight
        val w = (3f + depthT * 10f)
        val h = (14f + depthT * 48f)

        drawRect(
            color = DrgLaneMarker.copy(alpha = 0.35f + depthT * 0.60f),
            topLeft = Offset(centerX - w * 0.5f, y),
            size = Size(w, h)
        )
    }
}
