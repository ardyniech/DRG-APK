package com.example.core.p2p

object P2PConstants {
    // Tag untuk Logcat
    const val TAG = "P2P-SelfHost"

    // Perintah DataChannel
    const val CMD_ALERT = "alert"
    const val CMD_CHECK_IN = "check_in"
    const val CMD_LOCATION = "location"
    const val CMD_STATUS = "status"
    const val CMD_HEARTBEAT = "heartbeat"

    // Status koneksi
    const val CONNECTION_CONNECTING = "connecting"
    const val CONNECTION_CONNECTED = "connected"
    const val CONNECTION_FAILED = "failed"
    const val CONNECTION_DISCONNECTED = "disconnected"

    // FCM message keys
    const val KEY_SENDER_ID = "senderId"
    const val KEY_IP = "ip"
    const val KEY_PORT = "port"
    const val KEY_TIMESTAMP = "timestamp"
    const val KEY_MESSAGE = "message"

    // Database entity types
    const val ENTITY_TYPE_P2P_ALERT = "P2P_ALERT"
    const val ENTITY_TYPE_P2P_MESSAGE = "P2P_MESSAGE"
}