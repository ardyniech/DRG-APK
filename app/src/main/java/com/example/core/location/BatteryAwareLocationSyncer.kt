package com.example.core.location

import android.content.Context
import com.example.core.cache.LocationSyncPowerProfile
import com.google.android.gms.location.Priority
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val helper = LocationSyncHelper(context, scope) { lat, lng ->
        lastLat = lat
        lastLng = lng
    }

    private var lastRegisteredInterval: Int = -1
    private var lastRegisteredPriority: Int = -1

    fun startSync(profile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO) {
        syncJob?.cancel()
        syncJob = scope.launch(coroutineDispatcher) {
            while (isActive) {
                helper.fetchLastLocation()?.let { (lat, lng) -> lastLat = lat; lastLng = lng }
                val battery = helper.readBatteryStats()
                val effectiveProfile = if (battery.first < 20 && !battery.second) LocationSyncPowerProfile.ULTRA_SAVER else profile
                val isMoving = helper.isDeviceInMotion && (System.currentTimeMillis() - helper.lastMotionTimestamp < 15000L)
                val intervalSec = if (isMoving) effectiveProfile.normalIntervalSec else effectiveProfile.stationaryIntervalSec

                val currentPriority = when (effectiveProfile) {
                    LocationSyncPowerProfile.HIGH_PRECISION -> Priority.PRIORITY_HIGH_ACCURACY
                    LocationSyncPowerProfile.ADAPTIVE_ECO -> Priority.PRIORITY_BALANCED_POWER_ACCURACY
                    LocationSyncPowerProfile.ULTRA_SAVER -> Priority.PRIORITY_LOW_POWER
                }

                if (currentPriority != lastRegisteredPriority || intervalSec != lastRegisteredInterval) {
                    helper.registerLocationUpdates(intervalSec, currentPriority, coroutineDispatcher)
                    lastRegisteredInterval = intervalSec
                    lastRegisteredPriority = currentPriority
                }

                _metrics.value = LocationSyncMetrics(
                    currentIntervalSec = intervalSec, batteryPercent = battery.first,
                    isCharging = battery.second, isStationary = !isMoving, profile = effectiveProfile,
                    estimatedBatterySavedPercent = if (effectiveProfile == LocationSyncPowerProfile.ULTRA_SAVER) 60 else 40
                )
                onSyncLocation(lastLat, lastLng)
                delay(intervalSec * 1000L)
            }
        }
    }

    fun stopSync() {
        syncJob?.cancel()
        syncJob = null
        helper.stopLocationUpdates()
        helper.unregisterSensor()
        lastRegisteredInterval = -1
        lastRegisteredPriority = -1
    }

    fun setProfile(newProfile: LocationSyncPowerProfile) = startSync(newProfile)
}
