package com.example.core.firebase

import com.example.shared.models.CloudPttTransmissionDoc
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestorePttTransmissionRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun requireUid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Pengguna belum masuk dengan akun Google.")

    private fun cleanId(raw: String, fallback: String): String =
        raw.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").ifBlank { fallback }.take(64)

    suspend fun publishPttTransmission(
        senderName: String, channelId: String, audioBase64: String, durationMs: Long,
        txId: String = "ptt_${UUID.randomUUID().toString().take(12)}"
    ): Result<String> = runCatching {
        val uid = requireUid()
        require(audioBase64.isNotBlank() && audioBase64.length <= 180_000) { "Ukuran audio PTT di luar batas." }
        val ref = db.collection("ptt_transmissions").document(cleanId(txId, "ptt_1"))
        val data = mapOf(
            "userId" to uid, "senderName" to senderName.ifBlank { "Driver DRG" }.take(80),
            "channelId" to cleanId(channelId, "suhat_lowokwaru"), "audioBase64" to audioBase64,
            "codec" to "ULAW_8K", "sampleRateHz" to 8000L, "durationMs" to durationMs.coerceIn(100L, 15000L),
            "status" to "LIVE", "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            ref.set(data).await(); ref.id
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.CREATE, ref.path, auth); throw e }
    }

    suspend fun endPttTransmission(txId: String): Result<Unit> = runCatching {
        requireUid()
        val ref = db.collection("ptt_transmissions").document(txId)
        try {
            ref.update(mapOf("status" to "ENDED", "updatedAt" to FieldValue.serverTimestamp())).await()
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.UPDATE, ref.path, auth); throw e }
    }

    fun observeLivePttChannel(channelId: String): Flow<List<CloudPttTransmissionDoc>> {
        val path = "ptt_transmissions"
        return db.collection(path).whereEqualTo("channelId", cleanId(channelId, "suhat_lowokwaru"))
            .whereEqualTo("status", "LIVE").snapshots()
            .map { it.toObjects(CloudPttTransmissionDoc::class.java, ServerTimestampBehavior.ESTIMATE) }
            .catch { error ->
                if (error !is CancellationException && auth.currentUser != null) handleFirestoreError(error, OperationType.LIST, path, auth)
                throw error
            }
    }
}
