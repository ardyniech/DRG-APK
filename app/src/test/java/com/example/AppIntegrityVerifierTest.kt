package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.security.AppIntegrityVerifier
import com.example.modules.devtools.SecurityAuditEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppIntegrityVerifierTest {

    @Test
    fun `verify detects app metadata and runs cleanly without crash`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val report = AppIntegrityVerifier.verify(context)
        assertFalse("Unit test sandbox should not report hooking artifacts", report.hasHookingArtifacts)
        assertEquals(false, report.isTrustedInstaller)
        val checks = SecurityAuditEngine.audit(context)
        assertTrue(checks.any { it.name.contains("Anti-Tamper Integrity") })
    }
}
