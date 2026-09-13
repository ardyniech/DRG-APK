package com.example.core.p2p

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.mutableStateFlow

class P2PNetworkMonitor(private val context: Context) : AutoCloseable {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _networkState = mutableStateFlow(false)
    val networkState: StateFlow<Boolean> = _networkState.asStateFlow()

    init {
        monitorNetwork()
    }

    private fun monitorNetwork() {
        launch(Dispatchers.IO) {
            while (true) {
                val activeNetwork = connectivityManager.activeNetwork
                val networkInfo = connectivityManager.getNetworkInfo(activeNetwork)
                val isConnected = networkInfo?.isConnected == true

                _networkState.value = isConnected

                // Jika internet mati dan punya pesan pending → coba retry
                if (!isConnected) {
                    Log.d(P2PConstants.TAG, "Internet mati, pending messages akan disimpan")
                    // TODO: Cek PendingSyncQueue dan tandai untuk retry nanti
                }

                // Cek lagi setiap 5 detik
                Thread.sleep(5000)
            }
        }
    }

    // Cek koneksi saat ini
    fun isOnline(): Boolean = _networkState.value

    // Trigger manual sync saat internet balik
    fun triggerSync() {
        Log.d(P2PConstants.TAG, "Internet balik, memulai sync PendingSyncQueue")
        // TODO: Ambil semua pending messages dan coba kirim via P2P
    }

    override fun close() {
        // Stop monitoring thread
    }
}