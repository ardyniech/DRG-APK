package com.example.core.p2p

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow

class P2PNetworkMonitor(private val context: android.content.Context) : AutoCloseable {

    private val connectivityManager =
        context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

    private val _networkState = kotlinx.coroutines.flow.MutableStateFlow(false)
    val networkState: Flow<Boolean> = _networkState.asStateFlow()

    fun isOnline(): Boolean = _networkState.value

    fun startMonitoring() {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            while (true) {
                val activeNetwork = connectivityManager.activeNetwork
                val networkInfo = connectivityManager.getNetworkInfo(activeNetwork)
                val isConnected = networkInfo?.isConnected == true
                _networkState.value = isConnected
                if (!isConnected) {
                    Log.d(P2PConstants.TAG, "Internet mati, pending messages akan disimpan")
                }
                delay(5000)
            }
        }
    }

    suspend fun triggerSync(syncQueueDao: com.example.core.database.dao.SyncQueueDao) {
        Log.d(P2PConstants.TAG, "Internet balik, memulai sync PendingSyncQueue")
        val pendingMessages = syncQueueDao.getAllPendingSyncs().first()
        for (msg in pendingMessages) {
            Log.d(P2PConstants.TAG, "Retrying pending message: ${msg.id}")
        }
    }

    override fun close() {
        // Stop monitoring
    }
}