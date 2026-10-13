package com.example

import com.example.core.security.VerificationManager
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class VerificationManagerTest {

    private val manager = VerificationManager()

    @Test
    fun `test member verification status evaluation`() {
        val member = DriverMember(
            id = "M-01",
            name = "Budi Santoso",
            driverId = "DRG-001",
            phone = "08123456789",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1234 AB",
            motorcycleModel = "Honda Beat",
            verificationStatus = VerificationStatus.VERIFIED,
            isVerified = true,
            nik = "3507123456780001",
            simNumber = "1234-5678-9012"
        )

        assertTrue(manager.isMemberVerified(member))
        assertTrue(manager.canPerformVerifiedActions(member))
    }

    @Test
    fun `test bad input evaluation and rejection`() {
        val status = manager.evaluateDocumentSubmission("", "", false)
        assertEquals(VerificationStatus.REJECTED, status)

        val member = DriverMember(
            id = "M-02",
            name = "Siti Rahma",
            driverId = "DRG-002",
            phone = "08123456788",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 5678 CD",
            motorcycleModel = "Yamaha Mio",
            verificationStatus = VerificationStatus.PENDING_SCREENING,
            isVerified = false,
            nik = "",
            simNumber = ""
        )

        val rejected = manager.rejectMember(member, "KTP buram")
        assertEquals(VerificationStatus.REJECTED, rejected.verificationStatus)
        assertFalse(rejected.isVerified)
        assertTrue(rejected.screeningNotes.contains("Ditolak"))
    }

    @Test
    fun `test member approval workflow`() {
        val pending = DriverMember(
            id = "M-03",
            name = "Joko Widodo",
            driverId = "DRG-003",
            phone = "08123456787",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 9999 XX",
            motorcycleModel = "Suzuki Nex",
            verificationStatus = VerificationStatus.PENDING_SCREENING,
            isVerified = false,
            nik = "3507999999990001",
            simNumber = "9876-5432-1010"
        )

        val approved = manager.approveMember(pending)
        assertEquals(VerificationStatus.VERIFIED, approved.verificationStatus)
        assertTrue(approved.isVerified)
        assertTrue(manager.isMemberVerified(approved))
    }

    @Test
    fun `test null member handling`() {
        assertFalse(manager.isMemberVerified(null))
        assertFalse(manager.canPerformVerifiedActions(null))
    }
}
