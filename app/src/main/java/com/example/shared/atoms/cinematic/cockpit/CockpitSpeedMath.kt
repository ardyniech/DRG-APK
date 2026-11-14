package com.example.shared.atoms.cinematic.cockpit

import kotlin.math.PI
import kotlin.math.sin

const val MIN_CRUISING_SPEED = 40f
const val MAX_CRUISING_SPEED = 65f

/**
 * Calculates a smooth, organic cruising speed between 40 km/h and 65 km/h
 * mimicking natural throttle modulation on an open highway.
 */
fun calculateOrganicSpeed(timeSeconds: Float): Float {
    if (timeSeconds.isNaN() || timeSeconds.isInfinite()) return 52.5f

    // Primary wave (period ~14s) + secondary subtle wave (period ~5.5s)
    val w1 = sin(timeSeconds * (2f * PI.toFloat() / 14f))
    val w2 = sin(timeSeconds * (2f * PI.toFloat() / 5.5f)) * 0.35f
    val combined = (w1 + w2) / 1.35f // Normalized -1.0 to +1.0

    val midSpeed = (MIN_CRUISING_SPEED + MAX_CRUISING_SPEED) * 0.5f
    val amplitude = (MAX_CRUISING_SPEED - MIN_CRUISING_SPEED) * 0.5f

    return (midSpeed + combined * amplitude).coerceIn(MIN_CRUISING_SPEED, MAX_CRUISING_SPEED)
}

/**
 * Computes RPM based on cruising speed, ranging from 4,800 to 6,800 RPM.
 */
fun calculateClusterRpm(speedKmh: Float): Int {
    val clamped = speedKmh.coerceIn(MIN_CRUISING_SPEED, MAX_CRUISING_SPEED)
    val ratio = (clamped - MIN_CRUISING_SPEED) / (MAX_CRUISING_SPEED - MIN_CRUISING_SPEED)
    return (4800 + (ratio * 2000f)).toInt()
}

/**
 * Calculates engine micro-vibration magnitude (in pixels) scaling with speed.
 */
fun calculateEngineRumble(speedKmh: Float, vibePhase: Float): Float {
    val speedRatio = (speedKmh.coerceIn(MIN_CRUISING_SPEED, MAX_CRUISING_SPEED) - MIN_CRUISING_SPEED) / 25f
    val magnitude = 0.8f + speedRatio * 1.4f
    return vibePhase * magnitude
}
