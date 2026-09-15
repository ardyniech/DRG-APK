package com.example.core.p2p

object P2PConfig {
    val STUN_SERVER = org.webrtc.PeerConnection.IceServer.builder(
        "stun:stun.l.google.com:19302"
    ).createIceServer()

    val TURN_SERVER = org.webrtc.PeerConnection.IceServer.builder(
        "turn:stun.l.google.com:19302?transport=udp"
    ).createIceServer()

    const val SELF_HOST_PORT = 8080
    const val STUN_TIMEOUT_MS = 5000L
    const val RETRY_INTERVAL_MS = 10000L
    const val MAX_PENDING_QUEUE_SIZE = 500
    const val FCM_P2P_TOPIC = "drg_p2p_discovery"
}