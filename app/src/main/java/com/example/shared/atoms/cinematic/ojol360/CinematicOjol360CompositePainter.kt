package com.example.shared.atoms.cinematic.ojol360

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform

fun DrawScope.drawCinematicOjol360(
    angle: Float,
    zoom: Float,
    bounceY: Float
) {
    val normAngle = normalizeAngle(angle)
    val centerX = size.width * 0.5f
    val centerY = size.height * 0.65f
    val perspectiveScale = calcPerspectiveScale(normAngle, zoom)

    scale(scale = perspectiveScale, pivot = Offset(centerX, centerY)) {
        val cosVal = calcYawCos(normAngle)
        val sinVal = calcYawSin(normAngle)

        // Dynamic banking lean when turning
        val leanDegrees = -sinVal * 12f

        withTransform({
            rotate(degrees = leanDegrees, pivot = Offset(centerX, centerY))
            translate(left = sinVal * 18f, top = 0f)
        }) {
            when {
                // Front quadrant (135° to 225°)
                normAngle in 135f..<225f -> {
                    drawOjolFrontView(centerX, centerY, bounceY)
                }
                // Right side quadrant (45° to 135°)
                normAngle in 45f..<135f -> {
                    drawOjolSideView(centerX, centerY, bounceY, facingRight = true)
                }
                // Left side quadrant (225° to 315°)
                normAngle in 225f..<315f -> {
                    drawOjolSideView(centerX, centerY, bounceY, facingRight = false)
                }
                // Rear quadrant (315° to 45°)
                else -> {
                    drawOjolRearView(centerX, centerY, bounceY)
                }
            }
        }
    }
}
