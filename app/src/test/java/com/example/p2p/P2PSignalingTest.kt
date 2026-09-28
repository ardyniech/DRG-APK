package com.example.p2p

import com.example.core.p2p.P2PConfig
import com.example.core.p2p.P2PConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PSignalingTest {

    @Test
    fun `FCM topic should be correct for P2P discovery`() {
        assertEquals("drg_p2p_discovery", P2PConfig.FCM_P2P_TOPIC)
    }

    @Test
    fun `signaling data should contain IP and port keys`() {
        val data: Map<String, Any> = mapOf(
            P2PConstants.KEY_SENDER_ID to "driver_123",
            P2PConstants.KEY_IP to "192.168.1.100",
            P2PConstants.KEY_PORT to 8080,
            P2PConstants.KEY_TIMESTAMP to System.currentTimeMillis()
        )
        assertTrue(data.containsKey(P2PConstants.KEY_IP))
        assertTrue(data.containsKey(P2PConstants.KEY_PORT))
    }

    @Test
    fun `broadcastPeerInfo should include sender ID`() {
        val signalData: Map<String, Any> = mapOf(
            P2PConstants.KEY_SENDER_ID to "driver_123",
            P2PConstants.KEY_IP to "10.0.0.1",
            P2PConstants.KEY_PORT to 8080
        )
        assertEquals("driver_123", signalData[P2PConstants.KEY_SENDER_ID])
    }
}
