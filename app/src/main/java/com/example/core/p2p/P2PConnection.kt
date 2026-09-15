package com.example.core.p2p

import android.util.Log
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceCandidate
import org.webrtc.PeerConnection.SessionDescription
import org.webrtc.PeerConnectionConstraints

class P2PConnectionManager {

    private var peerConnection: PeerConnection? = null
    private var dataChannel: DataChannel? = null

    // Buat PeerConnection dengan konfigurasi STUN/TURN
    fun createPeerConnection(): PeerConnection {
        val config = PeerConnection.Configuration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )
        val constraints = PeerConnectionConstraints(
            optionalIceCandidates = true
        )

        return PeerConnectionFactoryAdapter.INSTANCE.createPeerConnection(
            config,
            P2PConnectionObserver(),
            constraints
        )
    }

    // Set connection ke peer tertentu
    fun setRemoteDescription(sdp: String) {
        // Parse SDP dan set ke PeerConnection
        // Log.d(P2PConstants.TAG, "Set remote SDP: ${sdp.substring(0, minOf(200, sdp.length))}...")
    }

    // Set ICE candidate
    fun addIceCandidate(candidate: IceCandidate) {
        peerConnection?.addIceCandidate(candidate)
    }

    // Dapatkan DataChannel untuk kirim data
    fun getDataChannel(): DataChannel? = dataChannel

    // Inisialisasi DataChannel
    fun initDataChannel(channelName: String) {
        dataChannel = PeerConnectionFactoryAdapter.INSTANCE.createPeerConnection(
            PeerConnection.Configuration(listOf(P2PConfig.STUN_SERVER)),
            object : PeerConnection.Observer {
                override fun onDataChannel(channel: DataChannel) {
                    dataChannel = channel
                    Log.d(P2PConstants.TAG, "DataChannel berhasil diinisialisasi")
                }
            }
        ).createDataChannel(
            channelName,
            DataChannel.Init()
        )
    }

    // Kirim data P2P
    fun sendPeerData(message: String) {
        dataChannel?.send(
            org.webrtc.DataChannel.Buffer(
                java.nio.ByteBuffer.wrap(message.toByteArray()),
                false
            )
        )
    }

    // Observer untuk PeerConnection events
    inner class P2PConnectionObserver : PeerConnection.Observer {
        override fun onIceCandidate(candidate: IceCandidate?) {
            Log.d(P2PConstants.TAG, "ICE candidate terima: ${candidate?.sdp?.substring(0, minOf(100, candidate.sdp?.length ?: 0))}...")
        }

        override fun onAddStream(stream: org.webrtc.MediaStream?) {
            // Not used
        }

        override fun onRemoveStream(stream: org.webrtc.MediaStream?) {
            // Not used
        }

        override fun onIceCandidateFailure() {
            Log.e(P2PConstants.TAG, "ICE candidate failure")
        }

        override fun onIceCandidatesRemoved() {
            // Not used
        }

        override fun onPeerConnectionInitialized() {
            Log.d(P2PConstants.TAG, "Peer connection initialized")
        }

        override fun onPeerConnectionParametersError(error: String?) {
            Log.e(P2PConstants.TAG, "Peer connection parameters error: $error")
        }

        override fun onSetLocalDescription(sdp: SessionDescription?) {
            Log.d(P2PConstants.TAG, "Set local description")
        }

        override fun onSetRemoteDescription(sdp: SessionDescription?) {
            Log.d(P2PConstants.TAG, "Set remote description")
        }

        override fun onPeerConnectionClosed() {
            Log.d(P2PConstants.TAG, "Peer connection closed")
        }

        override fun onPeerConnectionError(error: String?) {
            Log.e(P2PConstants.TAG, "Peer connection error: $error")
        }
    }
}

// Singleton buat PeerConnectionFactory
object PeerConnectionFactoryAdapter {
    val INSTANCE by lazy {
        val initOptions = org.webrtc.PeerConnectionFactory.InitializationOptions
            .builder(org.webrtc.ApplicationContext)
            .createInitializationOptions()
        org.webrtc.PeerConnectionFactory.initialize(initOptions)
        org.webrtc.PeerConnectionFactory.builder()
            .createPeerConnectionFactory()
    }
}