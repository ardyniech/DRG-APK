package com.example.core.audio

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object SosHapticManager {
    private var vibrator: Vibrator? = null

    fun initialize(context: Context) {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun playSosVibrationPattern() {
        try {
            val v = vibrator ?: return
            if (!v.hasVibrator()) return

            // Morse SOS pattern: ... --- ... (short 150ms, long 350ms)
            val timings = longArrayOf(0, 150, 100, 150, 100, 150, 200, 350, 150, 350, 150, 350, 200, 150, 100, 150, 100, 150, 600)
            val amplitudes = intArrayOf(0, 200, 0, 200, 0, 200, 0, 255, 0, 255, 0, 255, 0, 200, 0, 200, 0, 200, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(timings, amplitudes, 0)
                v.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(timings, 0)
            }
        } catch (_: Exception) {
        }
    }

    fun playCountdownTick(secondsRemaining: Int) {
        try {
            val v = vibrator ?: return
            if (!v.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val duration = if (secondsRemaining <= 1) 90L else 50L
                val amplitude = (255 - secondsRemaining * 25).coerceIn(120, 255)
                v.vibrate(VibrationEffect.createOneShot(duration, amplitude))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(50L)
            }
        } catch (_: Exception) {
        }
    }

    fun playSuccessHaptic() {
        try {
            val v = vibrator ?: return
            if (!v.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 40, 50, 40)
                val amplitudes = intArrayOf(0, 100, 0, 140)
                v.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(40L)
            }
        } catch (_: Exception) {
        }
    }

    fun playEmergencyDispatchedHaptic() {
        try {
            val v = vibrator ?: return
            if (!v.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 120, 70, 120, 70, 250)
                val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
                v.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(longArrayOf(0, 120, 70, 120, 70, 250), -1)
            }
        } catch (_: Exception) {
        }
    }

    fun stopVibration() {
        try {
            vibrator?.cancel()
        } catch (_: Exception) {
        }
    }
}
