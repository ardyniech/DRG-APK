package com.example.core.p2p

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class P2PDiscovery(
    private val signalingManager: P2PSignalingManager
) {
    fun startDiscovery() {
        CoroutineScope(Dispatchers.IO).launch {
            signalingManager.joinDiscoveryTopic()
            Log.d(P2PConstants.TAG, "Discovery dimulai")
        }
    }

    suspend fun broadcastMyInfo(myIP: String, myPort: Int) = withContext(Dispatchers.IO) {
        signalingManager.broadcastPeerInfo(myIP, myPort)
    }

    fun handleIncomingSignal(message: RemoteMessage) {
        signalingManager.onMessageReceived(message)
    }
}
