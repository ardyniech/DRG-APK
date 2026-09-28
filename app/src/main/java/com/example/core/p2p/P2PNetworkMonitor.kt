package com.example.core.p2p

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.core.database.dao.SyncQueueDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class P2PNetworkMonitor(private val context: Context) : AutoCloseable {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _networkState = MutableStateFlow(false)
    val networkState: Flow<Boolean> = _networkState.asStateFlow()

    fun isOnline(): Boolean = _networkState.value

    fun startMonitoring() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val activeNetwork = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                val isConnected = networkCapabilities != null
                _networkState.value = isConnected
                if (!isConnected) {
                    Log.d(P2PConstants.TAG, "Internet mati, pending messages akan disimpan")
                }
                delay(5000)
            }
        }
    }

    suspend fun triggerSync(syncQueueDao: SyncQueueDao) {
        Log.d(P2PConstants.TAG, "Internet balik, memulai sync PendingSyncQueue")
        val pendingMessages = syncQueueDao.getAllPendingSyncs().first()
        for (msg in pendingMessages) {
            Log.d(P2PConstants.TAG, "Retrying pending message: \${msg.id}")
        }
    }

    override fun close() {
        // Stop monitoring
    }
}
