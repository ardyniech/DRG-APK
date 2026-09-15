package com.example.core.p2p

import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription

class P2PConnectionManager {

    private var peerConnection: PeerConnection? = null
    var dataChannel: DataChannel? = null
        private set

    fun createPeerConnection(factory: PeerConnectionFactory): PeerConnection {
        val config = org.webrtc.PeerConnection.Configuration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )

        peerConnection = factory.createPeerConnection(
            config,
            object : PeerConnection.Observer {
                override fun onDataChannel(channel: DataChannel) {
                    dataChannel = channel
                    Log.d(P2PConstants.TAG, "DataChannel diterima: ${channel.label}")
                    channel.registerObserver(object : DataChannel.Observer {
                        override fun onMessage(buffer: DataChannel.Buffer) {
                            val msg = String(buffer.data.array())
                            Log.d(P2PConstants.TAG, "Pesan dari peer: $msg")
                        }
                        override fun onBufferedAmountChanged(amount: Long) {}
                        override fun onStateChanged(state: DataChannel.State) {}
                    })
                }

                override fun onIceCandidate(candidate: IceCandidate?) {
                    candidate?.let {
                        Log.d(P2PConstants.TAG, "ICE candidate: ${it.sdp}")
                    }
                }

                override fun onAddStream(stream: org.webrtc.MediaStream?) {}
                override fun onRemoveStream(stream: org.webrtc.MediaStream?) {}
                override fun onIceCandidateFailure() {
                    Log.e(P2PConstants.TAG, "ICE candidate failure")
                }
                override fun onIceCandidatesRemoved() {}
                override fun onPeerConnectionInitialized() {}
                override fun onPeerConnectionParametersError(error: String?) {}
                override fun onSetLocalDescription(sdp: SessionDescription?) {}
                override fun onSetRemoteDescription(sdp: SessionDescription?) {}
                override fun onPeerConnectionClosed() {}
                override fun onPeerConnectionError(error: String?) {
                    Log.e(P2PConstants.TAG, "Peer error: $error")
                }
            }
        )

        dataChannel = peerConnection?.createDataChannel(
            "drg_driver_channel",
            DataChannel.Init()
        )

        return peerConnection!!
    }

    fun addIceCandidate(candidate: IceCandidate) {
        peerConnection?.addIceCandidate(candidate)
    }

    fun sendData(message: String) {
        if (dataChannel?.state == DataChannel.State.OPEN) {
            dataChannel?.send(
                DataChannel.Buffer(
                    java.nio.ByteBuffer.wrap(message.toByteArray()),
                    false
                )
            )
        } else {
            Log.w(P2PConstants.TAG, "Tidak bisa kirim: DataChannel belum BUKA")
        }
    }

    fun close() {
        peerConnection?.close()
        peerConnection = null
        dataChannel = null
    }
}