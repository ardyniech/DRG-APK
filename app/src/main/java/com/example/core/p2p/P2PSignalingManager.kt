package com.example.core.p2p

import com.example.core.database.AppDatabase
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * P2P Signaling Manager (mode lokal/Tailscale-aware, tanpa ketergantungan FCM topic aktif).
 * API kompatibel dengan P2PDiscovery: joinDiscoveryTopic, broadcastPeerInfo, onMessageReceived.
 * Implementasi sinyal disimpan via antrean lokal; tanpa debug logging di jalur broadcast.
 */
class P2PSignalingManager(
    private val db: AppDatabase
) {

    suspend fun joinDiscoveryTopic() = withContext(Dispatchers.IO) {
        // Mode lokal: tidak subscribe FCM topic. Discovery berbasis mesh/antrean lokal.
    }

    suspend fun broadcastPeerInfo(myIP: String, myPort: Int) = withContext(Dispatchers.IO) {
        val senderId = currentDriverId()
        // Simpan info peer ke antrean sinkronisasi lokal (tanpa output debug log).
        db.syncQueueDao().insertSyncItem(
            com.example.shared.models.PendingSyncEntity(
                id = "p2p_$senderId",
                entityType = "p2p_announce",
                payloadJson = """{"senderId":"$senderId","ip":"$myIP","port":$myPort}""",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun onMessageReceived(message: RemoteMessage) {
        val senderIP = message.data[P2PConstants.KEY_IP] ?: return
        val senderPort = message.data[P2PConstants.KEY_PORT]?.toIntOrNull() ?: return
        val senderId = message.data[P2PConstants.KEY_SENDER_ID] ?: return
        // Proses sinyal masuk secara diam-diam ke antrean lokal.
        kotlinx.coroutines.runBlocking(Dispatchers.IO) {
            db.syncQueueDao().insertSyncItem(
                com.example.shared.models.PendingSyncEntity(
                    id = "p2p_signal_${senderId}_$senderPort",
                    entityType = "p2p_signal",
                    payloadJson = """{"senderId":"$senderId","ip":"$senderIP","port":$senderPort}""",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    private suspend fun currentDriverId(): String = withContext(Dispatchers.IO) {
        val member = db.memberDao().getMemberById("current")
        member?.name ?: "unknown_driver"
    }
}