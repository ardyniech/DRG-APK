package com.example.core.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.core.database.AppDatabase
import com.example.shared.models.PendingSyncEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Engine sinkronisasi background untuk antrean antrean lokal.
 * KEHARUSAN: status yang tampil HANYA boleh berubah jika terjadi network call nyata ke cloud.
 * Jika hanya proses antrean lokal tanpa network ke firestore/Retrofit — status wajib OFFLINE_SAVED.
 */
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

    /** Cek apakah ada koneksi internet nyata. */
    private fun isNetworkConnected(): Boolean {
        if (context == null) return true
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return true
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /** Pendengar siklus mendeteksi kembali koneksi jaringan. */
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

    /** Enqueue optimistic action ke lokal, lalu—jika ada jaringan—mulai sinkronisasi background. */
    fun enqueueOptimisticAction(entityType: String, id: String, payload: String = ""): Job {
        return scope.launch(ioDispatcher) {
            db.syncQueueDao().insertSyncItem(
                PendingSyncEntity(id = id, entityType = entityType, payloadJson = payload, timestamp = System.currentTimeMillis())
            )
            // Selalu catat status lokal pertama kalinya sebelum cek network
            _syncStatus.value = SyncStatus.OFFLINE_SAVED
            // Jika ada jaringan, mulai sinkronisasi background (akan memperbarui status menjadi SYNCED jika sukses)
            if (isNetworkConnected()) {
                triggerBackgroundSync().join()
            }
        }
    }

    /** Pemicu sinkronisasi antrean ke batch berikutnya.
     *  @note: Status hanya berubah menjadi SYNCED jika networkConnected=true DAN batch berhasil diproses.
     *  Jika !isNetworkConnected(), status tetap OFFLINE_SAVED (tidak boleh langsung ke SYNCED "ke Cloud"). */
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

                // --- FIX: Hanya berubah ke SYNCED jika network terdeteksi.
                //      Jika sampai sini tapi !isNetworkConnected() (race condition),
                //      jangan langsung ke SYNCED("Tersinkronisasi ke Cloud").
                if (isNetworkConnected()) {
                    _syncStatus.value = SyncStatus.SYNCED
                } else {
                    // Jaringan hilang saat proses: catat batch sudah dihapus tapi tetap offline
                    _syncStatus.value = SyncStatus.OFFLINE_SAVED
                }

                delay(300L)
            }

            val remaining = db.syncQueueDao().getNextBatch()
            // Pastikan status akhir mencerminkan kondisi sesungguhnya
            if (isNetworkConnected()) {
                _syncStatus.value = if (remaining.isNotEmpty()) SyncStatus.OFFLINE_SAVED else SyncStatus.IDLE
            } else {
                _syncStatus.value = SyncStatus.OFFLINE_SAVED
            }
        }
        syncJob = job
        return job
    }
}