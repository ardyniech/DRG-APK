package com.example.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class LocationSyncHelper(
    private val context: Context?,
    private val scope: CoroutineScope,
    private val onLocationUpdate: (lat: Double, lng: Double) -> Unit
) : SensorEventListener {
    @Volatile var isDeviceInMotion: Boolean = false
        private set
    @Volatile var lastMotionTimestamp: Long = 0L
        private set

    private val sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }
    private var locationCallback: LocationCallback? = null

    init {
        accelerometer?.let { sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val (x, y, z) = Triple(event.values[0], event.values[1], event.values[2])
        val magnitude = sqrt((x * x + y * y + z * z).toDouble())
        if (Math.abs(magnitude - SensorManager.GRAVITY_EARTH) > 1.2) {
            isDeviceInMotion = true
            lastMotionTimestamp = System.currentTimeMillis()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @SuppressLint("MissingPermission")
    fun registerLocationUpdates(intervalSec: Int, priority: Int, dispatcher: kotlinx.coroutines.CoroutineDispatcher) {
        val ctx = context ?: return
        val hasFine = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!hasFine && !hasCoarse) return

        fusedLocationClient?.let { client ->
            locationCallback?.let { client.removeLocationUpdates(it) }
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return
                    scope.launch(dispatcher) { onLocationUpdate(loc.latitude, loc.longitude) }
                }
            }
            locationCallback = callback
            val request = LocationRequest.Builder(priority, intervalSec * 1000L)
                .setMinUpdateIntervalMillis((intervalSec / 2).coerceAtLeast(1) * 1000L)
                .build()
            runCatching { client.requestLocationUpdates(request, callback, Looper.getMainLooper()) }
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchLastLocation(): Pair<Double, Double>? {
        val ctx = context ?: return null
        val hasFine = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!hasFine && !hasCoarse) return null

        var result: Pair<Double, Double>? = null
        runCatching {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { loc ->
                if (loc != null) result = Pair(loc.latitude, loc.longitude)
            }
        }
        return result
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            locationCallback = null
        }
    }

    fun unregisterSensor() {
        try { sensorManager?.unregisterListener(this) } catch (_: Exception) {}
    }

    fun readBatteryStats(): Pair<Int, Boolean> {
        val bm = context?.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 82
        val isCharging = (bm?.getIntProperty(BatteryManager.BATTERY_STATUS_CHARGING) == BatteryManager.BATTERY_STATUS_CHARGING)
        return Pair(level.coerceIn(5, 100), isCharging)
    }
}
