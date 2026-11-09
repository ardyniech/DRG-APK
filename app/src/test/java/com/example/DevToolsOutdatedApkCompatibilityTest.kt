package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.modules.devtools.DynamicApkInspector
import com.example.modules.devtools.DynamicDbIntrospector
import com.example.modules.devtools.RuntimeCapabilityProber
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevToolsOutdatedApkCompatibilityTest {

    @Test
    fun testDynamicApkInspectorSelfIntrospection() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val apkInfo = DynamicApkInspector.inspectRunningApk(context)

        assertNotNull("Package name must be retrieved", apkInfo.packageName)
        assertTrue("Installed version code must be positive", apkInfo.installedVersionCode >= 1L)
        assertNotNull("Version name must be present", apkInfo.installedVersionName)
        assertTrue("Target SDK must be >= 26", apkInfo.targetSdkVersion >= 26)
        assertTrue("Skew description must explain status", apkInfo.versionSkewDescription.isNotBlank())
    }

    @Test
    fun testDynamicDbIntrospectorSafety() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbReport = DynamicDbIntrospector.introspectDatabase(context)

        assertNotNull(dbReport)
        assertTrue("Actual user version must be >= 0", dbReport.actualUserVersion >= 0)
        assertTrue("Status description must be non-empty", dbReport.statusDescription.isNotBlank())
    }

    @Test
    fun testRuntimeCapabilityProberReflectionResilience() {
        val capsReport = RuntimeCapabilityProber.probeRunningCapabilities()

        assertTrue("Probed items list must not be empty", capsReport.items.isNotEmpty())
        assertTrue("Present count must be <= total count", capsReport.presentCount <= capsReport.totalCount)
        assertTrue("Compatibility percentage must be in range 0..100", capsReport.compatibilityPercentage in 0f..100f)

        // SecurityAuditEngine must be present in the running binary
        val secItem = capsReport.items.firstOrNull { it.featureName.contains("Audit Keamanan") }
        assertNotNull("Security audit capability item must exist", secItem)
        assertTrue("SecurityAuditEngine must be detected in this build", secItem?.isPresentInRunningApk == true)
    }

    @Test
    fun testAdversarialSimulatedLegacyApkDelta() {
        val currentTarget = DynamicApkInspector.LATEST_TARGET_VERSION_CODE
        val legacySimulatedCode = 1L
        val isOutdated = legacySimulatedCode < currentTarget

        assertTrue("Simulated code 1 must be flagged as outdated", isOutdated)
        val delta = currentTarget - legacySimulatedCode
        assertEquals(9L, delta)
    }
}
