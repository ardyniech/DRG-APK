package com.example

import com.example.shared.atoms.cinematic.rider.gearAt
import com.example.shared.atoms.cinematic.rider.leanAngleAt
import com.example.shared.atoms.cinematic.rider.rpmAt
import com.example.shared.atoms.cinematic.rider.speedAt
import com.example.shared.atoms.cinematic.rider.steerAt
import com.example.shared.atoms.cinematic.rider.turnSideAt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RiderPovCinematicTest {

    @Test
    fun `speed and rpm scale proportionally from idle to cruising speed`() {
        val idleSpeed = speedAt(0f)
        assertEquals(0f, idleSpeed, 0.01f)

        val idleRpm = rpmAt(idleSpeed)
        assertEquals(1250, idleRpm)

        val cruiseSpeed = speedAt(0.473f)
        assertTrue("Cruising speed should be above 60kmh", cruiseSpeed > 60f)

        val cruiseRpm = rpmAt(cruiseSpeed)
        assertTrue("Cruising RPM should be above 6000", cruiseRpm > 6000)
    }

    @Test
    fun `gear shifting accurately reflects driver speed progression`() {
        assertEquals("Neutral when stationary", "N", gearAt(0f))
        assertEquals("First gear at takeoff", "1", gearAt(15f))
        assertEquals("Second gear in city traffic", "2", gearAt(35f))
        assertEquals("Third gear at medium speed", "3", gearAt(55f))
        assertEquals("Fourth gear at highway speed", "4", gearAt(72f))
    }

    @Test
    fun `steering and lean angle calculate bank angle and turn direction`() {
        val leftSteer = -0.5f
        val leftLean = leanAngleAt(leftSteer)
        assertTrue("Leaning left must have negative bank angle", leftLean < 0f)
        assertEquals("Turn side is left (-1)", -1, turnSideAt(leftSteer))

        val rightSteer = 0.4f
        val rightLean = leanAngleAt(rightSteer)
        assertTrue("Leaning right must have positive bank angle", rightLean > 0f)
        assertEquals("Turn side is right (1)", 1, turnSideAt(rightSteer))

        val neutralSteer = 0f
        assertEquals("Center position has 0 bank angle", 0f, leanAngleAt(neutralSteer), 0.01f)
        assertEquals("Center position has 0 turn side", 0, turnSideAt(neutralSteer))
    }

    @Test
    fun `adversarial bad input values are safely clamped`() {
        // Underflow / negative inputs
        val underflowSteer = steerAt(-5f)
        assertEquals(0f, underflowSteer, 0.01f)

        // Overflow inputs
        val overflowSpeed = speedAt(10f)
        assertEquals(0f, overflowSpeed, 0.01f)

        // Extreme speed values clamp RPM to rev limiter max 8500
        val overRevRpm = rpmAt(250f)
        assertEquals(8500, overRevRpm)
    }
}
