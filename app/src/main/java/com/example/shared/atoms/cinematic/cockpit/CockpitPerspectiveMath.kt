package com.example.shared.atoms.cinematic.cockpit

data class ProjectedLaneDash(
    val centerX: Float,
    val topY: Float,
    val width: Float,
    val length: Float,
    val alpha: Float
)

/**
 * Projects a 3D highway lane dash onto 2D screen coordinates
 * flowing from the horizon vanishing point directly toward the motorcycle.
 */
fun projectRoadDash(
    stepProgress: Float,
    horizonY: Float,
    screenHeight: Float,
    roadCenterX: Float,
    steerOffset: Float
): ProjectedLaneDash {
    val progress = (stepProgress % 1.0f).let { if (it < 0f) it + 1.0f else it }
    // Quadratic depth acceleration: marks rush faster as they approach the cockpit
    val depthT = progress * progress

    val roadHeight = screenHeight - horizonY
    val y = horizonY + depthT * roadHeight

    // Road dashes expand laterally and in length as they approach the foreground
    val dashWidth = 3.5f + depthT * 18f
    val dashLength = 16f + depthT * 95f
    val lateralCurvature = steerOffset * depthT * 40f

    val alpha = (0.25f + depthT * 0.75f).coerceIn(0f, 1f)

    return ProjectedLaneDash(
        centerX = roadCenterX + lateralCurvature,
        topY = y,
        width = dashWidth,
        length = dashLength,
        alpha = alpha
    )
}

fun clampCockpitZoom(zoom: Float): Float {
    if (zoom.isNaN() || zoom.isInfinite()) return 1.0f
    return zoom.coerceIn(0.85f, 2.40f)
}

fun clampSteerAngle(steer: Float): Float {
    if (steer.isNaN() || steer.isInfinite()) return 0f
    return steer.coerceIn(-1.0f, 1.0f)
}
