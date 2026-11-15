package com.example.core.firebase

import com.example.shared.models.P2pPeerDoc
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class FirestoreP2pPeerRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun requireUid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Pengguna belum masuk dengan akun Google.")

    private fun cleanId(raw: String, fallback: String): String =
        raw.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").ifBlank { fallback }.take(64)

    suspend fun registerOnlinePeer(
        driverName: String, channelId: String, localIp: String, port: Int = 8080, sdpOffer: String? = null
    ): Result<String> = runCatching {
        val uid = requireUid()
        val ref = db.collection("p2p_peers").document(uid)
        val safePort = port.coerceIn(1024, 65535).toLong()
        val safeOffer = sdpOffer?.takeIf { it.isNotBlank() }?.take(16384)
        val base = mutableMapOf<String, Any>(
            "driverName" to driverName.ifBlank { "Driver DRG" }.take(80),
            "channelId" to cleanId(channelId, "suhat_lowokwaru"),
            "localIp" to localIp.take(64),
            "port" to safePort, "status" to "ONLINE", "updatedAt" to FieldValue.serverTimestamp()
        )
        if (safeOffer != null) base["sdpOffer"] = safeOffer
        try {
            val snap = ref.get().await()
            if (snap.exists()) ref.update(base).await()
            else ref.set(base + mapOf("userId" to uid, "createdAt" to FieldValue.serverTimestamp())).await()
            uid
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.WRITE, ref.path, auth); throw e }
    }

    suspend fun goOfflinePeer(): Result<Unit> = runCatching {
        val uid = requireUid()
        val ref = db.collection("p2p_peers").document(uid)
        try {
            if (ref.get().await().exists()) {
                ref.update(mapOf("status" to "OFFLINE", "updatedAt" to FieldValue.serverTimestamp())).await()
            }
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) { handleFirestoreError(e, OperationType.UPDATE, ref.path, auth); throw e }
    }

    fun observeOnlinePeers(channelId: String): Flow<List<P2pPeerDoc>> {
        val path = "p2p_peers"
        return db.collection(path).whereEqualTo("channelId", cleanId(channelId, "suhat_lowokwaru"))
            .whereEqualTo("status", "ONLINE").snapshots()
            .map { it.toObjects(P2pPeerDoc::class.java, ServerTimestampBehavior.ESTIMATE) }
            .catch { error ->
                if (error !is CancellationException && auth.currentUser != null) handleFirestoreError(error, OperationType.LIST, path, auth)
                throw error
            }
    }
}
