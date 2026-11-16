package com.example.core.p2p

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Self-host TCP/HTTP micro-server per perangkat untuk verifikasi peer P2P lokal (< 125 baris).
 */
class P2PSelfHostServer(
    private val port: Int = P2PConfig.SELF_HOST_PORT
) : Runnable {
    private val running = AtomicBoolean(false)
    @Volatile private var serverSocket: ServerSocket? = null
    @Volatile private var workerThread: Thread? = null
    @Volatile var boundPort: Int = -1
        private set

    fun isActive(): Boolean = running.get()

    override fun run() {
        try {
            val socket = ServerSocket(port, 8, InetAddress.getByName("127.0.0.1")).apply {
                reuseAddress = true
                soTimeout = 1000
            }
            serverSocket = socket
            boundPort = socket.localPort
            running.set(true)
            while (running.get()) {
                try {
                    socket.accept().use { client ->
                        client.soTimeout = 1000
                        BufferedReader(InputStreamReader(client.getInputStream())).readLine()
                        val body = """{"service":"drg-p2p-host","port":$boundPort,"status":"LISTENING"}"""
                        val resp = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: ${body.length}\r\nConnection: close\r\n\r\n$body"
                        client.getOutputStream().write(resp.toByteArray(Charsets.UTF_8))
                        client.getOutputStream().flush()
                    }
                } catch (_: SocketTimeoutException) {
                    // Timeout periodik agar loop bisa mengecek flag running secara responsif
                } catch (_: SocketException) {
                    if (!running.get()) break
                }
            }
        } catch (e: Exception) {
            Log.w(P2PConstants.TAG, "Self-host listener berhenti: ${e.message}")
        } finally {
            running.set(false)
            runCatching { serverSocket?.close() }
            serverSocket = null
        }
    }

    fun start() {
        if (running.get()) return
        workerThread = Thread(this, "drg-p2p-selfhost").apply {
            isDaemon = true
            start()
        }
    }

    fun stop() {
        running.set(false)
        runCatching { serverSocket?.close() }
        workerThread?.interrupt()
        workerThread = null
        boundPort = -1
    }
}
