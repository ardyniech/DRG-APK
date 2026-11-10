package com.example.modules.profile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.core.update.ApkInstaller
import com.example.core.update.UpdateUiState
import java.util.Locale

/**
 * Teks status ringkas untuk kartu OTA: ukuran patch vs APK penuh, progres,
 * dan pesan gagal. Dipisah dari composable agar tiap file tetap < 125 baris.
 */
internal fun updateStatusText(state: UpdateUiState, busy: Boolean): String = when (state) {
    UpdateUiState.Idle -> "Ketuk untuk cek versi (hanya beberapa KB)"
    UpdateUiState.Checking -> "Memeriksa update.json…"
    is UpdateUiState.UpToDate -> "Sudah versi terbaru (v${state.currentVersionCode})"
    is UpdateUiState.Available -> {
        val size = formatMb(state.downloadBytes)
        val note = state.release.changelog.firstOrNull()?.take(46)
        val suffix = if (note.isNullOrBlank()) "" else " · $note"
        if (state.usesPatch) {
            "v${state.release.versionName} · patch $size (hemat ${state.savingsPercent}% dari ${formatMb(state.fullBytes)})$suffix"
        } else {
            "v${state.release.versionName} · unduh APK $size (patch belum tersedia)$suffix"
        }
    }
    is UpdateUiState.Downloading ->
        if (state.percent > 0) "Mengunduh ${state.percent}% · ${formatMb(state.bytes)}" else "Mengunduh…"
    UpdateUiState.Applying -> "Menerapkan patch ke APK terpasang…"
    is UpdateUiState.ReadyToInstall -> "Siap dipasang — ketuk Pasang"
    is UpdateUiState.Failed -> if (busy) "Memproses…" else "Gagal: ${state.message}"
}

internal fun formatMb(bytes: Long): String = String.format(Locale.US, "%.1f MB", bytes / 1048576.0)

internal fun handleInstallOutcome(
    outcome: ApkInstaller.InstallOutcome,
    setHint: (String?) -> Unit,
    setFailed: (String) -> Unit
) {
    when (outcome) {
        // Cache dibersihkan OS → file hilang, minta cek ulang (bukan crash).
        ApkInstaller.InstallOutcome.MISSING_FILE -> {
            setHint(null)
            setFailed("File update hilang — ketuk untuk cek ulang")
        }
        ApkInstaller.InstallOutcome.NEEDS_PERMISSION ->
            setHint("Izinkan \"Instal aplikasi tidak dikenal\", lalu ketuk Pasang lagi.")
        ApkInstaller.InstallOutcome.STARTED -> setHint(null)
    }
}

/**
 * Aksi otomatis: cek saat Profil dibuka + buka dialog instalasi saat siap.
 * Dipisah agar ProfileUpdateEntry tetap di bawah batas 125 baris.
 */
@Composable
internal fun ProfileUpdateAutoActions(
    state: UpdateUiState,
    context: Context,
    allowAutoCheck: Boolean,
    onCheck: () -> Unit,
    onMissingFile: () -> Unit,
    onPermissionNeeded: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        if (allowAutoCheck && state is UpdateUiState.Idle) onCheck()
    }
    LaunchedEffect(state) {
        val ready = state as? UpdateUiState.ReadyToInstall ?: return@LaunchedEffect
        when (ApkInstaller.install(context, ready.apk)) {
            ApkInstaller.InstallOutcome.MISSING_FILE -> onMissingFile()
            ApkInstaller.InstallOutcome.NEEDS_PERMISSION ->
                onPermissionNeeded("Izinkan \"Instal aplikasi tidak dikenal\", lalu ketuk Pasang lagi.")
            ApkInstaller.InstallOutcome.STARTED -> Unit
        }
    }
}
