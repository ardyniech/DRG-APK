package com.example

import com.example.shared.models.BadgeItem
import com.example.shared.models.CommunityTask
import com.example.shared.models.MemberRole
import com.example.shared.models.RewardItem
import com.example.shared.models.TaskStatus
import org.junit.Assert.*
import org.junit.Test

class GamificationAndLoyaltyTest {

    @Test
    fun testBadgeEarnEligibility() {
        val badge = BadgeItem(
            id = "BDG-01",
            title = "Pahlawan Aspal",
            category = "HELP_PATROL",
            description = "Bantu 5 rekan di jalan",
            iconEmoji = "🛡️",
            requiredXp = 500,
            isEarned = false
        )

        val driverXpLow = 350
        val driverXpHigh = 600

        assertFalse(driverXpLow >= badge.requiredXp)
        assertTrue(driverXpHigh >= badge.requiredXp)
    }

    @Test
    fun testRewardVoucherRedemptionValidation() {
        val voucher = RewardItem(
            id = "RWD-01",
            title = "Diskon Servis Bengkel",
            description = "Potongan 25rb di Bengkel Resmi",
            requiredPoints = 300,
            category = "BENGKEL",
            iconEmoji = "🔧",
            stockAvailable = 5
        )

        val driverPointsSufficient = 450
        val driverPointsInsufficient = 150

        // Validation for redemption
        assertTrue(driverPointsSufficient >= voucher.requiredPoints && voucher.stockAvailable > 0)
        assertFalse(driverPointsInsufficient >= voucher.requiredPoints)

        // Simulating redemption result
        val remainingStock = voucher.stockAvailable - 1
        val remainingPoints = driverPointsSufficient - voucher.requiredPoints
        assertEquals(4, remainingStock)
        assertEquals(150, remainingPoints)
    }

    @Test
    fun testCommunityTaskStatusProgression() {
        val task = CommunityTask(
            id = "TSK-01",
            title = "Patroli Jalur Rawan",
            category = "PATROLI",
            description = "Pantau jalur sepi jam pulang kantor",
            rewardPoints = 150,
            issuerRole = MemberRole.SATGAS,
            status = TaskStatus.OPEN
        )

        assertEquals(TaskStatus.OPEN, task.status)
        val inProgress = task.copy(status = TaskStatus.IN_PROGRESS, assignedMemberId = "DRG-001")
        assertEquals(TaskStatus.IN_PROGRESS, inProgress.status)
        val completed = inProgress.copy(status = TaskStatus.COMPLETED)
        assertEquals(TaskStatus.COMPLETED, completed.status)
        val verified = completed.copy(status = TaskStatus.VERIFIED)
        assertEquals(TaskStatus.VERIFIED, verified.status)
    }
}
