package com.example.core.firebase

import com.example.core.database.AppDatabase
import com.example.shared.models.CloudCommunityMember
import com.example.shared.models.CloudEmergencyAlert
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.EmergencyType
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

/**
 * Menyelaraskan snapshot Firestore ke Room DB secara aman (2-way sync < 125 baris).
 */
object FirestoreCloudSyncMapper {

    suspend fun syncMemberToRoom(
        db: AppDatabase,
        cloud: CloudCommunityMember?,
        localMemberId: String
    ) = withContext(Dispatchers.IO) {
        if (cloud == null || localMemberId.isBlank()) return@withContext
        val existing = db.memberDao().getMemberById(localMemberId) ?: return@withContext
        val resolvedRole = runCatching { MemberRole.valueOf(cloud.role) }.getOrDefault(existing.role)
        val resolvedStatus = runCatching { VerificationStatus.valueOf(cloud.verificationStatus) }
            .getOrDefault(existing.verificationStatus)
        val updated = existing.copy(
            name = cloud.name.ifBlank { existing.name },
            baseArea = cloud.baseArea?.ifBlank { existing.baseArea } ?: existing.baseArea,
            motorcycleModel = cloud.motorcycleModel?.ifBlank { existing.motorcycleModel } ?: existing.motorcycleModel,
            motorcyclePlate = cloud.motorcyclePlate?.ifBlank { existing.motorcyclePlate } ?: existing.motorcyclePlate,
            role = resolvedRole,
            verificationStatus = resolvedStatus,
            isVerified = resolvedStatus == VerificationStatus.VERIFIED,
            loyaltyPoints = maxOf(existing.loyaltyPoints, cloud.loyaltyPoints.toInt()),
            isOnline = cloud.isOnline
        )
        if (updated != existing) db.memberDao().updateMember(updated)
    }

    suspend fun syncActiveAlertsToRoom(
        db: AppDatabase,
        cloudAlerts: List<CloudEmergencyAlert>
    ) = withContext(Dispatchers.IO) {
        val mapped = cloudAlerts.filter { it.id.isNotBlank() && it.status == "ACTIVE" }.map { cloud ->
            val type = runCatching { EmergencyType.valueOf(cloud.alertType) }.getOrDefault(EmergencyType.MOGOK)
            EmergencyAlert(
                id = cloud.id,
                driverId = cloud.userId,
                driverName = cloud.driverName.ifBlank { "Driver DRG" },
                driverPhone = "",
                plateNumber = "",
                type = type,
                message = cloud.message.orEmpty(),
                lat = cloud.latitude ?: -7.9822,
                lng = cloud.longitude ?: 112.6303,
                locationName = cloud.locationName ?: "Malang Raya",
                timestamp = cloud.createdAt?.toDate()?.time ?: System.currentTimeMillis(),
                isActive = true,
                responderCount = cloud.respondersCount.toInt()
            )
        }
        if (mapped.isNotEmpty()) db.emergencyDao().insertAlerts(mapped)

        val activeIds = mapped.map { it.id }.toSet()
        val localActive = db.emergencyDao().getActiveAlerts().firstOrNull().orEmpty()
        localActive
            .filter { (it.id.startsWith("SOS-") || it.id.startsWith("sos_")) && it.id !in activeIds }
            .forEach { stale -> db.emergencyDao().resolveAlert(stale.id, "Cloud Sync") }
    }
}
