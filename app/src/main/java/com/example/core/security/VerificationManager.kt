package com.example.core.security

import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus

class VerificationManager {

    fun isMemberVerified(member: DriverMember?): Boolean {
        if (member == null) return false
        return member.isVerified && member.verificationStatus == VerificationStatus.VERIFIED
    }

    fun canPerformVerifiedActions(member: DriverMember?): Boolean {
        return isMemberVerified(member)
    }

    fun evaluateDocumentSubmission(
        simNumber: String,
        nik: String,
        selfieProvided: Boolean
    ): VerificationStatus {
        if (simNumber.isBlank() || nik.isBlank() || !selfieProvided) {
            return VerificationStatus.REJECTED
        }
        return VerificationStatus.PENDING_SCREENING
    }

    fun approveMember(member: DriverMember): DriverMember {
        return member.copy(
            verificationStatus = VerificationStatus.VERIFIED,
            isVerified = true,
            screeningNotes = "Dokumen terverifikasi oleh Admin / Dewan Etika DRG"
        )
    }

    fun rejectMember(member: DriverMember, rejectionReason: String): DriverMember {
        return member.copy(
            verificationStatus = VerificationStatus.REJECTED,
            isVerified = false,
            screeningNotes = "Ditolak: $rejectionReason"
        )
    }
}
