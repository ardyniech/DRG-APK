package com.example.core.location

import android.content.Context
import android.os.BatteryManager
import com.example.core.cache.LocationSyncPowerProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LocationSyncMetrics(
    val currentIntervalSec: Int = 30,
    val batteryPercent: Int = 85,
    val isCharging: Boolean = false,
    val isStationary: Boolean = true,
    val profile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO,
    val estimatedBatterySavedPercent: Int = 35
)

class BatteryAwareLocationSyncer(
    private val context: Context?,
    private val scope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val onSyncLocation: suspend (lat: Double, lng: Double) -> Unit
) {
    private val _metrics = MutableStateFlow(LocationSyncMetrics())
    val metrics: StateFlow<LocationSyncMetrics> = _metrics.asStateFlow()

    private var syncJob: Job? = null
    private var lastLat: Double = -7.9822
    private var lastLng: Double = 112.6303

    fun startSync(profile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO) {
        syncJob?.cancel()
        syncJob = scope.launch(coroutineDispatcher) {
            while (isActive) {
                val battery = readBatteryStats()
                val effectiveProfile = if (battery.first < 20 && !battery.second) {
                    LocationSyncPowerProfile.ULTRA_SAVER
                } else {
                    profile
                }

                val isMoving = System.currentTimeMillis() % 120000L < 75000L
                val intervalSec = if (isMoving) {
                    effectiveProfile.normalIntervalSec
                } else {
                    effectiveProfile.stationaryIntervalSec
                }

                _metrics.value = LocationSyncMetrics(
                    currentIntervalSec = intervalSec,
                    batteryPercent = battery.first,
                    isCharging = battery.second,
                    isStationary = !isMoving,
                    profile = effectiveProfile,
                    estimatedBatterySavedPercent = if (effectiveProfile == LocationSyncPowerProfile.ULTRA_SAVER) 60 else 40
                )

                if (isMoving) {
                    lastLat += (Math.random() - 0.5) * 0.0004
                    lastLng += (Math.random() - 0.5) * 0.0004
                }
                onSyncLocation(lastLat, lastLng)

                delay(intervalSec * 1000L)
            }
        }
    }

    fun stopSync() {
        syncJob?.cancel()
        syncJob = null
    }

    fun setProfile(newProfile: LocationSyncPowerProfile) {
        startSync(newProfile)
    }

    private fun readBatteryStats(): Pair<Int, Boolean> {
        val bm = context?.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 82
        val isCharging = (bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS) == BatteryManager.BATTERY_STATUS_CHARGING)
        return Pair(level.coerceIn(5, 100), isCharging)
    }
}
