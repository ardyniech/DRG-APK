package com.example.modules.devtools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

data class NetworkProbeResult(
    val isOnline: Boolean,
    val networkType: String,
    val firestoreLatencyMs: Long,
    val aiGatewayLatencyMs: Long,
    val preFailureWarning: String? = null
)

object RealtimeNetworkProbe {
    suspend fun probe(context: Context): NetworkProbeResult = withContext(Dispatchers.IO) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNet = cm?.activeNetwork
        val caps = activeNet?.let { cm.getNetworkCapabilities(it) }
        val hasInternet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        val netType = when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "Wi-Fi (High Bandwidth)"
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Seluler 4G/5G"
            else -> "Terputus / Offline"
        }

        if (!hasInternet) {
            return@withContext NetworkProbeResult(
                isOnline = false, networkType = netType, firestoreLatencyMs = -1, aiGatewayLatencyMs = -1,
                preFailureWarning = "Gejala: Koneksi data terputus. Data lokal Room aktif; opsi SMS darurat (buka composer, butuh pulsa/aksi user) tersedia di fitur SOS."
            )
        }

        val firestorePing = measureSocketPing("firestore.googleapis.com", 443)
        val aiGatewayPing = measureSocketPing("generativelanguage.googleapis.com", 443)

        val warning = when {
            firestorePing > 400 -> "Gejala: Latensi tinggi (>400ms), rawan timeout pada sinkronisasi posisi radar driver."
            firestorePing == -1L -> "Gejala: Port SSL 443 Firestore unreachable. Cek firewall jaringan / DNS hotspot."
            else -> null
        }

        NetworkProbeResult(
            isOnline = true, networkType = netType, firestoreLatencyMs = firestorePing,
            aiGatewayLatencyMs = aiGatewayPing, preFailureWarning = warning
        )
    }

    private fun measureSocketPing(host: String, port: Int): Long {
        return try {
            val start = System.currentTimeMillis()
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), 1200)
            }
            System.currentTimeMillis() - start
        } catch (e: Exception) {
            -1L
        }
    }
}
