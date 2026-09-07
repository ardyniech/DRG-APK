package com.example

import com.example.shared.models.CrashSensitivity
import org.junit.Assert.*
import org.junit.Test

class CrashDetectionTest {

    @Test
    fun testCrashSensitivityThresholds() {
        assertEquals(3.5f, CrashSensitivity.LOW.thresholdG)
        assertEquals(2.8f, CrashSensitivity.MEDIUM.thresholdG)
        assertEquals(2.2f, CrashSensitivity.HIGH.thresholdG)
    }

    @Test
    fun testCrashSensitivityParsing() {
        assertEquals(CrashSensitivity.LOW, CrashSensitivity.fromName("LOW"))
        assertEquals(CrashSensitivity.HIGH, CrashSensitivity.fromName("HIGH"))
        assertEquals(CrashSensitivity.MEDIUM, CrashSensitivity.fromName("UNKNOWN_VALUE"))
        assertEquals(CrashSensitivity.MEDIUM, CrashSensitivity.fromName(null))
    }

    @Test
    fun testThresholdDetectionEvaluation() {
        val sensitivity = CrashSensitivity.MEDIUM // 2.8G threshold
        val normalRideGForce = 1.4f
        val speedBumpGForce = 2.1f
        val crashImpactGForce = 3.6f

        assertFalse(normalRideGForce >= sensitivity.thresholdG)
        assertFalse(speedBumpGForce >= sensitivity.thresholdG)
        assertTrue(crashImpactGForce >= sensitivity.thresholdG)
    }
}
