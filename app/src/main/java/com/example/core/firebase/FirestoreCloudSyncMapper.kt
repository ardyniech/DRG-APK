package com.example.core.firebase

import com.example.core.database.AppDatabase
import com.example.core.sync.CrdtRegister
import com.example.core.sync.LwwCrdtReconciler
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

/**
 * Menyelaraskan snapshot Firestore ke Room DB secara aman + rekonsiliasi LWW CRDT (< 125 baris).
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
        val localActive = db.emergencyDao().getActiveAlerts().firstOrNull().orEmpty()
        val localRegs = localActive.associate { it.id to CrdtRegister(it, it.timestamp, "local") }
        val cloudRegs = cloudAlerts.filter { it.id.isNotBlank() && it.status == "ACTIVE" }.associate { cloud ->
            val ts = (cloud.updatedAt ?: cloud.createdAt)?.toDate()?.time ?: System.currentTimeMillis()
            val alert = EmergencyAlert(
                id = cloud.id,
                driverId = cloud.userId,
                driverName = cloud.driverName.ifBlank { "Driver DRG" },
                driverPhone = "",
                plateNumber = "",
                type = runCatching { EmergencyType.valueOf(cloud.alertType) }.getOrDefault(EmergencyType.MOGOK),
                message = cloud.message.orEmpty(),
                lat = cloud.latitude ?: -7.9822,
                lng = cloud.longitude ?: 112.6303,
                locationName = cloud.locationName ?: "Malang Raya",
                timestamp = ts,
                isActive = true,
                responderCount = cloud.respondersCount.toInt()
            )
            cloud.id to CrdtRegister(alert, ts, "cloud:${cloud.userId}")
        }
        val reconciled = LwwCrdtReconciler.activeValues(LwwCrdtReconciler.mergeMap(localRegs, cloudRegs)).values.toList()
        if (reconciled.isNotEmpty()) db.emergencyDao().insertAlerts(reconciled)

        val activeIds = cloudRegs.keys
        localActive
            .filter { (it.id.startsWith("SOS-") || it.id.startsWith("sos_")) && it.id !in activeIds }
            .forEach { stale -> db.emergencyDao().resolveAlert(stale.id, "Cloud Sync") }
    }
}
