package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.modules.devtools.DefectType
import com.example.modules.devtools.DeviceAdvisorEngine
import com.example.modules.devtools.NodeTopologyScanner
import com.example.modules.devtools.ProductionDefectScanner
import com.example.modules.devtools.SecurityAuditEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevToolsNextGenAuditTest {

    @Test
    fun testSecurityAuditVectorsCompleteness() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val results = SecurityAuditEngine.audit(context)

        assertTrue("Security audit must return at least 5 audit vectors", results.size >= 5)
        val vectorTitles = results.map { it.name }
        assertTrue(vectorTitles.any { it.contains("Root") })
        assertTrue(vectorTitles.any { it.contains("Enkripsi") || it.contains("TLS") })
        assertTrue(vectorTitles.any { it.contains("Keystore") })
        assertTrue(vectorTitles.any { it.contains("Sandbox") })
        assertTrue(vectorTitles.any { it.contains("SOS") })

        results.forEach { item ->
            assertTrue("Name must be present", item.name.isNotBlank())
            assertTrue("Category must be present", item.category.isNotBlank())
            assertTrue("Status text must be present", item.statusText.isNotBlank())
        }
    }

    @Test
    fun testNodeTopologyClusterOnlineAndOffline() {
        val onlineNodes = NodeTopologyScanner.getClusterNodes(isNetworkOnline = true)
        assertEquals("Cluster must track 5 core nodes", 5, onlineNodes.size)

        val sqliteNode = onlineNodes.first { it.nodeName.contains("SQLite") }
        assertTrue("SQLite node is always connected", sqliteNode.isConnected)

        val offlineNodes = NodeTopologyScanner.getClusterNodes(isNetworkOnline = false)
        val cloudNode = offlineNodes.first { it.nodeName.contains("Firestore") }
        assertTrue("Cloud node must reflect offline state", !cloudNode.isConnected)
    }

    @Test
    fun testProductionDefectHonestDisclosure() {
        val defects = ProductionDefectScanner.scanKnownDefects()
        assertTrue("Must contain known defects", defects.isNotEmpty())

        val conceptFlaws = defects.filter { it.type == DefectType.CONCEPT_FLAW }
        val prodFlaws = defects.filter { it.type == DefectType.PRODUCTION_DEFECT }

        assertTrue("Must disclose concept flaws", conceptFlaws.isNotEmpty())
        assertTrue("Must disclose production flaws", prodFlaws.isNotEmpty())

        defects.forEach { defect ->
            assertTrue(defect.affectedComponent.isNotBlank())
            assertTrue(defect.honestDefectExplanation.isNotBlank())
            assertTrue(defect.engineeringRoadmapFix.isNotBlank())
        }
    }

    @Test
    fun testDeviceOptimizationAdvisorGeneratesAdvice() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val adviceList = DeviceAdvisorEngine.evaluateDevice(context)

        assertNotNull(adviceList)
        assertTrue("Advice list must not be empty", adviceList.isNotEmpty())
        adviceList.forEach { advice ->
            assertTrue("Advice title must be non-empty", advice.title.isNotBlank())
            assertTrue("Priority must be non-empty", advice.priority.isNotBlank())
            assertTrue("Metric must be non-empty", advice.currentDeviceMetric.isNotBlank())
            assertTrue("Advice text must be non-empty", advice.practicalOjolAdvice.isNotBlank())
        }
    }

    @Test
    fun testAdversarialOfflineDeviceAdvisorSafety() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val adviceList = DeviceAdvisorEngine.evaluateDevice(context)

        adviceList.forEach { advice ->
            assertTrue("Advice title length must be reasonable", advice.title.length > 3)
            assertTrue("Practical advice must provide guidance", advice.practicalOjolAdvice.length > 10)
        }
    }
}
