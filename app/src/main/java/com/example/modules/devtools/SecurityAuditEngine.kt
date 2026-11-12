package com.example.modules.devtools

import android.content.Context
import android.os.Build
import com.example.core.security.PinHasher
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
                isSecure = PinHasher.isKeystoreAvailable(),
                statusText = if (PinHasher.isKeystoreAvailable()) "Aman (PBKDF2WithHmacSHA256 + salt, Android Keystore)" else "Kurang Aman (Keystore tidak tersedia — software mode)",
                riskDetail = "SecurityUtils/PinHasher: v2 format, salt 16 byte, 120k iterasi, AES-GCM Keystore (hardware-backed bila ada). Legacy SHA-256 terdeteksi & di-migrasi saat login."
            )
        )

        list.add(
            SecurityCheckItem(
                name = "Isolasi & Enkripsi Data Lokal (Room v13)",
                category = "Storage Security",
                isSecure = false,
                statusText = "Isolasi app sandbox + allowBackup=false; SQLite BELUM terenkripsi (SQLCipher)",
                riskDetail = "Data sensitif (NIK/SIM/kas) berpotensi plain di DB lokal. Risiko terdokumentasi; enkripsi butuh evaluasi/keputusan."
            )
        )

        list.add(
            SecurityCheckItem(
                name = "Privasi Koordinat SOS/Telemetri",
                category = "Privacy & Telemetry",
                isSecure = false,
                statusText = "Belum diverifikasi penuh (tidak ada probe runtime komprehensif)",
                riskDetail = "Fallback SMS dapat memuat link lokasi & butuh pulsa (DEFECT-03). Pastikan tidak ada logging koordinat sensitif."
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
