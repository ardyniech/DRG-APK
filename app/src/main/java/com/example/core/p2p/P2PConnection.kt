package com.example.core.p2p

import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.SessionDescription
import java.nio.ByteBuffer

class P2PConnectionManager {
    private var peerConnection: PeerConnection? = null
    var dataChannel: DataChannel? = null
        private set

    var onLocalIceCandidate: ((IceCandidate) -> Unit)? = null
    var onPeerMessageReceived: ((String) -> Unit)? = null
    var onConnectionStateChanged: ((PeerConnection.IceConnectionState) -> Unit)? = null

    fun createPeerConnection(factory: PeerConnectionFactory): PeerConnection {
        val config = PeerConnection.RTCConfiguration(listOf(P2PConfig.STUN_SERVER, P2PConfig.TURN_SERVER))
        val pc = factory.createPeerConnection(config, createObserver())
            ?: throw IllegalStateException("Gagal menginisialisasi WebRTC PeerConnection")
        peerConnection = pc
        dataChannel = pc.createDataChannel("drg_driver_channel", DataChannel.Init())?.also(::registerChannel)
        return pc
    }

    fun createOffer(onReady: (SessionDescription) -> Unit, onError: (String) -> Unit = {}) {
        val pc = peerConnection ?: return onError("PeerConnection belum dibuat")
        pc.createOffer(SimpleSdpObserver(onCreateSuccessCallback = { sdp ->
            pc.setLocalDescription(SimpleSdpObserver(onSetSuccessCallback = { onReady(sdp) }, onFailureCallback = onError), sdp)
        }, onFailureCallback = onError), MediaConstraints())
    }

    fun createAnswer(onReady: (SessionDescription) -> Unit, onError: (String) -> Unit = {}) {
        val pc = peerConnection ?: return onError("PeerConnection belum dibuat")
        pc.createAnswer(SimpleSdpObserver(onCreateSuccessCallback = { sdp ->
            pc.setLocalDescription(SimpleSdpObserver(onSetSuccessCallback = { onReady(sdp) }, onFailureCallback = onError), sdp)
        }, onFailureCallback = onError), MediaConstraints())
    }

    fun setRemoteDescription(sdp: SessionDescription, onDone: () -> Unit = {}, onError: (String) -> Unit = {}) {
        val pc = peerConnection ?: return onError("PeerConnection belum dibuat")
        pc.setRemoteDescription(SimpleSdpObserver(onSetSuccessCallback = onDone, onFailureCallback = onError), sdp)
    }

    fun addIceCandidate(candidate: IceCandidate) {
        peerConnection?.addIceCandidate(candidate)
    }

    fun isChannelOpen(): Boolean = dataChannel?.state() == DataChannel.State.OPEN

    fun sendData(message: String) {
        if (isChannelOpen()) {
            dataChannel?.send(DataChannel.Buffer(ByteBuffer.wrap(message.toByteArray(Charsets.UTF_8)), false))
        } else {
            Log.w(P2PConstants.TAG, "DataChannel belum terbuka")
        }
    }

    fun close() {
        dataChannel?.close()
        peerConnection?.close()
        peerConnection = null
        dataChannel = null
    }

    private fun registerChannel(channel: DataChannel) {
        channel.registerObserver(object : DataChannel.Observer {
            override fun onMessage(buffer: DataChannel.Buffer?) {
                val buf = buffer?.data ?: return
                val bytes = ByteArray(buf.remaining())
                buf.get(bytes)
                onPeerMessageReceived?.invoke(String(bytes, Charsets.UTF_8))
            }
            override fun onBufferedAmountChange(amount: Long) {}
            override fun onStateChange() {}
        })
    }

    private fun createObserver() = object : PeerConnection.Observer {
        override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
        override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
            if (state != null) onConnectionStateChanged?.invoke(state)
        }
        override fun onIceConnectionReceivingChange(receiving: Boolean) {}
        override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
        override fun onIceCandidate(candidate: IceCandidate?) {
            if (candidate != null) onLocalIceCandidate?.invoke(candidate)
        }
        override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
        override fun onAddStream(stream: MediaStream?) {}
        override fun onRemoveStream(stream: MediaStream?) {}
        override fun onDataChannel(channel: DataChannel?) {
            if (channel != null) {
                dataChannel = channel
                registerChannel(channel)
            }
        }
        override fun onRenegotiationNeeded() {}
        override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {}
    }
}
