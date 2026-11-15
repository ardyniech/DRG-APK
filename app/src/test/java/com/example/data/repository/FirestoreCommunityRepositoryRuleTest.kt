package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.core.firebase.FirestoreBackupSyncRepository
import com.example.core.firebase.FirestoreCommunityRepository
import com.example.core.firebase.FirestoreOperationsRepository
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.TransactionType
import com.example.shared.models.VerificationStatus
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class FirestoreCommunityRepositoryRuleTest : FirestoreEmulatorTestBase() {

    private fun sampleMember(uid: String) = DriverMember(
        id = uid,
        name = "Driver Test",
        driverId = "DRG-${uid.take(6)}",
        phone = "081234567890",
        role = MemberRole.ANGGOTA,
        motorcyclePlate = "N 1234 AB",
        motorcycleModel = "Vario 160",
        rating = 5.0f,
        reviewCount = 0,
        loyaltyPoints = 25,
        loyaltyTier = "Junior Rider",
        verificationStatus = VerificationStatus.PENDING_SCREENING,
        screeningNotes = "",
        isOnline = true,
        currentStatus = "Siaga",
        baseArea = "Malang Kota",
        nik = "3573010101900001"
    )

    @Test
    fun syncOwnProfile_andObserveRealtime_authenticatedUserSucceeds() = runBlocking {
        val uid = signInTestUser("alice_${UUID.randomUUID().toString().take(6)}@test.drg")
        val repo = FirestoreCommunityRepository(firestore, auth)

        val syncRes = withTimeout(TIMEOUT_MS) { repo.syncOwnProfile(sampleMember(uid), savePii = true) }
        assertTrue("Profile sync harus sukses: ${syncRes.exceptionOrNull()}", syncRes.isSuccess)

        val observed = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeOwnMemberProfile(uid).first { it != null && it.userId == uid }
        }
        assertNotNull(observed)
        assertEquals("ANGGOTA", observed?.role)
    }

    @Test
    fun addKasAndEmergencyAlert_authenticatedOwner_emitsRealtimeAndEnforcesTerminalLock() = runBlocking {
        val uid = signInTestUser("kas_${UUID.randomUUID().toString().take(6)}@test.drg")
        val ops = FirestoreOperationsRepository(firestore, auth)

        val txId = "tx_${UUID.randomUUID().toString().take(8)}"
        val kasRes = withTimeout(TIMEOUT_MS) {
            ops.addKasTransaction(txId, "Iuran Wajib", 20000L, TransactionType.INCOME, "IURAN_WAJIB", "Driver")
        }
        assertTrue(kasRes.isSuccess)

        val kasList = withTimeout(FLOW_TIMEOUT_MS) {
            ops.observeOwnKasTransactions(uid).first { list -> list.any { it.id == txId } }
        }
        assertTrue(kasList.any { it.id == txId && it.amount == 20000L })

        val alertId = "sos_${UUID.randomUUID().toString().take(8)}"
        assertTrue(withTimeout(TIMEOUT_MS) {
            ops.publishEmergencyAlert(alertId, "Driver", "DARURAT", "Butuh bantuan ban", "Sohat", -7.94, 112.61)
        }.isSuccess)

        val alerts = withTimeout(FLOW_TIMEOUT_MS) {
            ops.observeActiveAlerts().first { list -> list.any { it.id == alertId } }
        }
        assertTrue(alerts.any { it.id == alertId })

        assertTrue(withTimeout(TIMEOUT_MS) { ops.resolveEmergencyAlert(alertId) }.isSuccess)
    }

    @Test
    fun backupAndSyncQueue_ownerIsolatedFromCrossUser() = runBlocking {
        val aliceUid = signInTestUser("alice_b_${UUID.randomUUID().toString().take(6)}@test.drg")
        val aliceSyncRepo = FirestoreBackupSyncRepository(firestore, auth)
        val checksum = "b".repeat(64)
        val backupId = "bak_${UUID.randomUUID().toString().take(8)}"

        assertTrue(withTimeout(TIMEOUT_MS) {
            aliceSyncRepo.pushSyncQueueItem("sync_${UUID.randomUUID().toString().take(6)}", "KAS", "tx1", "{}")
        }.isSuccess)
        assertTrue(withTimeout(TIMEOUT_MS) {
            aliceSyncRepo.uploadEncryptedBackup(backupId, checksum, "YWJjMTIz", 14)
        }.isSuccess)
        val fetchedAlice = withTimeout(TIMEOUT_MS) { aliceSyncRepo.fetchLatestEncryptedBackup() }.getOrThrow()
        assertNotNull(fetchedAlice)
        assertEquals(checksum, fetchedAlice?.checksumSha256)

        val bobUid = signInTestUser("bob_b_${UUID.randomUUID().toString().take(6)}@test.drg")
        assertTrue(bobUid != aliceUid)
        val bobSyncRepo = FirestoreBackupSyncRepository(firestore, auth)
        val bobFetched = withTimeout(TIMEOUT_MS) { bobSyncRepo.fetchLatestEncryptedBackup() }.getOrThrow()
        assertNull("Bob tidak boleh melihat cadangan milik Alice", bobFetched)
    }

    @Test
    fun nonAdmin_cannotEscalateRole_getsPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser("nonadmin_${UUID.randomUUID().toString().take(6)}@test.drg")
        val repo = FirestoreCommunityRepository(firestore, auth)
        withTimeout(TIMEOUT_MS) { repo.syncOwnProfile(sampleMember(aliceUid)) }.getOrThrow()

        val result = withTimeout(TIMEOUT_MS) {
            repo.adminUpdateMemberRole(
                targetUid = aliceUid,
                previousRole = MemberRole.ANGGOTA,
                newRole = MemberRole.KETUA,
                newStatus = VerificationStatus.VERIFIED,
                reason = "Self escalation attempt"
            )
        }
        assertTrue("Non-admin harus ditolak mengubah role", result.isFailure)
        val fsErr = result.exceptionOrNull()?.unwrapFirestoreException()
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsErr?.code)
    }

    @Test
    fun observeOwnKasTransactions_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val ops = FirestoreOperationsRepository(firestore, auth)

        var caught: Exception? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) { ops.observeOwnKasTransactions("any_uid").first() }
        } catch (e: Exception) {
            caught = e
        }
        val fsErr = checkNotNull(caught?.unwrapFirestoreException()) {
            "Expected FirebaseFirestoreException wrapping PERMISSION_DENIED, got $caught"
        }
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, fsErr.code)
    }

    private companion object {
        const val TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 3000L
    }
}
