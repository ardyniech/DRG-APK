package com.example.core.firebase

import android.content.Context
import com.example.R
import com.example.shared.models.*
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreCommunityRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(context.applicationContext.getString(R.string.firestore_database_id))
    )

    private fun requireUid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Pengguna belum masuk dengan akun Google.")

    suspend fun syncOwnProfile(member: DriverMember, savePii: Boolean = true): Result<Unit> = runCatching {
        val uid = requireUid()
        val currentUser = auth.currentUser
        val isBootAdmin = currentUser?.isEmailVerified == true && currentUser.email == "ardy.syafii@gmail.com"
        val initialRole = if (isBootAdmin) MemberRole.SUPER_ADMIN.name else MemberRole.ANGGOTA.name
        val initialStatus = if (isBootAdmin) VerificationStatus.VERIFIED.name else VerificationStatus.PENDING_SCREENING.name
        val displayName = member.name.ifBlank { currentUser?.displayName ?: "Driver DRG" }.take(80)
        val safeDriverId = member.driverId.ifBlank { "DRG-${uid.take(8).uppercase()}" }.take(48)

        upsertDoc("users/$uid", mapOf(
            "userId" to uid, "displayName" to displayName, "email" to currentUser?.email,
            "driverId" to safeDriverId, "role" to initialRole,
            "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp()
        ), mapOf("displayName" to displayName, "email" to currentUser?.email, "updatedAt" to FieldValue.serverTimestamp()))

        upsertDoc("members/$uid", mapOf(
            "userId" to uid, "name" to displayName, "driverId" to safeDriverId,
            "baseArea" to member.baseArea.take(80), "motorcycleModel" to member.motorcycleModel.take(60),
            "motorcyclePlate" to member.motorcyclePlate.take(24), "role" to initialRole,
            "verificationStatus" to initialStatus, "loyaltyPoints" to member.loyaltyPoints.toLong(),
            "isOnline" to member.isOnline,
            "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp()
        ), mapOf(
            "name" to displayName, "baseArea" to member.baseArea.take(80),
            "motorcycleModel" to member.motorcycleModel.take(60), "motorcyclePlate" to member.motorcyclePlate.take(24),
            "isOnline" to member.isOnline, "updatedAt" to FieldValue.serverTimestamp()
        ))

        if (savePii) {
            val pii = mapOf(
                "phone" to member.phone.take(32), "address" to member.address.take(240),
                "bloodType" to member.bloodType.take(8), "emergencyContact" to member.emergencyContact.take(80),
                "emergencyPhone" to member.emergencyPhone.take(32), "simNumber" to member.simNumber.take(32),
                "nik" to member.nik.take(32), "updatedAt" to FieldValue.serverTimestamp()
            )
            upsertDoc("users/$uid/private/pii", pii + mapOf("userId" to uid, "createdAt" to FieldValue.serverTimestamp()), pii)
        }
    }

    suspend fun getOwnMemberProfile(uid: String = requireUid()): Result<CloudCommunityMember?> = runCatching {
        val ref = db.collection("members").document(uid)
        try {
            ref.get().await().toObject(CloudCommunityMember::class.java, ServerTimestampBehavior.ESTIMATE)
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.GET, ref.path, auth); throw e }
    }

    fun observeOwnMemberProfile(uid: String): Flow<CloudCommunityMember?> {
        val ref = db.collection("members").document(uid)
        return ref.snapshots().map { it.toObject(CloudCommunityMember::class.java, ServerTimestampBehavior.ESTIMATE) }
            .catch { error ->
                val fsErr = generateSequence(error) { it.cause }.filterIsInstance<Throwable>().firstOrNull()
                if (fsErr != null && auth.currentUser?.uid == uid) handleFirestoreError(fsErr, OperationType.GET, ref.path, auth)
                throw error
            }
    }

    suspend fun adminUpdateMemberRole(
        targetUid: String, previousRole: MemberRole, newRole: MemberRole, newStatus: VerificationStatus, reason: String
    ): Result<Unit> = runCatching {
        val actorUid = requireUid()
        val memberRef = db.collection("members").document(targetUid)
        val logRef = db.collection("role_audit_logs").document("log_${UUID.randomUUID().toString().take(12)}")
        try {
            memberRef.update(mapOf("role" to newRole.name, "verificationStatus" to newStatus.name, "updatedAt" to FieldValue.serverTimestamp())).await()
            logRef.set(mapOf(
                "actorUid" to actorUid, "targetUserId" to targetUid,
                "previousRole" to previousRole.name, "newRole" to newRole.name,
                "reason" to reason.ifBlank { "Pembaruan peran pengurus" }.take(240), "createdAt" to FieldValue.serverTimestamp()
            )).await()
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.UPDATE, memberRef.path, auth); throw e }
    }

    private suspend fun upsertDoc(path: String, createData: Map<String, Any?>, updateData: Map<String, Any?>) {
        val ref = db.document(path)
        try {
            val snap = ref.get().await()
            val payload = (if (snap.exists()) updateData else createData).filterValues { it != null }
            if (snap.exists()) ref.update(payload).await() else ref.set(payload).await()
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.WRITE, path, auth); throw e }
    }
}
