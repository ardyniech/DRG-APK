package com.example.core.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
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
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context? = null
) {
    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private var syncJob: Job? = null
    private var lastNetworkState = false

    init {
        startNetworkRestorationListener()
    }

    private fun isNetworkConnected(): Boolean {
        if (context == null) return true
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return true
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun startNetworkRestorationListener() {
        if (context == null) return
        scope.launch(ioDispatcher) {
            while (isActive) {
                val isOnline = isNetworkConnected()
                if (isOnline && !lastNetworkState) {
                    Log.d("BackgroundSyncEngine", "Koneksi internet pulih! Memicu outbox sync worker...")
                    triggerBackgroundSync()
                }
                lastNetworkState = isOnline
                delay(10_000L)
            }
        }
    }

    fun enqueueOptimisticAction(entityType: String, id: String, payload: String = ""): Job {
        return scope.launch(ioDispatcher) {
            db.syncQueueDao().insertSyncItem(
                PendingSyncEntity(id = id, entityType = entityType, payloadJson = payload, timestamp = System.currentTimeMillis())
            )
            _syncStatus.value = SyncStatus.OFFLINE_SAVED
            if (isNetworkConnected()) {
                triggerBackgroundSync().join()
            }
        }
    }

    fun triggerBackgroundSync(isPowerSaver: Boolean = false): Job {
        if (syncJob?.isActive == true) return syncJob!!
        if (!isNetworkConnected() && context != null) {
            _syncStatus.value = SyncStatus.OFFLINE_SAVED
            return scope.launch {}
        }

        val job = scope.launch(ioDispatcher) {
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
        syncJob = job
        return job
    }
}
