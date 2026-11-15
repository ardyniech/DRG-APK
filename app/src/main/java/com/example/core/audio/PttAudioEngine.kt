package com.example.core.audio

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

class PttAudioEngine(private val context: Context) {
    private val recorder = PttAudioRecorder(context)
    private val player = PttAudioPlayer(context)

    val rmsLevel: StateFlow<Float> = recorder.rmsLevel

    suspend fun recordTransmission(onAudioCaptured: (String, Long) -> Unit) {
        recorder.record(onAudioCaptured)
    }

    fun stopRecording() {
        recorder.stop()
    }

    suspend fun playAudioTransmission(audioBase64: String) {
        player.play(audioBase64)
    }
}
