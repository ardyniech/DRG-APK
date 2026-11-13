package com.example

import com.example.modules.devtools.CiCdStatusScanner
import com.example.modules.devtools.PhaseTreeScanner
import com.example.modules.devtools.PhaseTreeStatus
import com.example.modules.devtools.UiUxAuditEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevToolsPhaseTreeAndUiUxTest {

    @Test
    fun testPhaseTreeDefectPropagationHonesty() {
        val phases = PhaseTreeScanner.scanFullTree()
        assertEquals("Must contain 5 development phases", 5, phases.size)

        // Phase 1 has a defect in sub-phase 1.2 (Role validation client-side)
        val phase1 = phases.first { it.phaseNumber == 1 }
        assertTrue("Phase 1 must be flagged with defects", phase1.hasAnyDefect)
        assertEquals(PhaseTreeStatus.TERKENDALA_CACAT, phase1.overallStatus)

        // Phase 2: Asuransi is DEFERRED (not a defect/progress gap); events are still in progress
        val phase2 = phases.first { it.phaseNumber == 2 }
        assertEquals(1, phase2.deferredFeatures)
        assertTrue("Deferred must be excluded from phase completion denominator", phase2.inScopeFeatures < phase2.totalFeatures)
        assertEquals(PhaseTreeStatus.DALAM_PENGERJAAN, phase2.overallStatus)

        // Phase 3 has defects (PTT simulation) while Bluetooth intercom is DEFERRED
        val phase3 = phases.first { it.phaseNumber == 3 }
        assertTrue("Phase 3 must be flagged with defects", phase3.hasAnyDefect)
        assertEquals(PhaseTreeStatus.TERKENDALA_CACAT, phase3.overallStatus)
        assertEquals(1, phase3.deferredFeatures)

        // Check each leaf has what & why
        phases.forEach { phase ->
            phase.subPhases.forEach { sub ->
                sub.features.forEach { feat ->
                    assertTrue("What is it must be explained", feat.whatIsIt.isNotBlank())
                    assertTrue("Why required must be explained", feat.whyRequired.isNotBlank())
                    if (feat.hasDefect) {
                        assertNotNull("Defect explanation required if hasDefect", feat.defectExplanation)
                    }
                }
            }
        }
    }

    @Test
    fun testPhaseTreeMetricsIntegrity() {
        val m = PhaseTreeScanner.getMetrics()
        assertTrue("Total sub-features must be > 0", m.totalFeatures > 0)
        assertTrue("Completed count must be <= in-scope count", m.completedFeatures <= m.inScopeFeatures)
        assertTrue("Defect count must be > 0 due to identified flaws", m.defectFeatures > 0)
        assertTrue("Deferred must be tracked separately", m.deferredFeatures > 0)
        assertEquals("Total must equal in-scope + deferred", m.totalFeatures, m.inScopeFeatures + m.deferredFeatures)
        assertTrue("In-progress + defect + completed cannot exceed in-scope", m.completedFeatures + m.inProgressFeatures + m.defectFeatures <= m.inScopeFeatures)
        assertEquals(
            "Progress = lulus / in-scope",
            (m.completedFeatures.toFloat() / m.inScopeFeatures) * 100f,
            m.progressPercentage,
            0.01f
        )
        assertTrue("Progress must be within 0..100", m.progressPercentage in 0f..100f)
    }

    @Test
    fun testUiUxAuditEngineCalculations() {
        val audit = UiUxAuditEngine.auditExperience()
        assertTrue("UI/UX score must be between 0 and 100", audit.overallScore in 50f..100f)
        assertTrue("Must contain grade text", audit.grade.isNotBlank())
        assertEquals("Must assess 6 UI/UX pillars", 6, audit.pillars.size)

        audit.pillars.forEach { pillar ->
            assertTrue("Pillar title must be present", pillar.title.isNotBlank())
            assertTrue("Field observation must be present", pillar.fieldObservation.isNotBlank())
            assertTrue("Driver impact must be explained", pillar.driverImpact.isNotBlank())
            assertTrue("Remediation step must be provided", pillar.remediationStep.isNotBlank())
        }
    }

    @Test
    fun testCiCdPipelineInspectionReality() {
        val ci = CiCdStatusScanner.inspectPipeline()
        assertEquals("DRG Driver Android CI/CD", ci.workflowName)
        assertEquals(25, ci.timeoutMinutes)
        assertTrue("Concurrency cancel must be active", ci.isConcurrencyCancelled)
        assertEquals("Must monitor 7 pipeline steps", 7, ci.steps.size)

        val archStep = ci.steps.first { it.stepNumber == 1 }
        assertTrue(archStep.name.contains("Architecture"))
        assertTrue("Architecture step must be hard fail", archStep.failurePolicy.contains("HARD FAIL"))
    }
}
