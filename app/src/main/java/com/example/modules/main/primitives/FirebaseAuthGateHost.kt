package com.example.modules.main.primitives

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.firebase.FirestoreCommunityRepository
import com.example.core.firebase.FirestoreOperationsRepository
import com.example.core.firebase.authStateFlow
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.completeGoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow

/**
 * Mengamati FirebaseUser secara reaktif hanya bila FirebaseApp telah terinisialisasi.
 */
@Composable
fun rememberFirebaseAuthUser(context: Context): State<FirebaseUser?> {
    val isFirebaseReady = remember(context) { FirebaseApp.getApps(context).isNotEmpty() }
    val flow = remember(isFirebaseReady) {
        if (isFirebaseReady) Firebase.auth.authStateFlow() else emptyFlow()
    }
    val initial = remember(isFirebaseReady) {
        if (isFirebaseReady) Firebase.auth.currentUser else null
    }
    return flow.collectAsStateWithLifecycle(initialValue = initial)
}

/**
 * Menjalankan sinkronisasi Firestore hanya setelah pengguna terautentikasi penuh.
 */
@Composable
fun CloudSyncSessionEffect(viewModel: DRGViewModel, firebaseUser: FirebaseUser, context: Context) {
    LaunchedEffect(firebaseUser.uid) {
        if (!viewModel.isLoggedIn.value || viewModel.currentMemberId.value.isBlank()) {
            viewModel.completeGoogleSignIn(firebaseUser)
        }
    }

    LaunchedEffect(firebaseUser.uid) {
        if (FirebaseApp.getApps(context).isEmpty()) return@LaunchedEffect
        val communityRepo = runCatching { FirestoreCommunityRepository(context) }.getOrNull() ?: return@LaunchedEffect
        communityRepo.observeOwnMemberProfile(firebaseUser.uid)
            .catch { Log.w("CloudSync", "Member observer tertunda: ${it.message}") }
            .collect { /* Snapshot listener mempertahankan sinkronisasi profil cloud aktif */ }
    }

    LaunchedEffect(firebaseUser.uid) {
        if (FirebaseApp.getApps(context).isEmpty()) return@LaunchedEffect
        val opsRepo = runCatching { FirestoreOperationsRepository(context) }.getOrNull() ?: return@LaunchedEffect
        opsRepo.observeActiveAlerts()
            .catch { Log.w("CloudSync", "Alert observer tertunda: ${it.message}") }
            .collect { /* Snapshot listener mempertahankan pantauan SOS aktif */ }
    }
}
