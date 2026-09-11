package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "member_role_permissions",
    indices = [
        Index(value = ["role"]),
        Index(value = ["verificationStatus"])
    ]
)
data class MemberRolePermissionEntity(
    @PrimaryKey val memberId: String,
    val role: MemberRole,
    val assignedByMemberId: String = "SYSTEM",
    val assignedByMemberName: String = "Sistem DRG",
    val assignedTimestamp: Long = System.currentTimeMillis(),
    val canManageKas: Boolean = false,
    val canVerifyDrivers: Boolean = false,
    val canBroadcastSos: Boolean = false,
    val canManagePosko: Boolean = false,
    val verificationStatus: VerificationStatus = VerificationStatus.VERIFIED,
    val notes: String = ""
)
