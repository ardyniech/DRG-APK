package com.example.modules.devtools

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager

data class DeviceOptimizationAdvice(
    val title: String,
    val priority: String,
    val currentDeviceMetric: String,
    val practicalOjolAdvice: String,
    val isCritical: Boolean
)

object DeviceAdvisorEngine {
    fun evaluateDevice(context: Context): List<DeviceOptimizationAdvice> {
        val list = mutableListOf<DeviceOptimizationAdvice>()

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        am?.getMemoryInfo(memInfo)
        val totalRamGb = (memInfo.totalMem / (1024.0 * 1024.0 * 1024.0))

        if (totalRamGb < 3.8 || am?.isLowRamDevice == true) {
            list.add(
                DeviceOptimizationAdvice(
                    title = "Optimasi RAM Entry-Level (< 4GB)",
                    priority = "Penting untuk Multitasking Ojol",
                    currentDeviceMetric = "RAM Fisik: ${String.format("%.1f", totalRamGb)} GB (Tersisa: ${memInfo.availMem / (1024 * 1024)} MB)",
                    practicalOjolAdvice = "Aktifkan 'Mode Hemat Kuota & Peta' di menu Profil DRG agar RAM tidak jenuh saat membuka aplikasi ojol utama bersamaan.",
                    isCritical = true
                )
            )
        }

        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 100
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
        val batteryPct = if (scale > 0) (level * 100 / scale) else 100
        val tempTenthCelsius = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val tempCelsius = tempTenthCelsius / 10.0

        if (batteryPct <= 30) {
            list.add(
                DeviceOptimizationAdvice(
                    title = "Baterai Lemah ($batteryPct%)",
                    priority = "Pencegahan HP Mati di Jalan",
                    currentDeviceMetric = "Status Baterai: $batteryPct% (Suhu: ${tempCelsius}°C)",
                    practicalOjolAdvice = "Turunkan interval GPS ke 5 detik di Setelan, atau colokkan charger holder motor agar radar darurat tidak terhenti.",
                    isCritical = true
                )
            )
        }

        if (tempCelsius >= 38.0) {
            list.add(
                DeviceOptimizationAdvice(
                    title = "Suhu Perangkat Hangat (${tempCelsius}°C)",
                    priority = "Thermal Throttling Protection",
                    currentDeviceMetric = "Suhu Baterai: ${tempCelsius}°C",
                    practicalOjolAdvice = "Hindari paparan sinar matahari langsung di stang motor; pasang kanopi mini holder HP agar GPS tidak thermal throttling.",
                    isCritical = false
                )
            )
        }

        val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val isIgnoringBattery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pm?.isIgnoringBatteryOptimizations(context.packageName) == true
        } else true

        if (!isIgnoringBattery) {
            list.add(
                DeviceOptimizationAdvice(
                    title = "Whitelist Optimasi Baterai OS",
                    priority = "Proteksi Layar Mati",
                    currentDeviceMetric = "OS Battery Optimizer: Mengontrol Aplikasi DRG",
                    practicalOjolAdvice = "Buka 'Pengaturan HP > Aplikasi > DRG > Baterai > Tidak Dibatasi' agar sensor getar darurat SOS tidak dimatikan Android saat layar HP mati.",
                    isCritical = true
                )
            )
        }

        return list
    }
}
