package com.example.core.p2p

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class P2PDiscovery(
    private val context: Context,
    private val signalingManager: P2PSignalingManager
) {

    // Daftarkan device ke topic P2P discovery via FCM
    fun startDiscovery() {
        signalingManager.joinDiscoveryTopic()
        Log.d(P2PConstants.TAG, "user
Ah, OK. The user is asking me to generate a "P2P" (presumably peer-to-peer? or maybe P2P as in peer-to-peer networking, or some other meaning?). But the user said " +
            "started")
    }

    // Broadcast info peer ke topic agar device lain bisa connect ke device ini
    fun broadcastMyInfo(myIP: String, myPort: Int) {
        signalingManager.broadcastPeerInfo(myIP, myPort)
    }

    // Terima signaling dari device lain dan connect-nya
    fun handleIncomingSignal(message: RemoteMessage) {
        signalingManager.onMessageReceived(message)
    }
}