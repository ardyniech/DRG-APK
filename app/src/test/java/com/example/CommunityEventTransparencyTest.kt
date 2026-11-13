package com.example

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.core.database.AppDatabaseMigrations
import com.example.core.repository.EventSeedData
import com.example.modules.events.CommunityEventsScreen
import com.example.modules.events.logic.EventReadinessCalculator
import com.example.modules.events.logic.EventRiskLevel
import com.example.shared.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CommunityEventTransparencyTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun member(role: MemberRole) = DriverMember(
        id = "DRG-55",
        driverId = "DRG-55",
        name = "Rizky Pratama",
        phone = "081999888777",
        baseArea = "Malang Raya",
        role = role,
        verificationStatus = VerificationStatus.VERIFIED,
        isOnline = true,
        motorcyclePlate = "N 5566 DRG",
        motorcycleModel = "Vario 160"
    )

    @Test
    fun readinessCalculatorHandlesOptimalBlockedAndEmptyTasksSafely() {
        val now = 1_700_000_000_000L
        val healthyEvent = EventSeedData.defaultEvents(now)[1]
        val doneTasks = EventSeedData.defaultTasks(now).filter { it.eventId == healthyEvent.id }
            .map { it.copy(progressPercent = 100, status = CommitteeTaskStatus.SELESAI) }

        val optimalReport = EventReadinessCalculator.analyze(healthyEvent, doneTasks, emptyList(), now)
        assertEquals(100, optimalReport.overallReadinessPct)
        assertEquals(EventRiskLevel.AMAN, optimalReport.riskLevel)

        // Adversarial negative check: empty tasks + 0 estimated budget should never divide by zero
        val bareEvent = healthyEvent.copy(estimatedBudgetRupiah = 0L, eventTimestamp = now - 86_400_000L)
        val bareReport = EventReadinessCalculator.analyze(bareEvent, emptyList(), emptyList(), now)
        assertEquals(100, bareReport.budgetCoveragePct)
        assertEquals(EventRiskLevel.KRITIS_RAWAN_GAGAL, bareReport.riskLevel)
        assertTrue(bareReport.overallReadinessPct in 0..100)

        // Critical feedback & blocked task escalate risk level
        val criticalEvent = EventSeedData.defaultEvents(now)[0]
        val tasks = EventSeedData.defaultTasks(now).filter { it.eventId == criticalEvent.id }
        val fb = EventSeedData.defaultFeedback(now).filter { it.eventId == criticalEvent.id }
        val riskReport = EventReadinessCalculator.analyze(criticalEvent, tasks, fb, now)
        assertEquals(EventRiskLevel.KRITIS_RAWAN_GAGAL, riskReport.riskLevel)
        assertEquals(1, riskReport.blockedTasks)
        assertEquals(1, riskReport.criticalFeedback)
    }

    @Test
    fun communityEventsScreenValidatesFormAllowsFeedbackAndUpdatesCommitteeTasks() {
        val events = EventSeedData.defaultEvents()
        val tasks = EventSeedData.defaultTasks()
        val feedback = EventSeedData.defaultFeedback()

        var createdTitle: String? = null
        var createdTasksCount = 0
        var updatedTaskStatus: CommitteeTaskStatus? = null
        var submittedFeedbackMsg: String? = null
        var submittedFeedbackSeverity: String? = null

        composeRule.setContent {
            CommunityEventsScreen(
                events = events,
                tasks = tasks,
                feedbackList = feedback,
                currentMember = member(MemberRole.ANGGOTA),
                onBack = {},
                onCreateEvent = { title, _, _, _, _, _, _, _, _, _, _, _, draftTasks ->
                    createdTitle = title
                    createdTasksCount = draftTasks.size
                },
                onUpdateEventStage = { _, _, _ -> },
                onAddTask = { _, _, _, _, _, _ -> },
                onUpdateTask = { _, _, st, _ -> updatedTaskStatus = st },
                onSubmitFeedback = { _, _, sev, msg ->
                    submittedFeedbackSeverity = sev
                    submittedFeedbackMsg = msg
                },
                onResolveFeedback = { _, _ -> }
            )
        }

        composeRule.waitForIdle()
        composeRule.onNodeWithTag("community_events_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("event_card_EVT-DRG-2026-01").assertIsDisplayed()

        // Open Record Form Modal & test validation guard on empty fields
        composeRule.onNodeWithTag("btn_create_event_record").performClick()
        composeRule.onNodeWithTag("event_create_modal").assertIsDisplayed()
        composeRule.onNodeWithTag("btn_submit_event_record").performClick()
        assertNull(createdTitle)
        composeRule.onNodeWithText("Nama Event dan Lokasi wajib diisi.").assertIsDisplayed()

        // Fill valid fields & submit full event report with committee task breakdown
        composeRule.onNodeWithTag("input_event_title").performTextInput("Kopdar Gabungan Raya")
        composeRule.onNodeWithTag("input_event_location").performTextInput("Posko Induk")
        composeRule.onNodeWithTag("btn_submit_event_record").performClick()
        assertEquals("Kopdar Gabungan Raya", createdTitle)
        assertTrue(createdTasksCount >= 4)

        // Open event detail transparency view -> submit regular member critical feedback
        composeRule.onNodeWithTag("event_card_EVT-DRG-2026-01").performClick()
        composeRule.onNodeWithTag("event_detail_view").assertIsDisplayed()
        composeRule.onNodeWithTag("event_detail_tab_1").performClick()
        composeRule.onNodeWithTag("severity_chip_KRITIS").performScrollTo().performClick()
        composeRule.onNodeWithTag("input_event_feedback_msg").performScrollTo().performTextInput("Parkir cadangan belum disiapkan panitia")
        composeRule.onNodeWithTag("btn_submit_event_feedback").performScrollTo().performClick()
        assertEquals("KRITIS", submittedFeedbackSeverity)
        assertEquals("Parkir cadangan belum disiapkan panitia", submittedFeedbackMsg)
    }

    @Test
    fun roomDatabasePersistsEventsTasksProgressAndFeedback() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addMigrations(*AppDatabaseMigrations.ALL_MIGRATIONS)
            .allowMainThreadQueries()
            .build()
        val dao = db.communityEventDao()

        dao.insertEvents(EventSeedData.defaultEvents())
        dao.insertTasks(EventSeedData.defaultTasks())
        dao.insertFeedbackList(EventSeedData.defaultFeedback())

        dao.updateTaskProgress("EVTK-01", 100, CommitteeTaskStatus.SELESAI, "Izin RW terbit", 999L)
        dao.resolveFeedback("EVFB-01", "Parkir cadangan dialihkan ke halaman masjid.", true)

        val updatedTask = dao.getAllCommitteeTasks().first().first { it.id == "EVTK-01" }
        val updatedFeedback = dao.getAllEventFeedback().first().first { it.id == "EVFB-01" }

        assertEquals(100, updatedTask.progressPercent)
        assertEquals(CommitteeTaskStatus.SELESAI, updatedTask.status)
        assertTrue(updatedFeedback.isResolved)
        db.close()
    }
}
