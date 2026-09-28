package com.example.core.p2p

import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.DataChannel.Buffer

class P2PDataChannel(private val channel: DataChannel) {
    init {
        channel.registerObserver(object : DataChannel.Observer {
            override fun onMessage(buffer: Buffer?) {
                buffer?.let {
                    val data = String(it.data.array())
                    Log.d(P2PConstants.TAG, "Pesan DataChannel: \$data")
                }
            }
            override fun onBufferedAmountChange(amount: Long) {}
            override fun onStateChange() {
                when (channel.state()) {
                    DataChannel.State.CONNECTING -> Log.d(P2PConstants.TAG, "DataChannel: CONNECTING")
                    DataChannel.State.OPEN -> Log.d(P2PConstants.TAG, "DataChannel: OPEN")
                    DataChannel.State.CLOSING -> Log.d(P2PConstants.TAG, "DataChannel: CLOSING")
                    DataChannel.State.CLOSED -> Log.d(P2PConstants.TAG, "DataChannel: CLOSED")
                    null -> {}
                }
            }
        })
    }

    fun send(message: String) {
        if (channel.state() == DataChannel.State.OPEN) {
            val buffer = Buffer(
                java.nio.ByteBuffer.wrap(message.toByteArray()),
                false
            )
            channel.send(buffer)
        } else {
            Log.w(P2PConstants.TAG, "Gagal kirim: DataChannel belum BUKA")
        }
    }

    fun close() {
        channel.close()
        Log.d(P2PConstants.TAG, "DataChannel ditutup")
    }
}
