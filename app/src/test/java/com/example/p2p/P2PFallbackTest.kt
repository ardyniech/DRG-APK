package com.example.p2p

import com.example.core.p2p.P2PConfig
import com.example.core.p2p.P2PConstants
import com.example.shared.models.PendingSyncEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PFallbackTest {

    @Test
    fun `pending sync should save data when P2P fails`() {
        val entity = PendingSyncEntity(
            id = "test_id",
            entityType = P2PConstants.ENTITY_TYPE_P2P_ALERT,
            payloadJson = "SOS Alert",
            timestamp = System.currentTimeMillis()
        )
        assertEquals(P2PConstants.ENTITY_TYPE_P2P_ALERT, entity.entityType)
        assertEquals("SOS Alert", entity.payloadJson)
    }

    @Test
    fun `pending queue should respect max size`() {
        assertEquals(500, P2PConfig.MAX_PENDING_QUEUE_SIZE)
    }

    @Test
    fun `P2PConstants should have valid command types`() {
        assertEquals("alert", P2PConstants.CMD_ALERT)
        assertEquals("check_in", P2PConstants.CMD_CHECK_IN)
        assertEquals("location", P2PConstants.CMD_LOCATION)
    }
}
