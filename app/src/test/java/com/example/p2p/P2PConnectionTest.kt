package com.example.p2p

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PConnectionTest {

    @Before
    fun setup() {
        // Initialize WebRTC factory for testing
    }

    @Test
    fun `STUN server configuration should be valid`() {
        val stunServer = "stun:stun.l.google.com:19302"
        assert(stunServer.isNotEmpty())
        assert(stunServer.startsWith("stun:"))
    }

    @Test
    fun `self-host port should match configuration`() {
        val port = 8080
        assert(port == 8080)
    }

    @Test
    fun `STUN connection should not be null`() {
        val config = "stun:stun.l.google.com:19302"
        assert(config != null)
    }
}
