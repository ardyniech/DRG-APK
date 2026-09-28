package com.example.p2p

import com.example.core.p2p.P2PConfig
import com.example.core.p2p.P2PConnectionManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PConnectionTest {

    @Test
    fun `STUN server configuration should be valid`() {
        val stunServer = P2PConfig.STUN_SERVER
        assertNotNull(stunServer)
    }

    @Test
    fun `self-host port should match configuration`() {
        assertEquals(8080, P2PConfig.SELF_HOST_PORT)
    }

    @Test
    fun `P2PConnectionManager can be instantiated`() {
        val manager = P2PConnectionManager()
        assertNotNull(manager)
    }
}
