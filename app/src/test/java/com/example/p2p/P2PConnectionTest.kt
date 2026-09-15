package com.example.p2p

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.mockito.Mockito.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PConnectionTest {

    @Test
    fun `STUN server configuration should be valid`() {
        val stunServer = P2PConfig.STUN_SERVER
        assert(stunServer != null)
    }

    @Test
    fun `self-host port should match configuration`() {
        assert(P2PConfig.SELF_HOST_PORT == 8080)
    }

    @Test
    fun `P2PConnectionManager should create peer connection`() {
        val manager = com.example.core.p2p.P2PConnectionManager()
        val factory = mock(org.webrtc.PeerConnectionFactory::class.java)
        val connection = manager.createPeerConnection(factory)
        assert(connection != null)
    }
}
