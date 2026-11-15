package com.example.core.p2p

import android.content.Context
import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * WebRTC P2P Manager dengan deteksi antarmuka jaringan nyata (tanpa IP statis palsu).
 */
class P2PManager(private val context: Context) {
    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var connectionManager: P2PConnectionManager? = null
    private var peerConnection: PeerConnection? = null
    var dataChannel: DataChannel? = null
        private set
    private var isServerMode = false

    fun initialize() {
        if (peerConnectionFactory != null) return
        val options = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(false)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(PeerConnectionFactory.Options())
            .createPeerConnectionFactory()
    }

    fun startSelfHost(onLocalIce: ((IceCandidate) -> Unit)? = null) {
        isServerMode = true
        val factory = peerConnectionFactory ?: run { initialize(); peerConnectionFactory } ?: return
        val mgr = P2PConnectionManager().also { it.onLocalIceCandidate = onLocalIce }
        connectionManager = mgr
        peerConnection = mgr.createPeerConnection(factory)
        dataChannel = mgr.dataChannel
    }

    fun generateIceCandidate(localIp: String, remotePort: Int): IceCandidate {
        val candidateString = "candidate 1 UDP 2013266431 $localIp $remotePort typ host"
        return IceCandidate("data", 1, candidateString)
    }

    /** Membaca IPv4 aktif perangkat dari antarmuka jaringan nyata; string kosong bila offline. */
    fun discoverLocalIpAddress(): String {
        val preferred = listOf("tailscale0", "tun0", "wlan0", "eth0")
        return try {
            val ifaces = NetworkInterface.getNetworkInterfaces()?.toList().orEmpty()
                .filter { it.isUp && !it.isLoopback }
                .sortedBy { iface -> preferred.indexOf(iface.name).let { if (it == -1) 99 else it } }
            ifaces.firstNotNullOfOrNull { iface ->
                iface.inetAddresses?.toList()?.firstOrNull { addr ->
                    !addr.isLoopbackAddress && addr is Inet4Address
                }?.hostAddress?.takeIf { it.isNotBlank() }
            }.orEmpty()
        } catch (_: Exception) {
            ""
        }
    }

    fun connectToPeer(remoteIP: String, remotePort: Int, factory: PeerConnectionFactory) {
        val mgr = P2PConnectionManager()
        connectionManager = mgr
        peerConnection = mgr.createPeerConnection(factory)
        dataChannel = mgr.dataChannel
        val localIp = discoverLocalIpAddress()
        if (localIp.isNotBlank()) {
            mgr.addIceCandidate(generateIceCandidate(localIp, remotePort))
        }
        Log.d(P2PConstants.TAG, "WebRTC peer session dibuka untuk port $remotePort (localReady=${localIp.isNotBlank()}, remote=${remoteIP.isNotBlank()})")
    }

    fun createOffer(onReady: (SessionDescription) -> Unit, onError: (String) -> Unit = {}) {
        connectionManager?.createOffer(onReady, onError) ?: onError("Koneksi P2P belum siap")
    }

    fun applyRemoteOfferAndCreateAnswer(
        remoteSdp: SessionDescription, onAnswer: (SessionDescription) -> Unit, onError: (String) -> Unit = {}
    ) {
        val mgr = connectionManager ?: return onError("Koneksi P2P belum siap")
        mgr.setRemoteDescription(remoteSdp, onDone = { mgr.createAnswer(onAnswer, onError) }, onError = onError)
    }

    fun applyRemoteAnswer(remoteSdp: SessionDescription) {
        connectionManager?.setRemoteDescription(remoteSdp)
    }

    fun sendData(message: String) {
        connectionManager?.sendData(message)
    }

    fun close() {
        connectionManager?.close()
        connectionManager = null
        peerConnection = null
        dataChannel = null
    }
}