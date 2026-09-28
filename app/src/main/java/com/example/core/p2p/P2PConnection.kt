package com.example.core.p2p

import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver

class P2PConnectionManager {
    private var peerConnection: PeerConnection? = null
    var dataChannel: DataChannel? = null
        private set

    fun createPeerConnection(factory: PeerConnectionFactory): PeerConnection {
        val config = PeerConnection.RTCConfiguration(
            listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER)
        )
        peerConnection = factory.createPeerConnection(
            config,
            object : PeerConnection.Observer {
                override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {}
                override fun onIceConnectionReceivingChange(receiving: Boolean) {}
                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
                override fun onIceCandidate(candidate: IceCandidate?) {
                    candidate?.let {
                        Log.d(P2PConstants.TAG, "ICE candidate: \${it.sdp}")
                    }
                }
                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
                override fun onAddStream(stream: MediaStream?) {}
                override fun onRemoveStream(stream: MediaStream?) {}
                override fun onDataChannel(channel: DataChannel?) {
                    channel?.let {
                        dataChannel = it
                        Log.d(P2PConstants.TAG, "DataChannel diterima: \${it.label()}")
                        it.registerObserver(object : DataChannel.Observer {
                            override fun onMessage(buffer: DataChannel.Buffer?) {
                                buffer?.let { buf ->
                                    val msg = String(buf.data.array())
                                    Log.d(P2PConstants.TAG, "Pesan dari peer: \$msg")
                                }
                            }
                            override fun onBufferedAmountChange(amount: Long) {}
                            override fun onStateChange() {}
                        })
                    }
                }
                override fun onRenegotiationNeeded() {}
                override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {}
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
        if (dataChannel?.state() == DataChannel.State.OPEN) {
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
