package com.example.core.location

import com.example.core.cache.LocationSyncPowerProfile

data class LocationSyncMetrics(
    val currentIntervalSec: Int = 30,
    val batteryPercent: Int = 85,
    val isCharging: Boolean = false,
    val isStationary: Boolean = true,
    val profile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO,
    val estimatedBatterySavedPercent: Int = 35
)
