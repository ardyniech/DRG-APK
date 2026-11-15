package com.example.core.firebase

import android.content.Context
import com.example.R
import com.example.shared.models.CloudEncryptedBackup
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirestoreBackupSyncRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(context.applicationContext.getString(R.string.firestore_database_id))
    )

    private fun requireUid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Pengguna belum masuk dengan akun Google.")

    suspend fun pushSyncQueueItem(
        itemId: String,
        entityType: String,
        entityId: String,
        payload: String?
    ): Result<Unit> = runCatching {
        val uid = requireUid()
        val safeDocId = itemId.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(128).ifBlank { "sync_item" }
        val ref = db.collection("users").document(uid).collection("sync_queue").document(safeDocId)
        val existing = runCatching { ref.get().await() }.getOrNull()
        val map = if (existing?.exists() == true) {
            mapOf(
                "entityType" to entityType.take(64),
                "entityId" to entityId.take(128),
                "payload" to payload?.take(32768),
                "syncState" to "SYNCED",
                "updatedAt" to FieldValue.serverTimestamp()
            )
        } else {
            mapOf(
                "userId" to uid,
                "entityType" to entityType.take(64),
                "entityId" to entityId.take(128),
                "payload" to payload?.take(32768),
                "syncState" to "SYNCED",
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
        }.filterValues { it != null }
        try {
            if (existing?.exists() == true) ref.update(map).await() else ref.set(map).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, ref.path, auth)
            throw e
        }
    }

    suspend fun uploadEncryptedBackup(
        backupId: String,
        checksumSha256: String,
        encryptedPayload: String,
        schemaVersion: Int
    ): Result<Unit> = runCatching {
        val uid = requireUid()
        val safeId = backupId.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(128).ifBlank { "latest_backup" }
        val ref = db.collection("users").document(uid).collection("backups").document(safeId)
        val existing = runCatching { ref.get().await() }.getOrNull()
        val data = if (existing?.exists() == true) {
            mapOf(
                "checksumSha256" to checksumSha256,
                "encryptedPayload" to encryptedPayload,
                "schemaVersion" to schemaVersion,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        } else {
            mapOf(
                "userId" to uid,
                "checksumSha256" to checksumSha256,
                "encryptedPayload" to encryptedPayload,
                "schemaVersion" to schemaVersion,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
        }
        try {
            if (existing?.exists() == true) ref.update(data).await() else ref.set(data).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, ref.path, auth)
            throw e
        }
    }

    suspend fun fetchLatestEncryptedBackup(): Result<CloudEncryptedBackup?> = runCatching {
        val uid = requireUid()
        val col = db.collection("users").document(uid).collection("backups")
        try {
            val snap = col.get().await()
            snap.toObjects(CloudEncryptedBackup::class.java, ServerTimestampBehavior.ESTIMATE)
                .maxByOrNull { (it.updatedAt ?: it.createdAt ?: Timestamp(0, 0)).seconds }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, col.path, auth)
            throw e
        }
    }
}
