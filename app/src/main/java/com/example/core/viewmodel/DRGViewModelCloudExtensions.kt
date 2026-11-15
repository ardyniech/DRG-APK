package com.example.core.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.core.firebase.FirestoreBackupSyncRepository
import com.example.core.firebase.FirestoreCommunityRepository
import com.example.core.firebase.FirestoreOperationsRepository
import com.example.shared.models.*
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

/**
 * Ekstensi sinkronisasi Cloud Firestore untuk DRGViewModel (< 125 lines).
 */
internal fun createCloudBackupSyncRepo(context: Context): FirestoreBackupSyncRepository? =
    if (FirebaseApp.getApps(context).isNotEmpty()) runCatching { FirestoreBackupSyncRepository(context) }.getOrNull() else null

internal suspend fun uploadPendingSyncItem(context: Context, item: PendingSyncEntity): Result<Unit> {
    if (FirebaseApp.getApps(context).isEmpty() || Firebase.auth.currentUser == null) {
        return Result.failure(IllegalStateException("Belum masuk akun Google"))
    }
    val repo = createCloudBackupSyncRepo(context) ?: return Result.failure(IllegalStateException("Firestore belum siap"))
    return repo.pushSyncQueueItem(item.id, item.entityType, item.id, item.payloadJson)
}

internal fun createCloudCommunityRepo(context: Context): FirestoreCommunityRepository? =
    if (FirebaseApp.getApps(context).isNotEmpty()) runCatching { FirestoreCommunityRepository(context) }.getOrNull() else null

internal fun createCloudOpsRepo(context: Context): FirestoreOperationsRepository? =
    if (FirebaseApp.getApps(context).isNotEmpty()) runCatching { FirestoreOperationsRepository(context) }.getOrNull() else null

fun DRGViewModel.completeGoogleSignIn(user: FirebaseUser) {
    profileCoord.loginWithGoogleUser(user) { member ->
        syncOwnProfileToCloud(member)
        syncEngine.triggerBackgroundSync(isPowerSaverMode.value)
    }
}

fun DRGViewModel.logoutSession(context: Context) = profileCoord.logout(context)

fun DRGViewModel.syncOwnProfileToCloud(member: DriverMember? = currentMember.value) {
    if (member == null || Firebase.auth.currentUser == null) return
    val repo = createCloudCommunityRepo(getApplication()) ?: return
    viewModelScope.launch { repo.syncOwnProfile(member, savePii = true) }
}

fun DRGViewModel.syncKasTransactionToCloud(tx: KasTransaction) {
    if (Firebase.auth.currentUser == null) return
    val repo = createCloudOpsRepo(getApplication()) ?: return
    viewModelScope.launch {
        repo.addKasTransaction(tx.id, tx.title, tx.amount, tx.type, tx.category.name, tx.recordedBy)
    }
}

fun DRGViewModel.syncEmergencyToCloud(driverName: String, alertType: String, message: String, location: String, lat: Double?, lng: Double?) {
    if (Firebase.auth.currentUser == null) return
    val repo = createCloudOpsRepo(getApplication()) ?: return
    viewModelScope.launch {
        repo.publishEmergencyAlert(driverName = driverName, alertType = alertType, message = message, locationName = location, lat = lat, lng = lng)
    }
}

fun DRGViewModel.syncAdminRoleChangeToCloud(
    targetUid: String,
    previousRole: MemberRole,
    newRole: MemberRole,
    status: VerificationStatus,
    reason: String
) {
    if (Firebase.auth.currentUser == null) return
    val repo = createCloudCommunityRepo(getApplication()) ?: return
    viewModelScope.launch {
        repo.adminUpdateMemberRole(targetUid, previousRole, newRole, status, reason)
            .onFailure { showToast("Audit role cloud tertunda: ${it.localizedMessage}") }
    }
}
