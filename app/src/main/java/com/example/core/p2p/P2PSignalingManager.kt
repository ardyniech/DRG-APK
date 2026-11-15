package com.example.core.p2p

import com.example.core.database.AppDatabase
import com.example.core.firebase.FirestoreP2pSignalingRepository
import com.example.shared.models.PendingSyncEntity
import com.google.firebase.Firebase
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Koordinator signaling P2P (Firestore peer discovery + FCM topic subscription + antrean offline Room).
 */
class P2PSignalingManager(
    private val db: AppDatabase,
    private val cloudRepo: FirestoreP2pSignalingRepository? = null
) {
    private val bgScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun joinDiscoveryTopic() = withContext(Dispatchers.IO) {
        runCatching { Firebase.messaging.subscribeToTopic(P2PConfig.FCM_P2P_TOPIC).await() }
        Unit
    }

    suspend fun broadcastPeerInfo(
        myIP: String, myPort: Int, channelId: String = "suhat_lowokwaru", sdpOffer: String? = null
    ) = withContext(Dispatchers.IO) {
        val senderId = currentDriverName()
        val cloudOk = if (cloudRepo != null && myIP.isNotBlank()) {
            cloudRepo.registerOnlinePeer(senderId, channelId, myIP, myPort, sdpOffer).isSuccess
        } else false
        if (!cloudOk) {
            db.syncQueueDao().insertSyncItem(
                PendingSyncEntity(
                    id = "p2p_${UUID.randomUUID().toString().take(8)}",
                    entityType = "p2p_announce",
                    payloadJson = """{"senderId":"$senderId","ip":"$myIP","port":$myPort,"status":"QUEUED_OFFLINE"}""",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun sendWebRtcSignal(
        targetUid: String, channelId: String, signalType: String, payload: String
    ): Boolean = withContext(Dispatchers.IO) {
        val sentCloud = cloudRepo?.sendSignal(targetUid, channelId, signalType, payload)?.isSuccess == true
        if (!sentCloud) {
            db.syncQueueDao().insertSyncItem(
                PendingSyncEntity(
                    id = "p2p_sig_${UUID.randomUUID().toString().take(8)}",
                    entityType = "p2p_signal",
                    payloadJson = """{"target":"$targetUid","type":"$signalType","status":"QUEUED_OFFLINE"}""",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        sentCloud
    }

    fun onMessageReceived(message: RemoteMessage) {
        val senderIP = message.data[P2PConstants.KEY_IP] ?: return
        val senderPort = message.data[P2PConstants.KEY_PORT]?.toIntOrNull() ?: return
        val senderId = message.data[P2PConstants.KEY_SENDER_ID] ?: return
        bgScope.launch {
            db.syncQueueDao().insertSyncItem(
                PendingSyncEntity(
                    id = "p2p_signal_${senderId}_$senderPort",
                    entityType = "p2p_signal",
                    payloadJson = """{"senderId":"$senderId","ip":"$senderIP","port":$senderPort}""",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    private suspend fun currentDriverName(): String = withContext(Dispatchers.IO) {
        db.memberDao().getMemberById("current")?.name ?: "Driver DRG"
    }
}