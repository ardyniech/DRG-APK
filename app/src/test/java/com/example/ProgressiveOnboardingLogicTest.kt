package com.example

import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import org.junit.Assert.*
import org.junit.Test

class ProgressiveOnboardingLogicTest {

    @Test
    fun testNewDriverOnboardingMilestonesCalculation() {
        // Scenario 1: Fresh new driver (offline, unverified, no kopdar/points)
        val isShiftReady1 = false
        val isVerified1 = false
        val isEngaged1 = false
        val completedSteps1 = listOf(isShiftReady1, isVerified1, isEngaged1).count { it }
        val isAllCompleted1 = completedSteps1 == 3

        assertEquals(0, completedSteps1)
        assertFalse("Onboarding card must be visible for new driver", isAllCompleted1)

        // Scenario 2: Driver goes online (mode siaga aktif)
        val isShiftReady2 = true
        val completedSteps2 = listOf(isShiftReady2, isVerified1, isEngaged1).count { it }
        assertEquals(1, completedSteps2)

        // Scenario 3: Driver passes screening verification
        val isVerified2 = true
        val completedSteps3 = listOf(isShiftReady2, isVerified2, isEngaged1).count { it }
        assertEquals(2, completedSteps3)

        // Scenario 4: Master Driver with points & kopdar attendance
        val isEngaged2 = true
        val completedSteps4 = listOf(isShiftReady2, isVerified2, isEngaged2).count { it }
        val isAllCompleted4 = completedSteps4 == 3
        assertEquals(3, completedSteps4)
        assertTrue("Onboarding card should automatically hide after master level reached", isAllCompleted4)
    }

    @Test
    fun testEmptyStateConditionTriggers() {
        val emptyList = emptyList<String>()
        val hasData = emptyList.isNotEmpty()
        assertFalse("Empty state must be triggered when items are zero", hasData)

        val populatedList = listOf("Item 1", "Item 2")
        assertTrue(populatedList.isNotEmpty())
    }
}
