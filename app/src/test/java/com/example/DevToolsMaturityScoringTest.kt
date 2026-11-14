package com.example

import androidx.test.core.app.ApplicationProvider
import android.content.Context
import com.example.modules.devtools.DeadButtonShallowScanner
import com.example.modules.devtools.EarlySymptomDetector
import com.example.modules.devtools.FeatureStage
import com.example.modules.devtools.PhaseTreeScanner
import com.example.modules.devtools.ProjectHealthScore
import com.example.modules.devtools.RealtimeMemoryProbe
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevToolsMaturityScoringTest {

    @Test
    fun testMaturityStagesClassification() {
        val features = DeadButtonShallowScanner.getAuditedFeatures()
        assertTrue("Features list must not be empty", features.isNotEmpty())

        val stages = features.map { it.stage }
        assertTrue("Must contain Mature stage", stages.contains(FeatureStage.MATURE))
        assertTrue("Must contain Lulus stage", stages.contains(FeatureStage.LULUS))
    }

    @Test
    fun testDeadButtonsAndShallowFeaturesDetection() {
        val deadButtonCount = DeadButtonShallowScanner.countDeadButtons()
        val shallowCount = DeadButtonShallowScanner.countShallowFeatures()

        assertEquals(0, deadButtonCount)
        assertEquals(0, shallowCount)
    }

    @Test
    fun testStrictScoringAndPenaltyEngine() {
        val score = ProjectHealthScore()
        assertEquals(95.0f, score.baseScore, 0.01f)
        assertEquals(0, score.deadButtonsCount)
        assertEquals(0, score.shallowFeaturesCount)
        assertTrue("Bawel: production defects >=0", score.bugPenaltiesCount >= 0)
        val expectedGaps = PhaseTreeScanner.getMetrics().progressGapCount
        assertEquals("Progress penalty must be derived from PhaseTree gaps", expectedGaps, score.progressPenaltiesCount)

        val expectedPenalty = (0 * 4f) + (0 * 3f) + (score.bugPenaltiesCount * 2f) + (expectedGaps * 2f)
        assertEquals(expectedPenalty, score.totalPenalty, 0.01f)
        assertEquals(95.0f - expectedPenalty, score.finalScore, 0.01f)
        assertTrue(score.finalScore > 60f)
        assertTrue(score.grade.contains("Grade"))
    }

    @Test
    fun testRootCauseAnalysisAndEarlySymptoms() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val findings = EarlySymptomDetector.diagnose(context)
        assertTrue("Findings must be generated", findings.isNotEmpty())
    }

    @Test
    fun testRealtimeMemoryProbe() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val stats = RealtimeMemoryProbe.inspect(context)
        assertTrue("Total memory must be positive", stats.deviceTotalRamMb > 0)
    }
}
