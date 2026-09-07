package com.example.core.battery

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BatteryOptimizationManager(
    private val sharedPrefs: SharedPreferences? = null
) {
    private val keyPowerSaver = "drg_power_saver_mode"
    private val keyDataSaver = "drg_data_saver_mode"
    private val keySavedDataBytes = "drg_saved_data_bytes"

    private val _isPowerSaverMode = MutableStateFlow(
        sharedPrefs?.getBoolean(keyPowerSaver, false) ?: false
    )
    val isPowerSaverMode: StateFlow<Boolean> = _isPowerSaverMode.asStateFlow()

    private val _isDataSaverMode = MutableStateFlow(
        sharedPrefs?.getBoolean(keyDataSaver, true) ?: true
    )
    val isDataSaverMode: StateFlow<Boolean> = _isDataSaverMode.asStateFlow()

    private val _savedDataBytes = MutableStateFlow(
        sharedPrefs?.getLong(keySavedDataBytes, 1024L * 1024L * 38L) ?: (1024L * 1024L * 38L)
    )
    val savedDataBytes: StateFlow<Long> = _savedDataBytes.asStateFlow()

    fun setPowerSaverMode(enabled: Boolean) {
        _isPowerSaverMode.value = enabled
        sharedPrefs?.edit()?.putBoolean(keyPowerSaver, enabled)?.apply()
    }

    fun setDataSaverMode(enabled: Boolean) {
        _isDataSaverMode.value = enabled
        sharedPrefs?.edit()?.putBoolean(keyDataSaver, enabled)?.apply()
    }

    fun recordDataSaved(bytes: Long) {
        val updated = _savedDataBytes.value + bytes
        _savedDataBytes.value = updated
        sharedPrefs?.edit()?.putLong(keySavedDataBytes, updated)?.apply()
    }

    fun getAnimationThrottleMs(): Int {
        return if (_isPowerSaverMode.value) 5000 else 2400
    }

    fun shouldRenderRadarSweep(): Boolean {
        return !_isPowerSaverMode.value
    }
}
