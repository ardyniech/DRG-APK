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
    IDLE("Tersinkron & Siap", false),
    SYNCING("Menyinkronkan ke Cloud...", false),
    SYNCED("Tersinkron ke Cloud", false),
    OFFLINE_SAVED("Tertunda di Antrean Lokal", true)
}

/**
 * Engine sinkronisasi lokal -> Firestore. Item hanya dihapus dari antrean bila
 * unggah cloud berhasil (atau dalam mode uji unit tanpa konteks/uploader).
 */
class BackgroundSyncEngine(
    private val db: AppDatabase,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context? = null,
    @Volatile var cloudUploader: (suspend (PendingSyncEntity) -> Result<Unit>)? = null
) {
    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()
    private var syncJob: Job? = null
    private var lastNetworkState = false

    init { startNetworkRestorationListener() }

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
                if (isOnline && !lastNetworkState) triggerBackgroundSync()
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
            if (isNetworkConnected()) triggerBackgroundSync().join()
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
            delay(if (isPowerSaver) 1500L else 150L)
            val batch = db.syncQueueDao().getNextBatch()
            var uploadedCount = 0
            if (batch.isNotEmpty()) {
                val uploader = cloudUploader
                for (item in batch) {
                    val ok = if (uploader != null) {
                        uploader(item).onFailure {
                            Log.w("BackgroundSyncEngine", "Cloud sync tertunda untuk ${item.id}: ${it.message}")
                        }.isSuccess
                    } else {
                        context == null // Hanya drain lokal bila unit test tanpa context
                    }
                    if (ok) {
                        db.syncQueueDao().removeSyncItem(item.id)
                        uploadedCount++
                    }
                }
                if (uploadedCount > 0) {
                    _syncStatus.value = SyncStatus.SYNCED
                    delay(200L)
                }
            }
            val remaining = db.syncQueueDao().getNextBatch()
            _syncStatus.value = if (remaining.isNotEmpty()) SyncStatus.OFFLINE_SAVED else SyncStatus.IDLE
        }
        syncJob = job
        return job
    }
}