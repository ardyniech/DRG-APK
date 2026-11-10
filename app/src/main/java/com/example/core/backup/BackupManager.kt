package com.example.core.backup

import android.content.Context
import com.example.core.database.AppDatabase
import java.io.File

/**
 * Export/import database lokal (Room/SQLite) ke file, agar data kas tidak hilang
 * saat ganti HP. Enkripsi AES-GCM via Android Keystore (fallback plaintext).
 */
object BackupManager {

    private const val DB_NAME = "drg_community.db"
    private const val SQLITE_MAGIC = "SQLite format 3\u0000"

    /** Export DB ke [target]. Return file target jika sukses. */
    fun export(context: Context, target: File): Result<File> = runCatching {
        target.parentFile?.mkdirs()
        val dbFile = context.getDatabasePath(DB_NAME)
        require(dbFile.exists()) { "Database belum ada di ${dbFile.absolutePath}" }
        val bytes = dbFile.readBytes()
        require(bytes.size > 0) { "Database kosong" }
        require(bytes.toString(Charsets.UTF_8).startsWith(SQLITE_MAGIC.take(6))) { "Bukan file SQLite" }
        val encrypted = BackupCrypto.encrypt(context, bytes)
        target.writeBytes(encrypted)
        require(target.length() > 0) { "Gagal menulis backup" }
        target
    }

    /** Import DB dari [source]. Validasi + ganti file DB. */
    fun import(context: Context, source: File): Result<Unit> = runCatching {
        require(source.exists() && source.length() > 0) { "File backup tidak ada/kosong" }
        val raw = source.readBytes()
        val bytes = if (BackupCrypto.isEncrypted(raw)) BackupCrypto.decrypt(context, raw) else raw
        require(bytes.size > 0) { "Hasil dekripsi kosong" }
        val header = bytes.copyOfRange(0, minOf(SQLITE_MAGIC.length, bytes.size)).toString(Charsets.UTF_8)
        require(header.startsWith("SQLite format")) { "Bukan file database valid" }

        val dbFile = context.getDatabasePath(DB_NAME)
        val backup = File(dbFile.parentFile, "$DB_NAME.bak")
        // Tutup Room sebelum mengganti file.
        AppDatabase.closeInstance()
        try {
            if (dbFile.exists()) dbFile.copyTo(backup, overwrite = true)
            // Bersihkan WAL/shm lama.
            File(dbFile.parentFile, "$DB_NAME-wal").delete()
            File(dbFile.parentFile, "$DB_NAME-shm").delete()
            dbFile.writeBytes(bytes)
            // Verifikasi bisa dibuka.
            AppDatabase.getInstance(context)
        } catch (e: Exception) {
            // Rollback ke backup lama bila gagal.
            if (backup.exists()) backup.copyTo(dbFile, overwrite = true)
            AppDatabase.getInstance(context)
            throw e
        }
        backup.delete()
    }

    /** Daftar file backup di [dir], terbaru dulu. */
    fun listBackups(dir: File): List<File> {
        if (!dir.exists()) return emptyList()
        return dir.listFiles { f -> f.isFile && f.name.endsWith(".bak") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }
}
