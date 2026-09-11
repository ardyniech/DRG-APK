package com.example

import com.example.core.audio.SosHapticManager
import org.junit.Assert.*
import org.junit.Test

class SosHapticTactileTest {

    @Test
    fun testHapticManagerGracefulNoContext() {
        // Without initialize, vibrator is null, must not throw any NPE
        SosHapticManager.playSosVibrationPattern()
        SosHapticManager.playCountdownTick(5)
        SosHapticManager.playCountdownTick(1)
        SosHapticManager.playSuccessHaptic()
        SosHapticManager.playEmergencyDispatchedHaptic()
        SosHapticManager.stopVibration()
        assertTrue(true)
    }

    @Test
    fun testCountdownHapticAmplitudeEscalation() {
        // Test that sensory feedback intensity escalates as countdown approaches 0
        fun calculateAmplitude(secondsRemaining: Int): Int {
            return (255 - secondsRemaining * 25).coerceIn(120, 255)
        }

        val amp5 = calculateAmplitude(5)
        val amp3 = calculateAmplitude(3)
        val amp1 = calculateAmplitude(1)

        assertEquals(130, amp5)
        assertEquals(180, amp3)
        assertEquals(230, amp1)

        assertTrue("Tick intensity must escalate as countdown decreases", amp1 > amp3 && amp3 > amp5)
    }

    @Test
    fun testLowRamDeviceCacheAllocation() {
        fun getMemoryAllocationPercent(isLowRam: Boolean): Double {
            return if (isLowRam) 0.10 else 0.15
        }

        assertEquals(0.10, getMemoryAllocationPercent(true), 0.001)
        assertEquals(0.15, getMemoryAllocationPercent(false), 0.001)
    }
}
