package com.example.core.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.shared.models.CrashSensitivity
import kotlin.math.sqrt

class CrashDetectionManager(
    private val context: Context,
    private val onCrashDetected: (Float) -> Unit
) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var isListening = false
    private var sensitivity: CrashSensitivity = CrashSensitivity.MEDIUM
    private var lastTriggerTime: Long = 0L

    fun setSensitivity(newSensitivity: CrashSensitivity) {
        sensitivity = newSensitivity
    }

    fun startListening() {
        if (isListening) return
        try {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            accelerometer?.let {
                // SENSOR_DELAY_NORMAL saves battery while still detecting sharp g-force peaks
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
                isListening = true
            }
        } catch (_: Exception) {
            isListening = false
        }
    }

    fun stopListening() {
        if (!isListening) return
        try {
            sensorManager?.unregisterListener(this)
        } catch (_: Exception) {
        } finally {
            isListening = false
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val totalAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val gForce = totalAcceleration / SensorManager.GRAVITY_EARTH

        val now = System.currentTimeMillis()
        if (gForce >= sensitivity.thresholdG && (now - lastTriggerTime > 30000L)) {
            lastTriggerTime = now
            onCrashDetected(gForce)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
