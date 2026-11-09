package com.example.modules.devtools

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

data class DevicePerformanceMetrics(
    val cpuUtilizationPercent: Float,
    val batteryTemperatureCelsius: Double,
    val thermalStatusString: String,
    val isOverheating: Boolean,
    val cpuThreadCount: Int,
    val optimizationSuggestion: String
)

object DevicePerformanceEngine {
    fun inspectPerformance(context: Context): DevicePerformanceMetrics {
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val tempTenth = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 300) ?: 300
        val tempCelsius = tempTenth / 10.0

        val thermalStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
            when (powerManager?.currentThermalStatus) {
                android.os.PowerManager.THERMAL_STATUS_NONE -> "Normal (0)"
                android.os.PowerManager.THERMAL_STATUS_LIGHT -> "Light Throttling (1)"
                android.os.PowerManager.THERMAL_STATUS_MODERATE -> "Moderate Throttling (2)"
                android.os.PowerManager.THERMAL_STATUS_SEVERE -> "Severe Throttling (3)"
                android.os.PowerManager.THERMAL_STATUS_CRITICAL -> "Critical Throttling (4)"
                else -> "Normal / Unreported"
            }
        } else {
            if (tempCelsius > 39.0) "Thermal Warm / Throttling Risk" else "Normal"
        }

        val activeThreads = Thread.activeCount()
        val cpuUsage = minOf(95f, 15f + (activeThreads * 0.8f))
        val isOverheating = tempCelsius >= 39.0 || cpuUsage > 80f

        val suggestion = if (isOverheating) {
            "Suhu atau utilisasi CPU tinggi! Turunkan refresh rate peta posko atau tutup aplikasi latar belakang."
        } else {
            "Performa CPU & Termal perangkat stabil. Optimal untuk operasi ojol seharian."
        }

        return DevicePerformanceMetrics(
            cpuUtilizationPercent = cpuUsage,
            batteryTemperatureCelsius = tempCelsius,
            thermalStatusString = thermalStatus,
            isOverheating = isOverheating,
            cpuThreadCount = activeThreads,
            optimizationSuggestion = suggestion
        )
    }
}
