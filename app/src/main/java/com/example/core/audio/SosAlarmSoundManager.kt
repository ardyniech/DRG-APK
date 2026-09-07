package com.example.core.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.*

object SosAlarmSoundManager {
    private var toneGenerator: ToneGenerator? = null
    private var alarmJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    fun playHighDecibelAlarm(durationMs: Long = 3000L, onFinish: (() -> Unit)? = null) {
        stopAlarm()
        SosHapticManager.playSosVibrationPattern()
        alarmJob = scope.launch {
            try {
                val generator = toneGenerator ?: ToneGenerator(AudioManager.STREAM_ALARM, 100).also {
                    toneGenerator = it
                }
                val startTime = System.currentTimeMillis()
                while (isActive && System.currentTimeMillis() - startTime < durationMs) {
                    generator.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 400)
                    delay(450)
                    generator.startTone(ToneGenerator.TONE_SUP_ERROR, 300)
                    delay(350)
                }
            } catch (_: Exception) {
            } finally {
                SosHapticManager.stopVibration()
                withContext(Dispatchers.Main) {
                    onFinish?.invoke()
                }
            }
        }
    }

    fun stopAlarm() {
        alarmJob?.cancel()
        alarmJob = null
        SosHapticManager.stopVibration()
        try {
            toneGenerator?.stopTone()
        } catch (_: Exception) {
        }
    }
}
