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

internal fun riderCycleSpec(durationMillis: Int = RiderCycleMillis): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(
        animation = tween(durationMillis, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
    )

internal fun riderBlinkSpec(): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(tween(RiderBlinkMillis, easing = LinearEasing), RepeatMode.Restart)

internal fun riderBeamSpec(): InfiniteRepeatableSpec<Float> =
    infiniteRepeatable(tween(RiderBeamMillis, easing = LinearEasing), RepeatMode.Reverse)

private val SteerPoints = floatArrayOf(
    0.00f, 0.064f, 0.136f, 0.218f, 0.282f, 0.382f,
    0.464f, 0.545f, 0.609f, 0.727f, 0.818f, 1.00f
)

private val SteerValues = floatArrayOf(
    0f, -0.10f, -0.72f, -0.30f, 0f, 0.06f,
    0.70f, 0.34f, 0f, -0.05f, 0.42f, 0f
)

private val SpeedPoints = floatArrayOf(
    0.00f, 0.118f, 0.255f, 0.327f, 0.473f,
    0.582f, 0.691f, 0.818f, 0.945f, 1.00f
)

private val SpeedValues = floatArrayOf(
    0f, 38f, 62f, 47f, 71f,
    58f, 44f, 66f, 39f, 0f
)

internal fun steerAt(t: Float): Float = interpolate(t, SteerPoints, SteerValues)

internal fun speedAt(t: Float): Float = interpolate(t, SpeedPoints, SpeedValues)

internal fun turnSideAt(steer: Float): Int = when {
    steer < -0.22f -> -1
    steer > 0.22f -> 1
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