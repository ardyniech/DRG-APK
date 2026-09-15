package com.example.core.p2p

import android.content.Context
import android.util.Log
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.IceCandidate

class P2PManager(private val context: Context) {

    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    var dataChannel: DataChannel? = null
        private set
    private var isServerMode = false

    fun initialize() {
        val options = PeerConnectionFactory.InitializationOptions
            .builder(context)
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .createPeerConnectionFactory()
    }

    fun startSelfHost() {
        isServerMode = true
        val connectionManager = P2PConnectionManager()
        peerConnection = connectionManager.createPeerConnection(
            peerConnectionFactory!!
        )
        dataChannel = connectionManager.dataChannel
        Log.d(P2PConstants.TAG, "Self-host server dimulai di port ${P2PConfig.SELF_HOST_PORT}")
    }

    fun connectToPeer(remoteIP: String, remotePort: Int, factory: PeerConnectionFactory) {
        val connectionManager = P2PConnectionManager()
        peerConnection = connectionManager.createPeerConnection(factory)
        val iceServer = org.webrtc.PeerConnection.IceServer.builder(
            "stun:$remoteIP:$remotePort"
        ).createIceServer()
        peerConnection?.addIceCandidate(IceCandidate("data", 0, iceServer.address))
        Log.d(P2PConstants.TAG, "Connect ke peer: $remoteIP:$remotePort")
    }

    fun sendData(message: String) {
        if (dataChannel != null) {
            val buffer = DataChannel.Buffer(
                java.nio.ByteBuffer.wrap(message.toByteArray()),
                false
            )
            dataChannel?.send(buffer)
            Log.d(P2PConstants.TAG, "Data terkirim P2P: ${message.take(50)}...")
        } else {
            Log.w(P2PConstants.TAG, "Gagal kirim: DataChannel null")
        }
    }

    fun close() {
        peerConnection?.close()
        peerConnection = null
        dataChannel = null
    }
}