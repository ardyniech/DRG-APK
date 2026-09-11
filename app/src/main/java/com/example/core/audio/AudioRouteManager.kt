package com.example.core.audio

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build

enum class AudioDeviceType(val label: String, val isHeadset: Boolean) {
    PHONE_SPEAKER("Speaker Perangkat", false),
    BLUETOOTH_INTERCOM("Intercom / Headset Helm (Bluetooth)", true),
    WIRED_HEADSET("Headset Kabel", true)
}

object AudioRouteManager {
    fun getConnectedAudioDeviceType(context: Context): AudioDeviceType {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return AudioDeviceType.PHONE_SPEAKER
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
                for (device in devices) {
                    when (device.type) {
                        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
                        AudioDeviceInfo.TYPE_BLUETOOTH_SCO,
                        AudioDeviceInfo.TYPE_BLE_HEADSET -> return AudioDeviceType.BLUETOOTH_INTERCOM
                        AudioDeviceInfo.TYPE_WIRED_HEADSET,
                        AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> return AudioDeviceType.WIRED_HEADSET
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                if (audioManager.isBluetoothA2dpOn || audioManager.isBluetoothScoOn) {
                    return AudioDeviceType.BLUETOOTH_INTERCOM
                }
                @Suppress("DEPRECATION")
                if (audioManager.isWiredHeadsetOn) {
                    return AudioDeviceType.WIRED_HEADSET
                }
            }
        } catch (_: Exception) {
        }
        return AudioDeviceType.PHONE_SPEAKER
    }

    fun isHeadsetOrIntercomConnected(context: Context): Boolean {
        return getConnectedAudioDeviceType(context).isHeadset
    }

    fun getRecommendedAlarmVolume(context: Context): Int {
        return if (isHeadsetOrIntercomConnected(context)) 70 else 100
    }
}
