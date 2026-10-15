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
    val highBeam: Boolean
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

    val steer = steerAt(cycle)
    return RiderSteeringState(
        steer = steer,
        speed = speedAt(cycle).toInt(),
        turnSide = turnSideAt(steer),
        signalOn = blink > 0.52f,
        highBeam = beam > 0.5f
    )
}