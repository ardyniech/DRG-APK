package com.example

import androidx.test.core.app.ApplicationProvider
import android.content.Context
import com.example.modules.devtools.DeadButtonShallowScanner
import com.example.modules.devtools.EarlySymptomDetector
import com.example.modules.devtools.FeatureStage
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
        assertTrue("Must contain Benih stage", stages.contains(FeatureStage.BENIH))
        assertTrue("Must contain Anak stage", stages.contains(FeatureStage.ANAK))
        assertTrue("Must contain Mature stage", stages.contains(FeatureStage.MATURE))
        assertTrue("Must contain Lulus stage", stages.contains(FeatureStage.LULUS))
    }

    @Test
    fun testDeadButtonsAndShallowFeaturesDetection() {
        val deadButtonCount = DeadButtonShallowScanner.countDeadButtons()
        val shallowCount = DeadButtonShallowScanner.countShallowFeatures()

        assertTrue("Dead buttons should be strictly detected", deadButtonCount > 0)
        assertTrue("Shallow features should be strictly detected", shallowCount > 0)
        assertEquals(2, deadButtonCount)
        assertEquals(2, shallowCount)
    }

    @Test
    fun testStrictScoringAndPenaltyEngine() {
        val score = ProjectHealthScore()
        assertEquals(95.0f, score.baseScore, 0.01f)
        assertEquals(2, score.deadButtonsCount)
        assertEquals(2, score.shallowFeaturesCount)
        // Bawel: bugPenalties = jumlah PRODUCTION_DEFECT dari scanner (4 awal + extra 08,10,11,12 = 6)
        assertTrue("Bawel: production defects >=2", score.bugPenaltiesCount >= 2)
        assertEquals(1, score.progressPenaltiesCount)

        // Penalties: (2*4) + (2*3) + (bug*2) + (1*2)
        val expectedPenalty = (2 * 4f) + (2 * 3f) + (score.bugPenaltiesCount * 2f) + (1 * 2f)
        assertEquals(expectedPenalty, score.totalPenalty, 0.01f)
        assertEquals(95.0f - expectedPenalty, score.finalScore, 0.01f)
        // Dengan 6 production defects, skor turun ke Grade C — jujur, bukan fake Grade B
        assertTrue("Grade harus konsisten dengan skor bawel", score.finalScore < 75f)
        assertTrue(score.grade.contains("Grade"))
    }

    @Test
    fun testRootCauseAnalysisAndEarlySymptoms() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val findings = EarlySymptomDetector.diagnose(context)
        assertTrue("RCA findings must not be empty", findings.isNotEmpty())

        findings.forEach { rca ->
            assertTrue("ID must not be blank", rca.id.isNotBlank())
            assertTrue("Title must not be blank", rca.title.isNotBlank())
            assertTrue("Pre-failure symptom must be identified", rca.preFailureSymptom.isNotBlank())
            assertTrue("Source file must be specified", rca.sourceFile.isNotBlank())
            assertTrue("Source function must be specified", rca.sourceFunction.isNotBlank())
            assertTrue("Technical root cause must be specified", rca.rootCauseTechnical.isNotBlank())
            assertTrue("Actionable fix must be provided", rca.actionableFix.isNotBlank())
        }
    }

    @Test
    fun testRealtimeMemoryProbeInspection() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val probe = RealtimeMemoryProbe.inspect(context)

        assertTrue("Heap max must be positive", probe.heapMaxMb > 0)
        assertTrue("Heap used must be non-negative", probe.heapUsedMb >= 0)
        assertTrue("Active threads must be > 0", probe.activeThreadCount > 0)
    }

    @Test
    fun testAdversarialExtremePenaltiesSafety() {
        val extremeScore = ProjectHealthScore(
            baseScore = 50f,
            deadButtonsCount = 20,
            shallowFeaturesCount = 20
        )
        assertTrue("Score must coerce to >= 0", extremeScore.finalScore >= 0f)
        assertEquals(0f, extremeScore.finalScore, 0.01f)
    }
}
