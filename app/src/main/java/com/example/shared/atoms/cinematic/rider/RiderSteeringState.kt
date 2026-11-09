package com.example.shared.atoms.cinematic.rider

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue

@Stable
class RiderSteeringState(
    val steer: Float,
    val speed: Int,
    val turnSide: Int,
    val signalOn: Boolean,
    val highBeam: Boolean,
    val rpm: Int,
    val gear: String,
    val leanAngle: Float,
    val vibeY: Float,
    val vibeX: Float
) {
    val isTurning: Boolean get() = turnSide != 0
}

@Composable
fun rememberRiderSteeringState(): RiderSteeringState {
    val transition = rememberInfiniteTransition(label = "rider")

    val cycle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = riderCycleSpec(),
        label = "cycle"
    )
    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = riderBlinkSpec(),
        label = "blink"
    )
    val beam by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = riderBeamSpec(),
        label = "beam"
    )
    val vibe by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = riderVibeSpec(),
        label = "vibe"
    )

    val currentSteer = steerAt(cycle)
    val rawSpeed = speedAt(cycle)
    val vibeMagnitude = if (rawSpeed > 5f) (1.2f + (rawSpeed / 75f) * 1.8f) else 0.8f

    return RiderSteeringState(
        steer = currentSteer,
        speed = rawSpeed.toInt(),
        turnSide = turnSideAt(currentSteer),
        signalOn = blink > 0.50f,
        highBeam = beam > 0.50f,
        rpm = rpmAt(rawSpeed),
        gear = gearAt(rawSpeed),
        leanAngle = leanAngleAt(currentSteer),
        vibeY = vibe * vibeMagnitude,
        vibeX = (vibe * 0.45f) * vibeMagnitude
    )
}