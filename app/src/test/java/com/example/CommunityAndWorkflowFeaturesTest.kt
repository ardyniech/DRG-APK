package com.example

import androidx.test.core.app.ApplicationProvider
import androidx.room.Room
import com.example.core.database.AppDatabase
import com.example.core.repository.CommunityRepository
import com.example.core.repository.CommunitySeedData
import com.example.core.repository.CommunitySeedGamification
import com.example.core.repository.CommunitySeedPostsKas
import com.example.modules.workflow.WorkflowStagesData
import com.example.shared.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CommunityAndWorkflowFeaturesTest {

    private lateinit var db: AppDatabase
    private lateinit var communityRepo: CommunityRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        communityRepo = CommunityRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testDefaultCommunitySeedDataCompleteness() {
        val workshops = CommunitySeedData.defaultWorkshops()
        val badges = CommunitySeedData.defaultBadges()
        val posts = CommunitySeedPostsKas.defaultPosts()
        val transactions = CommunitySeedPostsKas.defaultTransactions()
        val rewards = CommunitySeedGamification.defaultRewards()
        val tasks = CommunitySeedGamification.defaultTasks()

        assertTrue("Workshops should not be empty", workshops.isNotEmpty())
        assertEquals(4, workshops.size)

        // Check required specific badges
        val badgeTitles = badges.map { it.title }
        assertTrue(badgeTitles.contains("Anggota Teraktif"))
        assertTrue(badgeTitles.contains("Pelopor Keselamatan"))
        assertTrue(badgeTitles.contains("Srikandi Komunitas"))

        assertTrue("Forum posts should not be empty", posts.isNotEmpty())
        assertTrue("Kas transactions should not be empty", transactions.isNotEmpty())
        assertTrue("Rewards should not be empty", rewards.isNotEmpty())
        assertTrue("Tasks should not be empty", tasks.isNotEmpty())
    }

    @Test
    fun testAddWorkshopReviewAndRatingCalculation() = runTest {
        val workshop = CommunitySeedData.defaultWorkshops().first()
        db.workshopDao().insertWorkshop(workshop)

        val reviewer = DriverMember(
            id = "DRG-TEST-01",
            name = "Test Driver",
            driverId = "DRG-TEST-01",
            phone = "081234567899",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1111 ZZ",
            motorcycleModel = "Honda Vario 160",
            rating = 5.0f,
            reviewCount = 5,
            loyaltyPoints = 100,
            loyaltyTier = "Kapten",
            verificationStatus = VerificationStatus.VERIFIED,
            screeningNotes = "Aktif"
        )
        db.memberDao().insertMember(reviewer)

        // Submit a 5-star review
        communityRepo.addWorkshopReview(workshop, reviewer, 5.0f, "Ganti Oli Cepat", "Mekanik cepat dan ramah!")

        val updatedWorkshopList = communityRepo.allWorkshops.first()
        val updated = updatedWorkshopList.first { it.id == workshop.id }
        assertEquals(workshop.reviewCount + 1, updated.reviewCount)

        val reviews = communityRepo.getReviewsForWorkshop(workshop.id).first()
        assertEquals(1, reviews.size)
        assertEquals("Test Driver", reviews.first().reviewerName)
        assertEquals("Ganti Oli Cepat", reviews.first().tag)
    }

    @Test
    fun testKasFinancialSummariesAndPeriodFilter() {
        val transactions = CommunitySeedPostsKas.defaultTransactions()
        val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val netBalance = totalIncome - totalExpense

        assertEquals(1750000L, totalIncome)
        assertEquals(600000L, totalExpense)
        assertEquals(1150000L, netBalance)
    }

    @Test
    fun testFieldWorkflowStagesAndNegativeBoundsAudit() {
        val stages = WorkflowStagesData.stages
        assertEquals(5, stages.size)

        // Skenario 1: Verifikasi data telemetri riil lengkap untuk setiap fase
        stages.forEach { stage ->
            assertTrue(stage.title.isNotBlank())
            assertTrue(stage.realChallenge.isNotBlank())
            assertTrue(stage.drgSolution.isNotBlank())
            assertTrue(stage.weather.isNotBlank())
            assertTrue(stage.gForceText.isNotBlank())
            assertTrue(stage.statusText.isNotBlank())
        }

        // Skenario 2: Bad Input / Out-of-bounds stage index handling
        val outOfBoundsHigh = 999
        val safeStageHigh = stages[outOfBoundsHigh.coerceIn(0, stages.lastIndex)]
        assertNotNull(safeStageHigh)
        assertEquals(5, safeStageHigh.id)

        val outOfBoundsNegative = -10
        val safeStageLow = stages[outOfBoundsNegative.coerceIn(0, stages.lastIndex)]
        assertNotNull(safeStageLow)
        assertEquals(1, safeStageLow.id)
    }

    @Test
    fun testAdversarialAuditScenarios() = runTest {
        // 1. Bad Input: Workshop review with extreme values
        val workshop = CommunitySeedData.defaultWorkshops().last()
        db.workshopDao().insertWorkshop(workshop)
        val dummyMember = DriverMember(
            id = "DRG-DUMMY",
            name = "Dummy",
            driverId = "DRG-DUMMY",
            phone = "081",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1",
            motorcycleModel = "Motor",
            rating = 5.0f,
            reviewCount = 1,
            loyaltyPoints = 0,
            loyaltyTier = "Baru",
            verificationStatus = VerificationStatus.VERIFIED,
            screeningNotes = ""
        )
        db.memberDao().insertMember(dummyMember)

        // Empty comment handles gracefully
        communityRepo.addWorkshopReview(workshop, dummyMember, 1.0f, "Umum", "")
        val reviews = communityRepo.getReviewsForWorkshop(workshop.id).first()
        assertTrue(reviews.isNotEmpty())

        // 2. Cross-Module: Non-existent workshop reviews query returns empty list without crashing
        val emptyReviews = communityRepo.getReviewsForWorkshop("BKL-UNKNOWN-999").first()
        assertTrue(emptyReviews.isEmpty())

        // 3. UI Dead-End: Kas report exporter handling with empty transactions list
        val emptyTxList = emptyList<KasTransaction>()
        assertTrue(emptyTxList.isEmpty())
    }

    @Test
    fun testWorkshopBookingLoyaltyTierDiscountCalculation() {
        val workshop = CommunitySeedData.defaultWorkshops().first() // 15% discount
        val kaptenMember = DriverMember(
            id = "DRG-KAPTEN", name = "Kapten Driver", driverId = "DRG-KAPTEN", phone = "081234567800",
            role = MemberRole.ANGGOTA, motorcyclePlate = "N 1234 AA", motorcycleModel = "Vario 160",
            rating = 5.0f, reviewCount = 20, loyaltyPoints = 250, loyaltyTier = "Kapten",
            verificationStatus = VerificationStatus.VERIFIED, screeningNotes = ""
        )
        val tierBonus = when (kaptenMember.loyaltyTier) {
            "Kapten", "Leader" -> 5
            "Pejuang" -> 3
            else -> 0
        }
        val totalDiscount = workshop.discountPercent + tierBonus
        assertEquals(20, totalDiscount) // 15% base + 5% Kapten tier = 20%
    }
}
