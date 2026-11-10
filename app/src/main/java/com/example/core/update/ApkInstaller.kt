package com.example.core.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

/**
 * Pasang APK hasil patch/unduhan lewat FileProvider + intent sistem.
 * Android tetap menampilkan dialog konfirmasi user (tidak bisa silent install
 * tanpa akses root/device owner) — itu batasan OS, bukan bug aplikasi.
 */
object ApkInstaller {

    fun authority(context: Context): String = "${context.packageName}.updates"

    /** Folder kerja updater di cache (aman dihapus OS kapan saja). */
    fun updateDir(context: Context): File = File(context.cacheDir, "updates").apply { mkdirs() }

    fun uriFor(context: Context, apk: File): Uri =
        FileProvider.getUriForFile(context, authority(context), apk)

    /** User sudah mengizinkan "Install unknown apps" untuk aplikasi ini? */
    fun canInstall(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
            context.packageManager.canRequestPackageInstalls()

    /** True kalau file APK valid & bisa dibaca Android (bukan korup). */
    fun isValidApk(context: Context, apk: File): Boolean {
        if (!apk.exists() || apk.length() < 1024) return false
        return context.packageManager.getPackageArchiveInfo(apk.absolutePath, 0) != null
    }

    /** Hasil percobaan membuka dialog instalasi. */
    enum class InstallOutcome { STARTED, NEEDS_PERMISSION, MISSING_FILE, INVALID_APK }

    /**
     * Buka dialog instalasi. Kalau izin sumber tak dikenal belum ada, buka
     * layar pengaturannya — supaya tombol "Pasang" tidak jadi tombol mati.
     */
    fun install(context: Context, apk: File): InstallOutcome {
        // Cache bisa dibersihkan OS: jangan biarkan FileProvider crash.
        if (!apk.exists()) return InstallOutcome.MISSING_FILE
        // Cegah "ekstrak dr zip error": pastikan APK valid sebelum install.
        if (!isValidApk(context, apk)) return InstallOutcome.INVALID_APK
        if (!canInstall(context)) {
            openInstallPermissionSettings(context)
            return InstallOutcome.NEEDS_PERMISSION
        }
        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uriFor(context, apk), "application/vnd.android.package-archive")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return InstallOutcome.STARTED
    }

    /** Hapus APK/patch lama di cache, sisakan versi [keepVersion]. */
    fun cleanupOld(context: Context, keepVersion: Int) {
        val dir = updateDir(context)
        dir.listFiles()?.forEach { f ->
            val name = f.name
            val keep = name.contains("-$keepVersion.") || name.contains("-$keepVersion-")
            if (!keep && (name.endsWith(".apk") || name.endsWith(".patch"))) f.delete()
        }
    }
    private fun openInstallPermissionSettings(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            .setData(Uri.parse("package:${context.packageName}"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
    }
}
