package com.example.core.p2p

object P2PConfig {
    // Google Public STUN Server — gratis, tidak perlu akun
    val STUN_SERVER = PeerConnection.IceServer.builder(
        "stun:stun.l.google.com:19302"
    ).createIceServer()

    // Google TURN Relay — jika STUN gagal (CGNAT di jaringan seluler)
    val TURN_SERVER = PeerConnection.IceServer.builder(
        "turn:stun.l.google.com:19302?transport=udp"
    ).createIceServer()

    // Self-host server port — setiap device mendengarkan port ini
    const val SELF_HOST_PORT = 8080

    // Timeout STUN (ms)
    const val STUN_TIMEOUT_MS = 5000L

    // Interval retry koneksi P2P (ms)
    const val RETRY_INTERVAL_MS = 10000L

    // Maksimal ukuran PendingSyncQueue
    const val MAX_PENDING_QUEUE_SIZE = 500

    // Signaling via FCM topic
    const val FCM_P2P_TOPIC = "drg_p2p_discovery"
}