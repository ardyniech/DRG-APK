package com.example.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import androidx.core.content.ContextCompat
import com.example.core.cache.LocationSyncPowerProfile
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sqrt

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
) : SensorEventListener {
    private val _metrics = MutableStateFlow(LocationSyncMetrics())
    val metrics: StateFlow<LocationSyncMetrics> = _metrics.asStateFlow()

    private var syncJob: Job? = null
    private var lastLat: Double = -7.9822
    private var lastLng: Double = 112.6303

    private val fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }
    private val sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    @Volatile private var isDeviceInMotion: Boolean = false
    @Volatile private var lastMotionTimestamp: Long = 0L

    init {
        accelerometer?.let { sensor ->
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val magnitude = sqrt((x * x + y * y + z * z).toDouble())
        val deltaFromGravity = Math.abs(magnitude - SensorManager.GRAVITY_EARTH)
        if (deltaFromGravity > 1.2) {
            isDeviceInMotion = true
            lastMotionTimestamp = System.currentTimeMillis()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @SuppressLint("MissingPermission")
    private fun fetchRealLocationIfPermitted() {
        val ctx = context ?: return
        val hasFine = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasFine || hasCoarse) {
            runCatching {
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    if (location != null) {
                        lastLat = location.latitude
                        lastLng = location.longitude
                    }
                }
            }
        }
    }

    fun startSync(profile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO) {
        syncJob?.cancel()
        syncJob = scope.launch(coroutineDispatcher) {
            while (isActive) {
                fetchRealLocationIfPermitted()
                val battery = readBatteryStats()
                val effectiveProfile = if (battery.first < 20 && !battery.second) {
                    LocationSyncPowerProfile.ULTRA_SAVER
                } else {
                    profile
                }

                val now = System.currentTimeMillis()
                val isCurrentlyMoving = isDeviceInMotion && (now - lastMotionTimestamp < 15000L)
                val intervalSec = if (isCurrentlyMoving) effectiveProfile.normalIntervalSec else effectiveProfile.stationaryIntervalSec

                _metrics.value = LocationSyncMetrics(
                    currentIntervalSec = intervalSec,
                    batteryPercent = battery.first,
                    isCharging = battery.second,
                    isStationary = !isCurrentlyMoving,
                    profile = effectiveProfile,
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
        try { sensorManager?.unregisterListener(this) } catch (_: Exception) {}
    }

    fun setProfile(newProfile: LocationSyncPowerProfile) = startSync(newProfile)

    private fun readBatteryStats(): Pair<Int, Boolean> {
        val bm = context?.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 82
        val isCharging = (bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS) == BatteryManager.BATTERY_STATUS_CHARGING)
        return Pair(level.coerceIn(5, 100), isCharging)
    }
}
