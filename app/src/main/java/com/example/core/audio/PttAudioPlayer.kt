package com.example.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class PttAudioPlayer(private val context: Context) {
    private val isPlaying = AtomicBoolean(false)

    suspend fun play(audioBase64: String) = withContext(Dispatchers.IO) {
        if (audioBase64.isBlank() || !isPlaying.compareAndSet(false, true)) return@withContext
        val pcm = PttAudioCodec.decodeBase64ToPcm(audioBase64)
        if (pcm.isEmpty()) { isPlaying.set(false); return@withContext }

        val format = AudioFormat.ENCODING_PCM_16BIT
        val channel = AudioFormat.CHANNEL_OUT_MONO
        val sampleRate = PttAudioCodec.SAMPLE_RATE_HZ
        val bufSize = maxOf(AudioTrack.getMinBufferSize(sampleRate, channel, format), pcm.size * 2)

        (context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager)?.isSpeakerphoneOn =
            !AudioRouteManager.isHeadsetOrIntercomConnected(context)

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder().setEncoding(format).setSampleRate(sampleRate).setChannelMask(channel).build()
            )
            .setBufferSizeInBytes(bufSize)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        try {
            track.write(pcm, 0, pcm.size)
            track.play()
            delay(PttAudioCodec.estimateDurationMs(pcm.size) + 100L)
        } catch (_: Exception) {
        } finally {
            try { track.stop() } catch (_: Exception) {}
            try { track.release() } catch (_: Exception) {}
            isPlaying.set(false)
        }
    }
}
