package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.core.firebase.FirestoreP2pSignalingRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class FirestoreP2pPttRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun peerDiscovery_andWebRtcSignaling_respectsParticipantIsolation() = runBlocking {
        val suffix = UUID.randomUUID().toString().take(6)
        val aliceUid = signInTestUser("p2p_alice_$suffix@test.drg")
        val aliceRepo = FirestoreP2pSignalingRepository(firestore, auth)

        val channel = "suhat_$suffix"
        val regRes = withTimeout(TIMEOUT_MS) {
            aliceRepo.registerOnlinePeer("Alice Rider", channel, "10.0.0.5", 8080, "v=0 offer")
        }
        assertTrue("Register peer harus sukses: ${regRes.exceptionOrNull()}", regRes.isSuccess)

        val bobUid = signInTestUser("p2p_bob_$suffix@test.drg")
        val bobRepo = FirestoreP2pSignalingRepository(firestore, auth)

        val onlinePeers = withTimeout(FLOW_TIMEOUT_MS) {
            bobRepo.observeOnlinePeers(channel).first { list -> list.any { it.userId == aliceUid } }
        }
        assertTrue(onlinePeers.any { it.userId == aliceUid && it.sdpOffer == "v=0 offer" })

        signInTestUser("p2p_alice_$suffix@test.drg")
        val sigId = "sig_${UUID.randomUUID().toString().take(8)}"
        assertTrue(
            withTimeout(TIMEOUT_MS) {
                aliceRepo.sendSignal(bobUid, channel, "OFFER", "sdp-offer-alice", sigId)
            }.isSuccess
        )

        // Alice cannot consume Bob's signal
        val spoofConsume = withTimeout(TIMEOUT_MS) { aliceRepo.markSignalConsumed(sigId) }
        assertTrue(spoofConsume.isFailure)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, spoofConsume.exceptionOrNull()?.unwrapFirestoreException()?.code)

        // Bob reads incoming signal & marks consumed
        signInTestUser("p2p_bob_$suffix@test.drg")
        val incoming = withTimeout(FLOW_TIMEOUT_MS) {
            bobRepo.observeIncomingSignals(bobUid).first { list -> list.any { it.id == sigId } }
        }
        assertEquals("OFFER", incoming.first { it.id == sigId }.signalType)
        assertTrue(withTimeout(TIMEOUT_MS) { bobRepo.markSignalConsumed(sigId) }.isSuccess)

        signInTestUser("p2p_alice_$suffix@test.drg")
        assertTrue(withTimeout(TIMEOUT_MS) { aliceRepo.goOfflinePeer() }.isSuccess)
    }

    @Test
    fun pttTransmission_publishObserveAndOwnerEndLock_enforced() = runBlocking {
        val suffix = UUID.randomUUID().toString().take(6)
        val aliceUid = signInTestUser("ptt_alice_$suffix@test.drg")
        val aliceRepo = FirestoreP2pSignalingRepository(firestore, auth)
        val channel = "klojen_$suffix"
        val txId = "ptt_${UUID.randomUUID().toString().take(8)}"

        val pubRes = withTimeout(TIMEOUT_MS) {
            aliceRepo.publishPttTransmission("Alice", channel, "f39/f38=", 1250L, txId)
        }
        assertTrue("Publish PTT harus sukses: ${pubRes.exceptionOrNull()}", pubRes.isSuccess)

        signInTestUser("ptt_bob_$suffix@test.drg")
        val bobRepo = FirestoreP2pSignalingRepository(firestore, auth)
        val liveList = withTimeout(FLOW_TIMEOUT_MS) {
            bobRepo.observeLivePttChannel(channel).first { list -> list.any { it.id == txId } }
        }
        assertEquals(aliceUid, liveList.first { it.id == txId }.userId)

        // Bob cannot end Alice's PTT transmission
        val bobEnd = withTimeout(TIMEOUT_MS) { bobRepo.endPttTransmission(txId) }
        assertTrue(bobEnd.isFailure)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, bobEnd.exceptionOrNull()?.unwrapFirestoreException()?.code)

        // Alice ends her PTT; replay/update after ENDED is denied
        signInTestUser("ptt_alice_$suffix@test.drg")
        assertTrue(withTimeout(TIMEOUT_MS) { aliceRepo.endPttTransmission(txId) }.isSuccess)
        val secondEnd = withTimeout(TIMEOUT_MS) { aliceRepo.endPttTransmission(txId) }
        assertTrue(secondEnd.isFailure)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, secondEnd.exceptionOrNull()?.unwrapFirestoreException()?.code)
    }

    @Test
    fun observeLivePttChannel_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repo = FirestoreP2pSignalingRepository(firestore, auth)
        var caught: Exception? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) { repo.observeLivePttChannel("suhat").first() }
        } catch (e: Exception) {
            caught = e
        }
        val fsErr = checkNotNull(caught?.unwrapFirestoreException()) { "Expected PERMISSION_DENIED, got $caught" }
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsErr.code)
    }

    private companion object {
        const val TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
