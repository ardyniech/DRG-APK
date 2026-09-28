package com.example.core.p2p

import android.util.Log

class P2PSelfHostServer(
    private val port: Int = P2PConfig.SELF_HOST_PORT
) : Runnable {
    private var isRunning = false

    override fun run() {
        isRunning = true
        Log.d(P2PConstants.TAG, "Self-host server aktif di port \$port")
        while (isRunning) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                break
            }
        }
    }

    fun start() {
        Thread(this).start()
        Log.d(P2PConstants.TAG, "Self-host server dimulai")
    }

    fun stop() {
        isRunning = false
        Log.d(P2PConstants.TAG, "Self-host server dihentikan")
    }
}
