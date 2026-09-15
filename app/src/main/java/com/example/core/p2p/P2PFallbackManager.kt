package com.example.core.p2p

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class P2PFallbackManager(
    private val syncQueueDao: com.example.core.database.dao.SyncQueueDao,
    private val networkMonitor: P2PNetworkMonitor
) {

    private val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)

    fun enqueuePendingAlert(message: String) {
        val entity = com.example.shared.models.PendingSyncEntity(
            id = java.util.UUID.randomUUID().toString(),
            entityType = P2PConstants.ENTITY_TYPE_P2P_ALERT,
            payloadJson = message,
            timestamp = System.currentTimeMillis()
        )
        scope.launch {
            syncQueueDao.insertSyncItem(entity)
            Log.w(P2PConstants.TAG, "P2P gagal, disimpan ke PendingSyncQueue")
        }
    }

    suspend fun retryPendingMessages() {
        if (!networkMonitor.isOnline()) return
        val pendingMessages = syncQueueDao.getAllPendingSyncs().first()
        for (msg in pendingMessages) {
            Log.d(P2PConstants.TAG, "Retrying pending message: ${msg.id}")
            syncQueueDao.removeSyncItem(msg.id)
        }
    }

    fun startAutoRetry() {
        scope.launch {
            while (true) {
                if (networkMonitor.isOnline()) {
                    retryPendingMessages()
                }
                delay(30000)
            }
        }
    }
}