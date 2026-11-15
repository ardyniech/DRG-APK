package com.example.core.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

sealed interface GoogleAuthOutcome {
    data class Success(val user: FirebaseUser) : GoogleAuthOutcome
    data object Cancelled : GoogleAuthOutcome
    data class Error(val message: String) : GoogleAuthOutcome
}

fun FirebaseAuth.authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        trySend(firebaseAuth.currentUser)
    }
    addAuthStateListener(listener)
    awaitClose { removeAuthStateListener(listener) }
}

object GoogleAuthManager {
    private const val TAG = "GoogleAuthManager"

    suspend fun attemptAutoSignIn(context: Context): GoogleAuthOutcome {
        val clientId = resolveClientId(context) ?: return GoogleAuthOutcome.Error("Konfigurasi client ID belum tersedia.")
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(clientId)
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        return try {
            val result = CredentialManager.create(context).getCredential(context, request)
            exchangeGoogleCredential(result.credential)
        } catch (e: Exception) {
            Log.d(TAG, "Silent auto sign-in skipped: ${e.message}")
            GoogleAuthOutcome.Cancelled
        }
    }

    suspend fun signInWithGoogle(activity: Activity): GoogleAuthOutcome {
        val clientId = resolveClientId(activity) ?: return GoogleAuthOutcome.Error("Konfigurasi Google Sign-In tidak ditemukan.")
        val option = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        return try {
            val result = CredentialManager.create(activity).getCredential(activity, request)
            exchangeGoogleCredential(result.credential)
        } catch (e: GetCredentialCancellationException) {
            Log.w(TAG, "Google Sign-In dibatalkan oleh pengguna", e)
            GoogleAuthOutcome.Cancelled
        } catch (e: Exception) {
            Log.e(TAG, "Google Sign-In gagal", e)
            GoogleAuthOutcome.Error(e.localizedMessage ?: "Gagal masuk dengan Google")
        }
    }

    suspend fun signOut(context: Context) {
        withContext(NonCancellable) {
            try {
                CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
            } catch (e: Exception) {
                Log.w(TAG, "Gagal menghapus state kredensial: ${e.message}")
            } finally {
                Firebase.auth.signOut()
            }
        }
    }

    private suspend fun exchangeGoogleCredential(credential: Credential): GoogleAuthOutcome {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val authCred = GoogleAuthProvider.getCredential(googleIdToken, null)
            val result = Firebase.auth.signInWithCredential(authCred).await()
            val user = result.user ?: return GoogleAuthOutcome.Error("Sesi Firebase Auth kosong setelah login Google.")
            return GoogleAuthOutcome.Success(user)
        }
        return GoogleAuthOutcome.Error("Tipe kredensial Google tidak dikenali.")
    }

    private fun resolveClientId(context: Context): String? = runCatching {
        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        if (resId != 0) context.getString(resId).takeIf { it.isNotBlank() } else null
    }.getOrNull()
}
