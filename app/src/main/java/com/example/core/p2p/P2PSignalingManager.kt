package com.example.core.p2p

import android.util.Log
import com.example.core.database.AppDatabase
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class P2PSignalingManager(
    private val db: AppDatabase
) {
    private val messaging = FirebaseMessaging.getInstance()

    suspend fun joinDiscoveryTopic() = withContext(Dispatchers.IO) {
        try {
            Tasks.await(messaging.subscribeToTopic(P2PConfig.FCM_P2P_TOPIC))
            Log.d(P2PConstants.TAG, "Terjoin topic: \${P2PConfig.FCM_P2P_TOPIC}")
        } catch (e: Exception) {
            Log.e(P2PConstants.TAG, "Gagal join topic: \${e.message}")
        }
    }

    suspend fun broadcastPeerInfo(myIP: String, myPort: Int) = withContext(Dispatchers.IO) {
        val senderId = currentDriverId()
        Log.d(P2PConstants.TAG, "Broadcast signaling peer: senderId=\$senderId, IP=\$myIP, Port=\$myPort")
    }

    fun onMessageReceived(message: RemoteMessage) {
        val senderIP = message.data[P2PConstants.KEY_IP] ?: run {
            Log.w(P2PConstants.TAG, "Signaling: IP tidak ada"); return
        }
        val senderPort = message.data[P2PConstants.KEY_PORT]?.toIntOrNull() ?: run {
            Log.w(P2PConstants.TAG, "Signaling: port tidak valid"); return
        }
        val senderId = message.data[P2PConstants.KEY_SENDER_ID] ?: run {
            Log.w(P2PConstants.TAG, "Signaling: senderId tidak ada"); return
        }
        Log.d(P2PConstants.TAG, "Terima signaling dari \$senderId: IP=\$senderIP, Port=\$senderPort")
    }

    private suspend fun currentDriverId(): String = withContext(Dispatchers.IO) {
        val member = db.memberDao().getMemberById("current")
        return@withContext member?.name ?: "unknown_driver"
    }
}
