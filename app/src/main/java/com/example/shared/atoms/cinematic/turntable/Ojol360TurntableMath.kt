package com.example.shared.atoms.cinematic.turntable

import kotlin.math.roundToInt

const val TOTAL_TURNTABLE_FRAMES = 16
const val MIN_TURNTABLE_ZOOM = 0.80f
const val MAX_TURNTABLE_ZOOM = 2.60f
const val DEFAULT_TURNTABLE_ZOOM = 1.0f

fun normalizeTurntableAngle(angle: Float): Float {
    if (angle.isNaN() || angle.isInfinite()) return 0f
    val mod = angle % 360f
    return if (mod < 0f) mod + 360f else mod
}

fun angleToFrameIndex(angle: Float, totalFrames: Int = TOTAL_TURNTABLE_FRAMES): Int {
    val norm = normalizeTurntableAngle(angle)
    val step = 360f / totalFrames
    val rawIndex = (norm / step).roundToInt() % totalFrames
    return if (rawIndex < 0) rawIndex + totalFrames else rawIndex
}

fun clampTurntableZoom(zoom: Float): Float {
    if (zoom.isNaN() || zoom.isInfinite()) return DEFAULT_TURNTABLE_ZOOM
    return zoom.coerceIn(MIN_TURNTABLE_ZOOM, MAX_TURNTABLE_ZOOM)
}

fun turntableQuadrantLabel(angle: Float): String {
    val norm = normalizeTurntableAngle(angle)
    return when {
        norm >= 337.5f || norm < 22.5f -> "Tampak Depan (Front 0°)"
        norm in 22.5f..<112.5f -> "Profil Samping Kiri (Side 90°)"
        norm in 112.5f..<202.5f -> "Tampak Belakang (Rear 180°)"
        norm in 202.5f..<292.5f -> "Profil Samping Kanan (Side 270°)"
        else -> "Tampak Depan (Front 360°)"
    }
}
