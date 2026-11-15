package com.example.core.firebase

import android.content.Context
import com.example.R
import com.example.shared.models.CloudPttTransmissionDoc
import com.example.shared.models.P2pPeerDoc
import com.example.shared.models.P2pSignalDoc
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

class FirestoreP2pSignalingRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    private val peerRepo = FirestoreP2pPeerRepository(db, auth)
    private val pttRepo = FirestorePttTransmissionRepository(db, auth)

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(context.applicationContext.getString(R.string.firestore_database_id))
    )

    private fun requireUid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Pengguna belum masuk dengan akun Google.")

    private fun cleanId(raw: String, fallback: String): String =
        raw.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").ifBlank { fallback }.take(64)

    suspend fun registerOnlinePeer(
        driverName: String, channelId: String, localIp: String, port: Int = 8080, sdpOffer: String? = null
    ) = peerRepo.registerOnlinePeer(driverName, channelId, localIp, port, sdpOffer)

    suspend fun goOfflinePeer() = peerRepo.goOfflinePeer()

    fun observeOnlinePeers(channelId: String) = peerRepo.observeOnlinePeers(channelId)

    suspend fun sendSignal(
        targetUid: String, channelId: String, signalType: String, payload: String,
        signalId: String = "sig_${UUID.randomUUID().toString().take(12)}"
    ): Result<String> = runCatching {
        val uid = requireUid()
        val safeType = if (signalType in setOf("OFFER", "ANSWER", "ICE")) signalType else "ICE"
        require(targetUid.isNotBlank() && payload.isNotBlank()) { "Payload sinyal kosong." }
        val ref = db.collection("p2p_signals").document(cleanId(signalId, "sig_1"))
        val data = mapOf(
            "senderUserId" to uid, "targetUserId" to targetUid.take(128),
            "channelId" to cleanId(channelId, "suhat_lowokwaru"), "signalType" to safeType,
            "payload" to payload.take(16384), "consumed" to false,
            "createdAt" to FieldValue.serverTimestamp(), "updatedAt" to FieldValue.serverTimestamp()
        )
        try { ref.set(data).await(); ref.id } catch (e: Exception) { handleFirestoreError(e, OperationType.CREATE, ref.path, auth); throw e }
    }

    fun observeIncomingSignals(uid: String = requireUid()): Flow<List<P2pSignalDoc>> {
        val path = "p2p_signals"
        return db.collection(path).whereEqualTo("targetUserId", uid).snapshots()
            .map { snap -> snap.toObjects(P2pSignalDoc::class.java, ServerTimestampBehavior.ESTIMATE).filter { !it.consumed } }
            .catch { error ->
                if (error !is CancellationException && auth.currentUser?.uid == uid) handleFirestoreError(error, OperationType.LIST, path, auth)
                throw error
            }
    }

    suspend fun markSignalConsumed(signalId: String): Result<Unit> = runCatching {
        requireUid()
        val ref = db.collection("p2p_signals").document(signalId)
        try { ref.update(mapOf("consumed" to true, "updatedAt" to FieldValue.serverTimestamp())).await() }
        catch (e: Exception) { handleFirestoreError(e, OperationType.UPDATE, ref.path, auth); throw e }
    }

    suspend fun publishPttTransmission(senderName: String, channelId: String, audioBase64: String, durationMs: Long, txId: String = "ptt_${UUID.randomUUID().toString().take(12)}") =
        pttRepo.publishPttTransmission(senderName, channelId, audioBase64, durationMs, txId)

    suspend fun endPttTransmission(txId: String) = pttRepo.endPttTransmission(txId)

    fun observeLivePttChannel(channelId: String): Flow<List<CloudPttTransmissionDoc>> = pttRepo.observeLivePttChannel(channelId)
}
