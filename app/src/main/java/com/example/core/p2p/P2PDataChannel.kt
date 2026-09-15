package com.example.core.p2p

import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.DataChannel.Buffer

class P2PDataChannel(private val channel: DataChannel) {

    init {
        channel.registerObserver(object : DataChannel.Observer {
            override fun onMessage(buffer: Buffer) {
                val data = String(buffer.data.array())
                Log.d(P2PConstants.TAG, "Pesan DataChannel: $data")
            }

            override fun onBufferedAmountChanged(amount: Long) {
                // Not critical
            }

            override fun onStateChanged(state: DataChannel.State) {
                when (state) {
                    DataChannel.State.OPEN -> Log.d(P2PConstants.TAG, "DataChannel state: BUKA")
                    DataChannel.State.CLOSING -> Log.d(P2PConstants.TAG, "DataChannel state: menutup")
                    DataChannel.State.CLOSED -> Log.d(P2PConstants.TAG, "DataChannel state: TUTUP")
                }
            }
        })
    }

    fun send(message: String) {
        if (channel.state == DataChannel.State.OPEN) {
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