package com.example.core.update

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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

object UpdateManager {

    suspend fun check(
        context: Context,
        manifestUrl: String,
        force: Boolean = false
    ): UpdateUiState = withContext(Dispatchers.IO) {
        val current = UpdateChecker.currentVersionCode(context)
        // Pakai cache kalau masih segar dan bukan paksa refresh.
        if (!force) {
            UpdateCheckCache.cached(current)?.let { r ->
                if (r.versionCode <= current) return@withContext UpdateUiState.UpToDate(current)
                val sha = UpdateChecker.installedApkSha256(context)
                return@withContext UpdateUiState.Available(
                    release = r, patch = r.patchFor(current, sha),
                    downloadBytes = r.patchFor(current, sha)?.sizeBytes ?: r.apkSizeBytes,
                    fullBytes = r.apkSizeBytes
                )
            }
        }
        val release = UpdateChecker.fetchManifest(manifestUrl)
            ?: return@withContext UpdateUiState.Failed("Gagal baca update.json (offline / server?)")
        UpdateCheckCache.store(current, release)
        if (release.versionCode <= current) return@withContext UpdateUiState.UpToDate(current)
        val sha = UpdateChecker.installedApkSha256(context)
        val patch = release.patchFor(current, sha)
        UpdateUiState.Available(release, patch, patch?.sizeBytes ?: release.apkSizeBytes, release.apkSizeBytes)
    }

    suspend fun prepare(
        context: Context,
        available: UpdateUiState.Available,
        onProgress: (UpdateUiState) -> Unit
    ): UpdateUiState = withContext(Dispatchers.IO) {
        // Cegah dua unduhan paralel (hemat tenaga).
        UpdateCheckCache.downloadLock.lock()
        try {
            val dir = ApkInstaller.updateDir(context)
            val version = available.release.versionCode
            val patch = available.patch
            val apk = File(dir, "app-$version.apk")
            if (patch != null) {
                val patchFile = File(dir, "update-$version.patch")
                downloadWithProgress(patch.url, patchFile, onProgress)
                if (!UpdateChecker.sha256(patchFile).equals(patch.sha256, ignoreCase = true)) {
                    patchFile.delete(); apk.delete()
                    return@withContext UpdateUiState.Failed("Patch korup (sha256 beda)")
                }
                onProgress(UpdateUiState.Applying)
                DeltaPatchApplier.apply(File(context.applicationInfo.sourceDir), patchFile, apk)
                patchFile.delete()
            } else {
                downloadWithProgress(available.release.apkUrl, apk, onProgress)
            }
            // Bersihkan aset lama untuk hemat ruang.
            ApkInstaller.cleanupOld(context, version)
            verifyAndFinish(context, apk, available.release.apkSha256, available.release.apkSizeBytes)
        } catch (cancel: kotlinx.coroutines.CancellationException) {
            throw cancel
        } catch (t: Exception) {
            UpdateUiState.Failed(t.message ?: t::class.java.simpleName)
        } finally {
            UpdateCheckCache.downloadLock.unlock()
        }
    }

    private fun downloadWithProgress(url: String, target: File, onProgress: (UpdateUiState) -> Unit) {
        UpdateChecker.download(url, target) { done, total ->
            val percent = if (total > 0) (done * 100 / total).toInt() else 0
            onProgress(UpdateUiState.Downloading(percent, done, total))
        }
    }

    private fun verifyAndFinish(context: Context, apk: File, expectedSha256: String, expectedSize: Long): UpdateUiState {
        if (expectedSize > 0 && apk.length() != expectedSize) {
            apk.delete(); return UpdateUiState.Failed("Ukuran hasil tidak cocok manifest")
        }
        if (!UpdateChecker.sha256(apk).equals(expectedSha256, ignoreCase = true)) {
            apk.delete(); return UpdateUiState.Failed("Hasil unduhan tidak cocok sha256")
        }
        if (!ApkInstaller.isValidApk(context, apk)) {
            apk.delete(); return UpdateUiState.Failed("APK tidak bisa dibaca (korup) — ulang unduh")
        }
        return UpdateUiState.ReadyToInstall(apk)
    }
}
