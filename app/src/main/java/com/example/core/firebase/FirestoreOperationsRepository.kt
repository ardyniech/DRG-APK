package com.example.core.firebase

import android.content.Context
import com.example.R
import com.example.shared.models.CloudEmergencyAlert
import com.example.shared.models.CloudKasTransaction
import com.example.shared.models.TransactionType
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

class FirestoreOperationsRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(context.applicationContext.getString(R.string.firestore_database_id))
    )

    private fun requireUid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Pengguna belum masuk dengan akun Google.")

    suspend fun addKasTransaction(
        txId: String = "tx_${UUID.randomUUID().toString().take(12)}",
        title: String, amount: Long, type: TransactionType, category: String, recordedBy: String? = null
    ): Result<String> = runCatching {
        val uid = requireUid()
        require(title.isNotBlank() && amount > 0L && category.isNotBlank()) { "Data transaksi kas tidak valid." }
        val ref = db.collection("kas_transactions").document(txId)
        val payload = mapOf(
            "userId" to uid, "title" to title.take(120), "amount" to amount, "type" to type.name,
            "category" to category.take(60), "recordedBy" to recordedBy?.take(80),
            "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }
        try {
            ref.set(payload).await(); ref.id
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.CREATE, ref.path, auth); throw e }
    }

    fun observeOwnKasTransactions(uid: String): Flow<List<CloudKasTransaction>> {
        val path = "kas_transactions"
        return db.collection(path).whereEqualTo("userId", uid).snapshots()
            .map { snap -> snap.toObjects(CloudKasTransaction::class.java, ServerTimestampBehavior.ESTIMATE) }
            .catch { error ->
                if (error !is CancellationException && auth.currentUser?.uid == uid) handleFirestoreError(error, OperationType.LIST, path, auth)
                throw error
            }
    }

    suspend fun publishEmergencyAlert(
        alertId: String = "sos_${UUID.randomUUID().toString().take(12)}",
        driverName: String, alertType: String, message: String? = null,
        locationName: String? = null, lat: Double? = null, lng: Double? = null
    ): Result<String> = runCatching {
        val uid = requireUid()
        val ref = db.collection("emergency_alerts").document(alertId)
        val payload = mapOf(
            "userId" to uid, "driverName" to driverName.ifBlank { "Driver DRG" }.take(80),
            "alertType" to alertType.ifBlank { "DARURAT" }.take(40), "message" to message?.take(240),
            "locationName" to locationName?.take(120), "latitude" to lat, "longitude" to lng,
            "status" to "ACTIVE", "respondersCount" to 0L,
            "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp()
        ).filterValues { it != null }
        try {
            ref.set(payload).await(); ref.id
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.CREATE, ref.path, auth); throw e }
    }

    suspend fun resolveEmergencyAlert(alertId: String): Result<Unit> = runCatching {
        requireUid()
        val ref = db.collection("emergency_alerts").document(alertId)
        try {
            ref.update(mapOf("status" to "RESOLVED", "updatedAt" to FieldValue.serverTimestamp())).await()
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.UPDATE, ref.path, auth); throw e }
    }

    fun observeActiveAlerts(): Flow<List<CloudEmergencyAlert>> {
        val path = "emergency_alerts"
        return db.collection(path).whereEqualTo("status", "ACTIVE").snapshots()
            .map { snap -> snap.toObjects(CloudEmergencyAlert::class.java, ServerTimestampBehavior.ESTIMATE) }
            .catch { error ->
                if (error !is CancellationException && auth.currentUser != null) handleFirestoreError(error, OperationType.LIST, path, auth)
                throw error
            }
    }
}
