package com.example.core.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

sealed interface AiStreamChunk {
    data class Delta(val accumulated: String) : AiStreamChunk
    data class Completed(val finalText: String) : AiStreamChunk
    data class Error(val message: String) : AiStreamChunk
}

/**
 * Klien AI Driver berbasis Firebase AI Logic (Gemini 3.8 Flash) dengan streaming & timeout.
 */
object DrgAiAssistantEngine {
    private const val TAG = "DrgAiAssistant"
    private const val TIMEOUT_MS = 60_000L

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            systemInstruction = content {
                text(
                    "Kamu adalah DRG Asisten Rute, asisten resmi komunitas ojek online Driver Riang Gembira (DRG) Malang Raya. " +
                        "Berikan jawaban singkat, praktis, aman untuk pengemudi jalan raya (Bahasa Indonesia ramah, maks 4 poin). " +
                        "Prioritaskan keselamatan berkendara, info posko rehat, kewaspadaan jalur rawan/ranjau paku, " +
                        "dan arahkan keadaan darurat ke tombol merah SOS di aplikasi."
                )
            }
        )
    }

    fun streamAnswer(context: Context, prompt: String): Flow<AiStreamChunk> = flow {
        val cleanPrompt = prompt.trim()
        if (cleanPrompt.isBlank()) {
            emit(AiStreamChunk.Error("Masukkan pertanyaan terlebih dahulu."))
            return@flow
        }
        if (FirebaseApp.getApps(context).isEmpty()) {
            emit(AiStreamChunk.Error("Layanan Firebase AI belum terinisialisasi di perangkat ini."))
            return@flow
        }
        val accumulated = StringBuilder()
        try {
            withTimeout(TIMEOUT_MS) {
                model.generateContentStream(cleanPrompt).collect { chunk ->
                    val piece = chunk.text.orEmpty()
                    if (piece.isNotEmpty()) {
                        accumulated.append(piece)
                        emit(AiStreamChunk.Delta(accumulated.toString()))
                    }
                }
            }
            if (accumulated.isNotBlank()) {
                emit(AiStreamChunk.Completed(accumulated.toString()))
            } else {
                emit(AiStreamChunk.Error("Model Gemini mengembalikan respons kosong. Silakan coba lagi."))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Firebase AI generation timed out", e)
            emit(AiStreamChunk.Error("Permintaan AI melebihi batas waktu 60 detik. Periksa koneksi lalu coba lagi."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Firebase AI generation failed", e)
            emit(AiStreamChunk.Error(e.localizedMessage ?: "Gagal menghubungi layanan Firebase AI Logic."))
        }
    }
}
