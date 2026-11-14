package com.example.shared.atoms.cinematic.ojol360

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val MIN_ZOOM = 0.75f
const val MAX_ZOOM = 2.50f
const val DEFAULT_ZOOM = 1.0f

fun normalizeAngle(angle: Float): Float {
    if (angle.isNaN() || angle.isInfinite()) return 0f
    val mod = angle % 360f
    return if (mod < 0f) mod + 360f else mod
}

fun clampZoom(zoom: Float): Float {
    if (zoom.isNaN() || zoom.isInfinite()) return DEFAULT_ZOOM
    return zoom.coerceIn(MIN_ZOOM, MAX_ZOOM)
}

fun quadrantLabel(angle: Float): String {
    val norm = normalizeAngle(angle)
    return when {
        norm >= 315f || norm < 45f -> "Tampak Belakang (Chase 0°)"
        norm in 45f..<135f -> "Profil Kanan (Side 90°)"
        norm in 135f..<225f -> "Tampak Depan (Headlight 180°)"
        else -> "Profil Kiri (Side 270°)"
    }
}

fun calcYawCos(angle: Float): Float {
    val rad = (normalizeAngle(angle) * PI / 180.0).toFloat()
    return cos(rad)
}

fun calcYawSin(angle: Float): Float {
    val rad = (normalizeAngle(angle) * PI / 180.0).toFloat()
    return sin(rad)
}

fun calcPerspectiveScale(angle: Float, baseZoom: Float): Float {
    val norm = normalizeAngle(angle)
    // Subtle perspective depth when rotating front vs rear
    val depthFactor = 1.0f + 0.12f * calcYawCos(norm)
    return clampZoom(baseZoom) * depthFactor
}
