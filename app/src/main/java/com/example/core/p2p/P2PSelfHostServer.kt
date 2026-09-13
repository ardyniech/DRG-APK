package com.example.core.p2p

import android.util.Log
import org.webrtc.PeerConnection

class P2PSelfHostServer(

) : Runnable {

    override fun run() {
        // Device menjalankan server mendengarkan koneksi P2P masuk
        // Setiap incoming connection akan mendapatkan DataChannel sendiri
        Log.d(P2PConstants.TAG, "Self-host server aktif: mendengarkan koneksi P2P masuk")

        // Logika utama:
        // 1. Device mendengarkan port 8080 (dipanggil dari P2PManager.startSelfHost())
        // 2. Saat ada device lain connect → terima DataChannel
        // 3. Data yang datang langsung via DataChannel (tidak lewat server tengah)
        // 4. Fallback kalau gagal → PendingSyncQueue
    }
}