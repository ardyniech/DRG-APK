package com.example.modules.profile

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
        if (state.usesPatch) {
            "v${state.release.versionName} · patch $size (hemat ${state.savingsPercent}% dari ${formatMb(state.fullBytes)})"
        } else {
            "v${state.release.versionName} · unduh APK $size (patch belum tersedia)"
        }
    }
    is UpdateUiState.Downloading ->
        if (state.percent > 0) "Mengunduh ${state.percent}% · ${formatMb(state.bytes)}" else "Mengunduh…"
    UpdateUiState.Applying -> "Menerapkan patch ke APK terpasang…"
    is UpdateUiState.ReadyToInstall -> "Siap dipasang — ketuk Pasang"
    is UpdateUiState.Failed -> if (busy) "Memproses…" else "Gagal: ${state.message}"
}

internal fun formatMb(bytes: Long): String = String.format(Locale.US, "%.1f MB", bytes / 1048576.0)
