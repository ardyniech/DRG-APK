package com.example.core.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class PttAudioRecorder(private val context: Context) {
    private val isRecording = AtomicBoolean(false)
    private val _rmsLevel = MutableStateFlow(0f)
    val rmsLevel: StateFlow<Float> = _rmsLevel.asStateFlow()

    private val sampleRate = PttAudioCodec.SAMPLE_RATE_HZ
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    suspend fun record(onCaptured: (String, Long) -> Unit) = withContext(Dispatchers.IO) {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (!granted || !isRecording.compareAndSet(false, true)) return@withContext
        val bufSize = maxOf(minBufferSize, 2048)
        val recorder = try {
            AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufSize)
        } catch (_: SecurityException) {
            isRecording.set(false); return@withContext
        } catch (_: Exception) {
            isRecording.set(false); return@withContext
        }
        if (recorder.state != AudioRecord.STATE_INITIALIZED) {
            recorder.release(); isRecording.set(false); return@withContext
        }
        val pcmList = mutableListOf<Short>()
        val buffer = ShortArray(bufSize / 2)
        val start = System.currentTimeMillis()
        try {
            recorder.startRecording()
            while (isRecording.get() && (System.currentTimeMillis() - start) < PttAudioCodec.MAX_DURATION_MS) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) {
                    for (i in 0 until read) pcmList.add(buffer[i])
                    _rmsLevel.value = PttAudioCodec.computeNormalizedRms(buffer, read)
                }
            }
        } catch (_: Exception) {
        } finally {
            try { recorder.stop() } catch (_: Exception) {}
            try { recorder.release() } catch (_: Exception) {}
            isRecording.set(false)
            _rmsLevel.value = 0f
        }
        val pcm = pcmList.toShortArray()
        val duration = PttAudioCodec.estimateDurationMs(pcm.size)
        if (duration >= PttAudioCodec.MIN_DURATION_MS) {
            val b64 = PttAudioCodec.encodePcmToBase64(pcm)
            if (b64.isNotBlank()) withContext(Dispatchers.Main) { onCaptured(b64, duration) }
        }
    }

    fun stop() { isRecording.set(false) }
}
