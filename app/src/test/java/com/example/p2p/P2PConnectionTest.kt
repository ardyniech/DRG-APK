package com.example.p2p

import com.example.core.p2p.P2PConfig
import com.example.core.p2p.P2PConnectionManager
import com.example.core.p2p.P2PSelfHostServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.Socket

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

    @Test
    fun `self-host server binds ephemeral socket and responds to peer ping`() {
        val server = P2PSelfHostServer(port = 0)
        server.start()
        try {
            val deadline = System.currentTimeMillis() + 3000L
            while (server.boundPort <= 0 && System.currentTimeMillis() < deadline) {
                Thread.sleep(25)
            }
            assertTrue("Server socket harus bind ke port aktif", server.boundPort > 0)
            Socket("127.0.0.1", server.boundPort).use { client ->
                client.soTimeout = 2000
                client.getOutputStream().write("GET /ping HTTP/1.1\r\n\r\n".toByteArray())
                client.getOutputStream().flush()
                val response = client.getInputStream().bufferedReader().readText()
                assertTrue(response.contains("200 OK"))
                assertTrue(response.contains("drg-p2p-host"))
            }
        } finally {
            server.stop()
            assertFalse(server.isActive())
        }
    }
}
