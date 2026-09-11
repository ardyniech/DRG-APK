package com.example.core.audio

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HelmetIntercomMonitor(private val context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val _isIntercomConnected = MutableStateFlow(AudioRouteManager.isHeadsetOrIntercomConnected(context))
    val isIntercomConnected: StateFlow<Boolean> = _isIntercomConnected.asStateFlow()

    private var audioDeviceCallback: AudioDeviceCallback? = null

    fun startMonitoring(onDisconnected: (String) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && audioManager != null) {
            audioDeviceCallback = object : AudioDeviceCallback() {
                override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                    _isIntercomConnected.value = AudioRouteManager.isHeadsetOrIntercomConnected(context)
                }

                override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                    val wasConnected = _isIntercomConnected.value
                    val nowConnected = AudioRouteManager.isHeadsetOrIntercomConnected(context)
                    _isIntercomConnected.value = nowConnected
                    if (wasConnected && !nowConnected) {
                        SosHapticManager.playEmergencyDispatchedHaptic()
                        onDisconnected("Koneksi Intercom Helm terputus. Audio dialihkan ke speaker perangkat.")
                    }
                }
            }
            audioManager.registerAudioDeviceCallback(audioDeviceCallback, null)
        }
    }

    fun stopMonitoring() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && audioManager != null && audioDeviceCallback != null) {
            audioManager.unregisterAudioDeviceCallback(audioDeviceCallback)
            audioDeviceCallback = null
        }
    }
}
