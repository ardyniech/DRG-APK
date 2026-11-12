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
            val pm = context.getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
            when (pm?.currentThermalStatus) {
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
        val thermalHot = tempCelsius >= 39.0 || thermalStatus.contains("Severe") || thermalStatus.contains("Critical")
        val suggestion = if (thermalHot) {
            "Suhu/termal tinggi terdeteksi. Kurangi beban (peta/latar belakang) dan dinginkan perangkat."
        } else {
            "CPU % tidak diprobe runtime (UNKNOWN). Suhu/termal & thread count berbasis sinyal tersedia."
        }

        return DevicePerformanceMetrics(
            cpuUtilizationPercent = -1f, // tidak diprobe; hindari fabrikasi
            batteryTemperatureCelsius = tempCelsius,
            thermalStatusString = thermalStatus,
            isOverheating = thermalHot,
            cpuThreadCount = activeThreads,
            optimizationSuggestion = suggestion
        )
    }
}
