package com.example.shared.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class CloudUserAccount(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String? = null,
    val driverId: String? = null,
    val role: String = "ANGGOTA",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudPrivatePii(
    @DocumentId val id: String = "",
    val userId: String = "",
    val phone: String? = null,
    val address: String? = null,
    val bloodType: String? = null,
    val emergencyContact: String? = null,
    val emergencyPhone: String? = null,
    val simNumber: String? = null,
    val nik: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudEncryptedBackup(
    @DocumentId val id: String = "",
    val userId: String = "",
    val checksumSha256: String = "",
    val encryptedPayload: String = "",
    val schemaVersion: Long = 14L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudSyncItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val entityType: String = "",
    val entityId: String = "",
    val payload: String? = null,
    val syncState: String = "SYNCED",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudCommunityMember(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val driverId: String = "",
    val baseArea: String? = null,
    val motorcycleModel: String? = null,
    val motorcyclePlate: String? = null,
    val role: String = "ANGGOTA",
    val verificationStatus: String = "PENDING_SCREENING",
    val loyaltyPoints: Long = 0L,
    val isOnline: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudKasTransaction(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val amount: Long = 0L,
    val type: String = "INCOME",
    val category: String = "IURAN_WAJIB",
    val recordedBy: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudEmergencyAlert(
    @DocumentId val id: String = "",
    val userId: String = "",
    val driverName: String = "",
    val alertType: String = "DARURAT",
    val message: String? = null,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: String = "ACTIVE",
    val respondersCount: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CloudRoleAuditLog(
    @DocumentId val id: String = "",
    val actorUid: String = "",
    val targetUserId: String = "",
    val previousRole: String = "ANGGOTA",
    val newRole: String = "ANGGOTA",
    val reason: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
