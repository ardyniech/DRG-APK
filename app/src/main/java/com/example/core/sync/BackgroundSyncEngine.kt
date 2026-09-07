package com.example.core.sync

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class SyncStatus(val label: String, val isOptimisticLocal: Boolean) {
    IDLE("Semua Data Tersinkron", false),
    SYNCING("Sinkronisasi Latar Belakang Berjalan...", false),
    SYNCED("Tersinkronisasi ke Cloud", false),
    OFFLINE_SAVED("Tersimpan di Lokal (Offline)", true)
}

data class PendingSyncItem(
    val id: String,
    val entityType: String,
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

class BackgroundSyncEngine(
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val pendingQueue = mutableListOf<PendingSyncItem>()
    private var syncJob: Job? = null

    fun enqueueOptimisticAction(entityType: String, id: String, payload: String = "") {
        pendingQueue.add(PendingSyncItem(id = id, entityType = entityType, payloadJson = payload))
        _syncStatus.value = SyncStatus.OFFLINE_SAVED
        triggerBackgroundSync()
    }

    fun triggerBackgroundSync(isPowerSaver: Boolean = false) {
        if (syncJob?.isActive == true) return

        syncJob = scope.launch(ioDispatcher) {
            _syncStatus.value = SyncStatus.SYNCING
            val delayDuration = if (isPowerSaver) 3000L else 1200L
            delay(delayDuration)

            // Simulate optimistic network reconciliation
            pendingQueue.clear()
            _syncStatus.value = SyncStatus.SYNCED
            delay(2500L)
            _syncStatus.value = SyncStatus.IDLE
        }
    }

    fun getPendingCount(): Int = pendingQueue.size
}
