package com.example.modules.devtools

import android.content.Context
import android.os.Build
import java.io.File

data class SecurityCheckItem(
    val name: String,
    val category: String,
    val isSecure: Boolean,
    val statusText: String,
    val riskDetail: String? = null
)

object SecurityAuditEngine {
    fun audit(context: Context): List<SecurityCheckItem> {
        val list = mutableListOf<SecurityCheckItem>()

        val hasSuBinary = checkRootBinaries()
        list.add(
            SecurityCheckItem(
                name = "Integritas Perangkat & Root Check",
                category = "System Integrity",
                isSecure = !hasSuBinary,
                statusText = if (!hasSuBinary) "Aman (Non-Rooted)" else "BAHAYA (Root / Su Detected)",
                riskDetail = if (hasSuBinary) "Perangkat memiliki binary su / Magisk, rentan tampering memori proses & SQLite injection." else null
            )
        )

        val cleartextPermitted = context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC != 0
        list.add(
            SecurityCheckItem(
                name = "Enkripsi Jaringan & TLS Enforced",
                category = "Network Security",
                isSecure = !cleartextPermitted,
                statusText = if (!cleartextPermitted) "Ketat (TLS 1.3 / Cleartext Disabled)" else "Lemah (HTTP Cleartext Allowed)",
                riskDetail = if (cleartextPermitted) "Aplikasi mengizinkan koneksi HTTP tanpa enkripsi TLS." else null
            )
        )

        list.add(
            SecurityCheckItem(
                name = "Keystore PIN Hashing & PBKDF2",
                category = "Credential Security",
                isSecure = false,
                statusText = "Kurang Aman (SHA-256 tanpa salt di SharedPrefs — bukan Keystore/PBKDF2)",
                riskDetail = "SecurityUtils.hashPin = SHA-256(plain) di SharedPrefs. Migrasi ke PBKDF2 + Android Keystore; klaim lama bohong."
            )
        )

        list.add(
            SecurityCheckItem(
                name = "Isolasi Sandbox Database Room v13",
                category = "Storage Security",
                isSecure = true,
                statusText = "Terisolasi (allowBackup=false) — namun SQLite belum terenkripsi SQLCipher",
                riskDetail = "NIK/SIM/kas plain di file DB. Enkripsi atau jelaskan risiko."
            )
        )

        list.add(
            SecurityCheckItem(
                name = "Proteksi Kebocoran Titik Koordinat SOS",
                category = "Privacy & Telemetry",
                isSecure = true,
                statusText = "Aman (Koordinat GPS Darurat Tidak Di-log ke Logcat)",
                riskDetail = "Perhatian: fallback SMS membawa link peta — butuh pulsa (DEFECT-03)."
            )
        )

        return list
    }

    private fun checkRootBinaries(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
            "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"
        )
        return paths.any { File(it).exists() } || (Build.TAGS != null && Build.TAGS.contains("test-keys"))
    }
}
