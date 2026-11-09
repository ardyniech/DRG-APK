package com.example.shared.atoms.cinematic.rider

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import kotlin.math.abs

internal const val RiderCycleMillis = 11000
internal const val RiderBlinkMillis = 880
internal const val RiderBeamMillis = 5200
internal const val RiderEngineVibeMillis = 140

internal fun riderCycleSpec(durationMillis: Int = RiderCycleMillis): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(
        animation = tween(durationMillis, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
    )

internal fun riderBlinkSpec(): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(tween(RiderBlinkMillis, easing = LinearEasing), RepeatMode.Restart)

internal fun riderBeamSpec(): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(tween(RiderBeamMillis, easing = LinearEasing), RepeatMode.Reverse)

internal fun riderVibeSpec(): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(tween(RiderEngineVibeMillis, easing = LinearEasing), RepeatMode.Reverse)

private val SteerPoints = floatArrayOf(
    0.00f, 0.064f, 0.136f, 0.218f, 0.282f, 0.382f,
    0.464f, 0.545f, 0.609f, 0.727f, 0.818f, 1.00f
)

private val SteerValues = floatArrayOf(
    0f, -0.10f, -0.65f, -0.25f, 0f, 0.06f,
    0.62f, 0.30f, 0f, -0.05f, 0.38f, 0f
)

private val SpeedPoints = floatArrayOf(
    0.00f, 0.118f, 0.255f, 0.327f, 0.473f,
    0.582f, 0.691f, 0.818f, 0.945f, 1.00f
)

private val SpeedValues = floatArrayOf(
    0f, 38f, 64f, 47f, 74f,
    58f, 44f, 68f, 39f, 0f
)

internal fun steerAt(t: Float): Float = interpolate(t, SteerPoints, SteerValues)

internal fun speedAt(t: Float): Float = interpolate(t, SpeedPoints, SpeedValues)

internal fun leanAngleAt(steer: Float): Float = steer * 7.5f

internal fun rpmAt(speed: Float): Int = when {
    speed < 1f -> 1250
    speed < 30f -> (2200 + speed * 110f).toInt()
    speed < 55f -> (3600 + (speed - 30f) * 125f).toInt()
    else -> (5800 + (speed - 55f) * 110f).toInt().coerceAtMost(8500)
}

internal fun gearAt(speed: Float): String = when {
    speed < 1f -> "N"
    speed < 24f -> "1"
    speed < 46f -> "2"
    speed < 66f -> "3"
    else -> "4"
}

internal fun turnSideAt(steer: Float): Int = when {
    steer < -0.18f -> -1
    steer > 0.18f -> 1
    else -> 0
}

private fun interpolate(t: Float, points: FloatArray, values: FloatArray): Float {
    val clamped = t.coerceIn(0f, 1f)
    for (i in 0 until points.lastIndex) {
        val start = points[i]
        val end = points[i + 1]
        if (clamped in start..end) {
            val span = end - start
            val fraction = if (abs(span) < 0.0001f) 0f else (clamped - start) / span
            return values[i] + (values[i + 1] - values[i]) * fraction
        }
    }
    return values.last()
}