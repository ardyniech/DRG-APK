package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.modules.devtools.DefectType
import com.example.modules.devtools.MultiUserGapScanner
import com.example.modules.devtools.NodeTopologyScanner
import com.example.modules.devtools.ProductionDefectScanner
import com.example.modules.devtools.SecurityAuditEngine
import com.example.modules.devtools.ServerOverloadMonitor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevToolsMultiUserGapTest {

    @Test
    fun testExtraDefectsContainMultiUserBlockers() {
        val extras = MultiUserGapScanner.extraDefects()
        assertEquals(6, extras.size)
        val ids = extras.map { it.defectId }
        assertTrue(ids.containsAll(listOf("DEFECT-05", "DEFECT-06", "DEFECT-07")))
        // fake-sync, firebase, auth-lokal, p2p log-only wajib ada
        assertTrue(extras.any { it.title.contains("Cloud") })
        assertTrue(extras.any { it.title.contains("Firebase") })
        assertTrue(extras.any { it.title.contains("P2P") })
        extras.forEach { d ->
            assertTrue(d.honestDefectExplanation.isNotBlank())
            assertTrue(d.engineeringRoadmapFix.isNotBlank())
        }
    }

    @Test
    fun testCombinedScannerTotal10AndNoMarketplaceBlocker() {
        val all = ProductionDefectScanner.scanKnownDefects()
        assertEquals(10, all.size)
        assertEquals(5, all.count { it.type == DefectType.CONCEPT_FLAW })
        assertEquals(5, all.count { it.type == DefectType.PRODUCTION_DEFECT })
        // finansial deferred: marketplace/arisan TIDAK boleh jadi defect blocker
        assertFalse(all.any { it.title.contains("Marketplace", ignoreCase = true) })
        assertFalse(all.any { it.title.contains("Arisan", ignoreCase = true) })
    }

    @Test
    fun testNodeTopologyHonestCloudDisconnected() {
        val online = NodeTopologyScanner.getClusterNodes(isNetworkOnline = true)
        assertEquals(5, online.size)
        val cloud = online.first { it.nodeName.contains("Firestore") }
        assertFalse("Firestore belum di-enable harus disconnected walau online", cloud.isConnected)
        assertTrue(cloud.syncState.contains("Belum di-enable"))
    }

    @Test
    fun testSecurityAuditKeystoreNotSecure() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        val results = SecurityAuditEngine.audit(ctx)
        val keystore = results.first { it.name.contains("Keystore") }
        assertFalse("PIN SHA-256 SharedPrefs bukan Keystore — harus Kurang Aman", keystore.isSecure)
        assertTrue(keystore.statusText.contains("Kurang Aman"))
    }

    @Test
    fun testServerOverloadUnknownWithoutBackend() {
        val status = ServerOverloadMonitor.inspectServerLoad()
        assertTrue(status.loadDescription.contains("UNKNOWN"))
        assertEquals(0, status.concurrentUsersEstimate)
    }
}
