package com.example.core.p2p

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class P2PFallbackManager(
    private val db: AppDatabase,
    private val syncQueueDao: SyncQueueDao,
    private val networkMonitor: P2PNetworkMonitor
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    // Kirim data: coba P2P dulu, kalau gagal simpan ke PendingSyncQueue
    suspend fun sendWithFallback(targetId: String, message: String) {
        val connected = tryP2PConnection(targetId)

        if (connected) {
            // P2P berhasil → kirim langsung
            P2PManager.sendData(message)
            Log.d(P2PConstants.TAG, "P2P berhasil, data terkirim langsung")
        } else {
            // P2P gagal → simpan ke PendingSyncQueue untuk retry nanti
            syncQueueDao.insertSyncItem(
                PendingSyncEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    entityType = "P2P_ALERT",
                    payloadJson = message,
                    timestamp = System.currentTimeMillis()
                )
            )
            Log.w(P2PConstants.TAG, "P2P gagal, disimpan ke PendingSyncQueue untuk retry")

            // Jika internet balik → coba retry otomatis
            if (!networkMonitor.isOnline) {
                networkMonitor.triggerSync()
            }
        }
    }

    // Coba connect ke target via P2P
    private fun tryP2PConnection(targetId: String): Boolean {
        // Logika sederhana: coba connect via WebRTC STUN
        // Return true jika connection berhasil
        // Return false jika gagal (NAT block, mati internet, dsb)
        return false // Placeholder - implementasi nyata butuh WebRTC handshake
    }

    // Auto-retry saat koneksi balik
    fun startAutoRetry() {
        scope.launch {
            while (true) {
                if (networkMonitor.isOnline()) {
                    // Ambil pending messages dari queue
                    val pendingMessages = syncQueueDao.getAllPendingSyncs().await()
                    for (msg in pendingMessages) {
                        tryP2PConnection(msg.entityType)
                        // Jika sukses → hapus dari queue
                        syncQueueDao.removeSyncItem(msg.id)
                    }
                }
                // Cek lagi setiap 30 detik
                Thread.sleep(30000)
            }
        }
    }
}