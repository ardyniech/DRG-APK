package com.example.core.viewmodel

import android.app.Application
import com.example.core.backup.CloudBackupHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

fun DRGViewModel.uploadEncryptedCloudBackup() {
    val context = getApplication<Application>()
    if (Firebase.auth.currentUser == null) {
        showToast("Masuk dengan Google terlebih dahulu untuk mencadangkan ke cloud.")
        return
    }
    val repo = createCloudBackupSyncRepo(context) ?: run {
        showToast("Layanan cloud Firestore belum terhubung.")
        return
    }
    viewModelScope.launch {
        showToast("Mengenkripsi & mengunggah cadangan ke cloud...")
        CloudBackupHelper.uploadEncryptedDatabase(context, repo)
            .onSuccess { showToast(it) }
            .onFailure { showToast("Gagal unggah cadangan: ${it.localizedMessage}") }
    }
}

fun DRGViewModel.restoreEncryptedCloudBackup() {
    val context = getApplication<Application>()
    if (Firebase.auth.currentUser == null) {
        showToast("Masuk dengan Google terlebih dahulu untuk memulihkan dari cloud.")
        return
    }
    val repo = createCloudBackupSyncRepo(context) ?: run {
        showToast("Layanan cloud Firestore belum terhubung.")
        return
    }
    viewModelScope.launch {
        showToast("Mengunduh & mendekripsi cadangan cloud...")
        CloudBackupHelper.restoreEncryptedDatabase(context, repo)
            .onSuccess { showToast(it) }
            .onFailure { showToast("Gagal pulihkan cadangan: ${it.localizedMessage}") }
    }
}
