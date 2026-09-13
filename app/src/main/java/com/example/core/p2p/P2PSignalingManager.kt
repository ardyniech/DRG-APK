package com.example.core.p2p

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.Future

class P2PSignalingManager(
    private val context: Context,
    private val db: AppDatabase
) {

    private val messaging = FirebaseMessaging.getInstance()

    // === gabung ke FCM topic agar device lain bisa kirim signaling ke device ini ===
    suspend fun joinDiscoveryTopic() {
        messaging.subscribeToTopic(P2PConfig.FCM_P2P_TOPIC)
            .await()
        Log.d(P2PConstants.TAG, "Terjoin topic P2P discovery: ${P2PConfig.FCM_P2P_TOPIC}")
    }

    // === KIRIM signaling ke semua device di topic ===
    // Device ini akan mengirim IP & port-nya agar device lain bisa connect
    suspend fun broadcastPeerInfo(myIP: String, myPort: Int) {
        val signalData = mapOf(
            P2PConstants.KEY_SENDER_ID to currentDriverId(),
            P2PConstants.KEY_IP to myIP,
            P2PConstants.KEY_PORT to myPort,
            P2PConstants.KEY_TIMESTAMP to System.currentTimeMillis()
        )

        val message = Message.Builder()
            .setTopic(P2PConfig.FCM_P2P_TOPIC)
            .setData(signalData)
            .build()

        messaging.send(message).await()
        Log.d(P2PConstants.TAG, "Broadcast signaling: IP=$myIP, Port=$myPort")
    }

    // === TERIMA signaling dari device lain ===
    // Method ini dipanggil saat FCM meneruskan pesan dari device lain
    fun onMessageReceived(message: RemoteMessage) {
        val senderIP = message.data[P2PConstants.KEY_IP] ?: run {
            Log.w(P2PConstants.TAG, "Signaling diterima tapi IP tidak ada")
            return@onMessageReceived
        }
        val senderPort = message.data[P2PConstants.KEY_PORT]?.toInt() ?: run {
            Log.w(P2PConstants.TAG, "Signaling diterima tapi port tidak valid")
            return@onMessageReceived
        }
        val senderId = message.data[P2PConstants.KEY_SENDER_ID] ?: run {
            Log.w(P2PConstants.TAG, "Signaling diterima tapi senderId tidak ada")
            return@onMessageReceived
        }

        Log.d(P2PConstants.TAG, "Terima signaling dari $senderId: IP=$senderIP, Port=$senderPort")

        // Lakukan connect ke peer ini (via WebRTC STUN hole punching)
        P2PConnectionManager().connectToPeer(senderIP, senderPort)
    }

    // === Ambil ID driver dari database lokal ===
    private fun currentDriverId(): String {
        return db.memberDao().getCurrentMember()
            .await() ?: "unknown_driver"
    }
}