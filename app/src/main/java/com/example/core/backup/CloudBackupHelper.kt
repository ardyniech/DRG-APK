package com.example.core.backup

import android.content.Context
import android.util.Base64
import com.example.core.firebase.FirestoreBackupSyncRepository
import java.io.File
import java.security.MessageDigest

/**
 * Menghubungkan enkripsi BackupManager lokal dengan penyimpanan cloud terenkripsi di Firestore.
 */
object CloudBackupHelper {
    private const val DB_SCHEMA_VERSION = 14
    private const val MAX_BASE64_BYTES = 480_000

    suspend fun uploadEncryptedDatabase(
        context: Context,
        repo: FirestoreBackupSyncRepository,
        backupId: String = "drg_latest_backup"
    ): Result<String> = runCatching {
        val tempFile = File(context.cacheDir, "drg_cloud_export.bak")
        try {
            val exported = BackupManager.export(context, tempFile).getOrThrow()
            val cipherBytes = exported.readBytes()
            val checksum = sha256Hex(cipherBytes)
            val encoded = Base64.encodeToString(cipherBytes, Base64.NO_WRAP)
            require(encoded.length <= MAX_BASE64_BYTES) {
                "Ukuran cadangan terenkripsi (${encoded.length} byte) melebihi batas dokumen Firestore."
            }
            repo.uploadEncryptedBackup(backupId, checksum, encoded, DB_SCHEMA_VERSION).getOrThrow()
            "Cadangan terenkripsi ($checksum) berhasil diunggah ke cloud."
        } finally {
            tempFile.delete()
        }
    }

    suspend fun restoreEncryptedDatabase(
        context: Context,
        repo: FirestoreBackupSyncRepository
    ): Result<String> = runCatching {
        val backup = repo.fetchLatestEncryptedBackup().getOrThrow()
            ?: error("Belum ada cadangan cloud terenkripsi untuk akun ini.")
        val cipherBytes = Base64.decode(backup.encryptedPayload, Base64.NO_WRAP)
        val actualChecksum = sha256Hex(cipherBytes)
        require(actualChecksum == backup.checksumSha256) {
            "Verifikasi SHA-256 cadangan cloud gagal (data korup/tidak utuh)."
        }
        val tempFile = File(context.cacheDir, "drg_cloud_import.bak")
        try {
            tempFile.writeBytes(cipherBytes)
            BackupManager.import(context, tempFile).getOrThrow()
            "Database lokal berhasil dipulihkan dari cadangan cloud (v${backup.schemaVersion})."
        } finally {
            tempFile.delete()
        }
    }

    private fun sha256Hex(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}
