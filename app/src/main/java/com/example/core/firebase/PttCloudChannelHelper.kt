package com.example.core.firebase

import android.content.Context
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Jembatan PTT cloud Firestore untuk PttWalkieTalkieDialog (< 125 baris).
 */
object PttCloudChannelHelper {

    fun isSignedIn(context: Context): Boolean =
        FirebaseApp.getApps(context).isNotEmpty() && Firebase.auth.currentUser != null

    fun currentUid(context: Context): String? =
        if (FirebaseApp.getApps(context).isNotEmpty()) Firebase.auth.currentUser?.uid else null

    fun createRepo(context: Context): FirestorePttTransmissionRepository? {
        if (!isSignedIn(context)) return null
        return runCatching {
            val db = FirebaseFirestore.getInstance(context.applicationContext.getString(R.string.firestore_database_id))
            FirestorePttTransmissionRepository(db, Firebase.auth)
        }.getOrNull()
    }

    suspend fun publishVoiceBurst(
        context: Context,
        channelId: String,
        audioBase64: String,
        durationMs: Long
    ): Result<String> {
        val repo = createRepo(context)
            ?: return Result.failure(IllegalStateException("Masuk dengan akun Google untuk menyiarkan PTT ke cloud."))
        val sender = Firebase.auth.currentUser?.displayName?.ifBlank { "Driver DRG" } ?: "Driver DRG"
        return repo.publishPttTransmission(sender, channelId, audioBase64, durationMs)
    }

    fun observeIncomingBursts(context: Context, channelId: String) =
        createRepo(context)?.observeLivePttChannel(channelId) ?: emptyFlow()
}
