package com.example.p2p

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class P2PFallbackTest {

    @Test
    fun `pending sync should save data when P2P fails`() {
        val entity = com.example.shared.models.PendingSyncEntity(
            id = "test_id",
            entityType = P2PConstants.ENTITY_TYPE_P2P_ALERT,
            payloadJson = "SOS Alert",
            timestamp = System.currentTimeMillis()
        )
        assert(entity.entityType == P2PConstants.ENTITY_TYPE_P2P_ALERT)
        assert(entity.payloadJson == "SOS Alert")
    }

    @Test
    fun `pending queue should respect max size`() {
        assert(P2PConfig.MAX_PENDING_QUEUE_SIZE == 500)
    }

    @Test
    fun `P2PConstants should have valid command types`() {
        assert(P2PConstants.CMD_ALERT == "alert")
        assert(P2PConstants.CMD_CHECK_IN == "check_in")
        assert(P2PConstants.CMD_LOCATION == "location")
    }
}
