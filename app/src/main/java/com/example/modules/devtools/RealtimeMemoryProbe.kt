package com.example.modules.devtools

import android.app.ActivityManager
import android.content.Context

data class MemoryProbeResult(
    val heapUsedMb: Long,
    val heapMaxMb: Long,
    val heapFreeMb: Long,
    val deviceAvailRamMb: Long,
    val deviceTotalRamMb: Long,
    val isLowRamDevice: Boolean,
    val isSystemLowMemory: Boolean,
    val activeThreadCount: Int,
    val earlySymptomWarning: String? = null
)

object RealtimeMemoryProbe {
    fun inspect(context: Context): MemoryProbeResult {
        val runtime = Runtime.getRuntime()
        val heapMaxMb = runtime.maxMemory() / (1024 * 1024)
        val heapTotalMb = runtime.totalMemory() / (1024 * 1024)
        val heapFreeMb = runtime.freeMemory() / (1024 * 1024)
        val heapUsedMb = heapTotalMb - heapFreeMb

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        am?.getMemoryInfo(memInfo)

        val availRamMb = memInfo.availMem / (1024 * 1024)
        val totalRamMb = memInfo.totalMem / (1024 * 1024)
        val isLowRam = am?.isLowRamDevice == true
        val isSysLow = memInfo.lowMemory
        val threads = Thread.activeCount()

        val warning = when {
            isSysLow -> "⚠️ Gejala Kritis: OS mendekati threshold low-memory, resiko proses background di-kill OS."
            (heapUsedMb.toFloat() / heapMaxMb.toFloat()) > 0.65f -> "⚠️ Gejala: Alokasi Heap >65%, Garbage Collector bekerja ekstra keras memicu frame hitch."
            threads > 35 -> "⚠️ Gejala: Thread count tinggi ($threads), potensi thread contention pada coroutine dispatcher."
            else -> null
        }

        return MemoryProbeResult(
            heapUsedMb = heapUsedMb, heapMaxMb = heapMaxMb, heapFreeMb = heapFreeMb,
            deviceAvailRamMb = availRamMb, deviceTotalRamMb = totalRamMb,
            isLowRamDevice = isLowRam, isSystemLowMemory = isSysLow,
            activeThreadCount = threads, earlySymptomWarning = warning
        )
    }
}
