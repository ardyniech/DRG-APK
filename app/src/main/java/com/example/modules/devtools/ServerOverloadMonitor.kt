package com.example.modules.devtools

data class ServerOverloadStatus(
    val concurrentUsersEstimate: Int,
    val requestLatencyMs: Long,
    val isServerOverloaded: Boolean,
    val loadDescription: String
)

/**
 * Tanpa backend, beban server tidak bisa di-probe — laporkan UNKNOWN.
 * Angka 1420 user/145ms lama adalah fabrikasi dan sudah dihapus.
 */
object ServerOverloadMonitor {
    fun inspectServerLoad(): ServerOverloadStatus {
        // Tanpa backend multi-user, tidak ada telemetri server riil.
        return ServerOverloadStatus(
            concurrentUsersEstimate = 0,
            requestLatencyMs = -1L,
            isServerOverloaded = false,
            loadDescription = "UNKNOWN: belum ada backend multi-user — tidak ada telemetri server riil. Cek audit_minus.py BLOCKER firebase/fake-sync."
        )
    }

    fun hasSystemFailures(): Boolean {
        // Jujur: belum ada backend, jadi bukan "stable" — badge harus tanya user.
        // Kembalikan false hanya karena tidak ada probe gagal; status tetap UNKNOWN via inspectServerLoad.
        return false
    }
}
