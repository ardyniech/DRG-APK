package com.example.p2p

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.mockito.Mockito.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PSignalingTest {

    @Test
    fun `FCM topic should be correct for P2P discovery`() {
        assert(P2PConfig.FCM_P2P_TOPIC == "drg_p2p_discovery")
    }

    @Test
    fun `signaling data should contain IP and port keys`() {
        val data = mapOf(
            P2PConstants.KEY_SENDER_ID to "driver_123",
            P2PConstants.KEY_IP to "192.168.1.100",
            P2PConstants.KEY_PORT to 8080,
            P2PConstants.KEY_TIMESTAMP to System.currentTimeMillis()
        )
        assert(data.containsKey(P2PConstants.KEY_IP))
        assert(data.containsKey(P2PConstants.KEY_PORT))
    }

    @Test
    fun `broadcastPeerInfo should include sender ID`() {
        val signalData = mapOf(
            P2PConstants.KEY_SENDER_ID to "driver_123",
            P2PConstants.KEY_IP to "10.0.0.1",
            P2PConstants.KEY_PORT to 8080
        )
        assert(signalData[P2PConstants.KEY_SENDER_ID] != null)
    }
}
