package com.example.core.sync

import com.example.core.database.AppDatabase
import com.example.shared.models.PendingSyncEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

enum class SyncStatus(val label: String, val isOptimisticLocal: Boolean) {
    IDLE("Semua Data Tersinkron", false),
    SYNCING("Sinkronisasi Latar Belakang Berjalan...", false),
    SYNCED("Tersinkronisasi ke Cloud", false),
    OFFLINE_SAVED("Tersimpan di Lokal (Offline)", true)
}

class BackgroundSyncEngine(
    private val db: AppDatabase,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private var syncJob: Job? = null

    fun enqueueOptimisticAction(entityType: String, id: String, payload: String = "") {
        scope.launch(ioDispatcher) {
            db.syncQueueDao().insertSyncItem(
                PendingSyncEntity(id = id, entityType = entityType, payloadJson = payload, timestamp = System.currentTimeMillis())
            )
            _syncStatus.value = SyncStatus.OFFLINE_SAVED
            triggerBackgroundSync()
        }
    }

    fun triggerBackgroundSync(isPowerSaver: Boolean = false) {
        if (syncJob?.isActive == true) return

        syncJob = scope.launch(ioDispatcher) {
            _syncStatus.value = SyncStatus.SYNCING
            
            val delayDuration = if (isPowerSaver) 1500L else 300L
            delay(delayDuration)

            val batch = db.syncQueueDao().getNextBatch()
            if (batch.isNotEmpty()) {
                delay(300L) 
                
                batch.forEach { item ->
                    db.syncQueueDao().removeSyncItem(item.id)
                }
                
                _syncStatus.value = SyncStatus.SYNCED
                delay(300L)
            }
            
            val remaining = db.syncQueueDao().getNextBatch()
            _syncStatus.value = if (remaining.isNotEmpty()) SyncStatus.OFFLINE_SAVED else SyncStatus.IDLE
        }
    }
}
