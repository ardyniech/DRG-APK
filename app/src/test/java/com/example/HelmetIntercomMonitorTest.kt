package com.example

import androidx.test.core.app.ApplicationProvider
import com.example.core.audio.HelmetIntercomMonitor
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HelmetIntercomMonitorTest {

    @Test
    fun testMonitorInitialization() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val monitor = HelmetIntercomMonitor(context)
        assertNotNull(monitor.isIntercomConnected.value)

        var disconnectMsg = ""
        monitor.startMonitoring { msg -> disconnectMsg = msg }
        monitor.stopMonitoring()
        assertTrue(disconnectMsg.isEmpty())
    }
}
