package com.example.core.sync

import android.content.Context
import android.os.BatteryManager
import com.example.core.battery.BatteryOptimizationManager
import kotlinx.coroutines.*

class BatteryAwareSyncScheduler(
    private val context: Context?,
    private val syncEngine: BackgroundSyncEngine,
    private val batteryOptimizationManager: BatteryOptimizationManager? = null,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var monitorJob: Job? = null

    fun readBatteryStats(): Pair<Int, Boolean> {
        val bm = context?.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 85
        val isCharging = (bm?.getIntProperty(BatteryManager.BATTERY_STATUS_CHARGING) == BatteryManager.BATTERY_STATUS_CHARGING)
        return Pair(level.coerceIn(5, 100), isCharging)
    }

    fun startBatteryAwareSyncMonitoring() {
        if (monitorJob?.isActive == true) return

        monitorJob = scope.launch(ioDispatcher) {
            while (isActive) {
                val (batteryPercent, isCharging) = readBatteryStats()
                val isPowerSaver = batteryOptimizationManager?.isPowerSaverMode?.value ?: false
                val isCriticallyLow = batteryPercent <= 15 && !isCharging

                if (!isCriticallyLow) {
                    syncEngine.triggerBackgroundSync(isPowerSaver = isPowerSaver)
                }

                val checkIntervalMs = if (isCriticallyLow) 60_000L else if (isPowerSaver) 30_000L else 10_000L
                delay(checkIntervalMs)
            }
        }
    }

    fun stopMonitoring() {
        monitorJob?.cancel()
        monitorJob = null
    }
}

