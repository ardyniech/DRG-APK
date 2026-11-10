package com.example.core.update

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/** State UI updater (dipakai [UpdateManager] dan kartu profil). */
sealed class UpdateUiState {
    data object Idle : UpdateUiState()
    data object Checking : UpdateUiState()
    data class UpToDate(val currentVersionCode: Int) : UpdateUiState()

    data class Available(
        val release: UpdateRelease,
        val patch: UpdatePatch?,
        val downloadBytes: Long,
        val fullBytes: Long
    ) : UpdateUiState() {
        val usesPatch: Boolean get() = patch != null
        val savingsPercent: Int
            get() = if (fullBytes > 0) (100 - downloadBytes * 100 / fullBytes).toInt() else 0
    }

    data class Downloading(val percent: Int, val bytes: Long, val total: Long) : UpdateUiState()
    data object Applying : UpdateUiState()
    data class ReadyToInstall(val apk: File) : UpdateUiState()
    data class Failed(val message: String) : UpdateUiState()
}

/**
 * Orkestrasi OTA: cek manifest → pilih patch yang cocok → unduh → apply →
 * verifikasi sha256 → siap install. Semua I/O di Dispatchers.IO.
 */
object UpdateManager {

    /** Cek versi terbaru tanpa mengunduh APK (cukup beberapa KB). */
    suspend fun check(
        context: Context,
        manifestUrl: String
    ): UpdateUiState = withContext(Dispatchers.IO) {
        val current = UpdateChecker.currentVersionCode(context)
        val release = UpdateChecker.fetchManifest(manifestUrl)
            ?: return@withContext UpdateUiState.Failed("Gagal baca update.json (offline / server?)")
        if (release.versionCode <= current) return@withContext UpdateUiState.UpToDate(current)

        val installedSha = UpdateChecker.installedApkSha256(context)
        val patch = release.patchFor(current, installedSha)
        UpdateUiState.Available(
            release = release,
            patch = patch,
            downloadBytes = patch?.sizeBytes ?: release.apkSizeBytes,
            fullBytes = release.apkSizeBytes
        )
    }

    /**
     * Unduh + siapkan APK baru. Kalau patch tersedia, hanya patch yang diunduh
     * lalu diterapkan ke APK terpasang (hemat kuota besar).
     */
    suspend fun prepare(
        context: Context,
        available: UpdateUiState.Available,
        onProgress: (UpdateUiState) -> Unit
    ): UpdateUiState = withContext(Dispatchers.IO) {
        try {
            val dir = ApkInstaller.updateDir(context)
            val version = available.release.versionCode
            val patch = available.patch

            if (patch != null) {
                val patchFile = File(dir, "update-$version.patch")
                downloadWithProgress(patch.url, patchFile, onProgress)
                if (!UpdateChecker.sha256(patchFile).equals(patch.sha256, ignoreCase = true)) {
                    patchFile.delete()
                    return@withContext UpdateUiState.Failed("Patch korup (sha256 beda)")
                }
                onProgress(UpdateUiState.Applying)
                val apk = File(dir, "app-$version.apk")
                DeltaPatchApplier.apply(File(context.applicationInfo.sourceDir), patchFile, apk)
                patchFile.delete()
                verifyAndFinish(apk, available.release.apkSha256, available.release.apkSizeBytes)
            } else {
                val apk = File(dir, "app-$version.apk")
                downloadWithProgress(available.release.apkUrl, apk, onProgress)
                verifyAndFinish(apk, available.release.apkSha256, available.release.apkSizeBytes)
            }
        } catch (cancel: kotlinx.coroutines.CancellationException) {
            throw cancel // jangan ubah pembatalan jadi "gagal"
        } catch (t: Exception) {
            UpdateUiState.Failed(t.message ?: t::class.java.simpleName)
        }
    }

    private fun downloadWithProgress(
        url: String,
        target: File,
        onProgress: (UpdateUiState) -> Unit
    ) {
        UpdateChecker.download(url, target) { done, total ->
            val percent = if (total > 0) (done * 100 / total).toInt() else 0
            onProgress(UpdateUiState.Downloading(percent, done, total))
        }
    }

    private fun verifyAndFinish(apk: File, expectedSha256: String, expectedSize: Long): UpdateUiState {
        if (expectedSize > 0 && apk.length() != expectedSize) {
            apk.delete()
            return UpdateUiState.Failed("Ukuran hasil tidak cocok manifest")
        }
        if (!UpdateChecker.sha256(apk).equals(expectedSha256, ignoreCase = true)) {
            apk.delete()
            return UpdateUiState.Failed("Hasil unduhan tidak cocok sha256")
        }
        return UpdateUiState.ReadyToInstall(apk)
    }
}
