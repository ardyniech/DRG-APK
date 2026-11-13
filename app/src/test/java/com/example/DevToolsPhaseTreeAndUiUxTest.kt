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

        // Phase 3 has defects (P2P WiFi subnet & Bluetooth pairing)
        val phase3 = phases.first { it.phaseNumber == 3 }
        assertTrue("Phase 3 must be flagged with defects", phase3.hasAnyDefect)
        assertEquals(PhaseTreeStatus.TERKENDALA_CACAT, phase3.overallStatus)

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
        val (total, completed, defect) = PhaseTreeScanner.getTotalMetrics()
        assertTrue("Total sub-features must be > 0", total > 0)
        assertTrue("Completed count must be <= total", completed <= total)
        assertTrue("Defect count must be > 0 due to identified flaws", defect > 0)
        val remaining = total - (completed + defect)
        assertTrue("Remaining uncompleted/pending features must be positive", remaining > 0)
        assertEquals("Completed + Pending/Defects must reconcile", total, completed + defect + remaining)
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
