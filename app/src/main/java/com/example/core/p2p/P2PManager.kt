package com.example.core.p2p

import android.content.Context
import android.util.Log
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class P2PManager(private val context: Context) {

    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var dataChannel: DataChannel? = null
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

    // === SELF-HOST: Buka device sebagai server mendengarkan koneksi masuk ===
    fun startSelfHost() {
        isServerMode = true
        val config = PeerConnection.Configuration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )

        peerConnection = peerConnectionFactory?.createPeerConnection(
            config,
            P2PObserver()
        )

        // Buat DataChannel — ini tempat data P2P jalan
        dataChannel = peerConnection?.createDataChannel(
            "drg_driver_channel",
            DataChannel.Init()
        )

        Log.d(P2PConstants.TAG, "Self-host server dimulai di port ${P2PConfig.SELF_HOST_PORT}")
    }

    // === CLIENT: Hubungi device lain dengan IP dan port tertentu ===
    fun connectToPeer(remoteIP: String, remotePort: Int) {
        val config = PeerConnection.Configuration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )

        peerConnection = peerConnectionFactory?.createPeerConnection(
            config,
            P2PObserver()
        )

        // Setup ICE candidate untuk connect ke peer
        val remoteAddress = PeerConnection.IceServer.builder(
            "stun:$remoteIP:$remotePort"
        ).createIceServer()

        peerConnection?.addIceCandidate(
            IceCandidate(
                sdpMid = "data",
                sdpMLineIndex = 0,
                candidate = remoteIP to remotePort
            )
        )
    }

    // === KIRIM DATA: Kirim pesan langsung ke peer via DataChannel ===
    fun sendData(message: String) {
        if (dataChannel != null) {
            val buffer = DataChannel.Buffer(
                java.nio.ByteBuffer.wrap(message.toByteArray()),
                false
            )
            dataChannel?.send(buffer)
            Log.d(P2PConstants.TAG, "Data terkirim P2P: ${message.substring(0, if (message.length > 50) 50 else message.length)}...")
        } else {
            Log.w(P2PConstants.TAG, "Gagal kirim data: DataChannel belum dibuat")
        }
    }

    // === TERIMA DATA: Register observer untuk menerima pesan dari peer ===
    inner class P2PObserver : PeerConnection.Observer {
        override fun onDataChannel(dataChannel: DataChannel) {
            P2PManager.dataChannel = dataChannel
            dataChannel.registerObserver(object : DataChannel.Observer {
                override fun onMessage(buffer: DataChannel.Buffer) {
                    val message = String(buffer.data.array())
                    Log.d(P2PConstants.TAG, "Data diterima dari peer: $message")
                    // TODO: Simpan ke Room DB via P2PDao
                }
            })
        }

        override fun onIceCandidate(candidate: IceCandidate?) {
            Log.d(P2PConstants.TAG, "ICE candidate: ${candidate?.sdp}")
        }

        override fun onAddStream(stream: org.webrtc.MediaStream?) {
            // Not used for DataChannel-only
        }

        override fun onRemoveStream(stream: org.webrtc.MediaStream?) {
            // Not used for DataChannel-only
        }

        override fun onIceCandidateFailure() {
            Log.e(P2PConstants.TAG, "ICE candidate failure")
        }

        override fun onIceCandidatesRemoved() {
            // Not used
        }

        override fun onPeerConnectionValidityChange(isValid: Boolean) {
            Log.d(P2PConstants.TAG, "Peer connection validity: $isValid")
        }
    }
}