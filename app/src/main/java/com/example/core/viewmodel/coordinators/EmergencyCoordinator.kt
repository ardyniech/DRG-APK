package com.example.core.viewmodel.coordinators

import android.content.Context
import android.content.SharedPreferences
import com.example.core.audio.SosAlarmSoundManager
import com.example.core.repository.DRGRepository
import com.example.core.sensors.CrashDetectionManager
import com.example.core.sync.BackgroundSyncEngine
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class EmergencyCoordinator(
    private val repository: DRGRepository,
    private val sharedPrefs: SharedPreferences?,
    private val context: Context?,
    private val scope: CoroutineScope,
    private val syncEngine: BackgroundSyncEngine,
    private val showToast: (String) -> Unit
) {
    private val _isSosAlarmSoundEnabled = MutableStateFlow(
        sharedPrefs?.getBoolean("pref_high_decibel_sos_alarm", true) ?: true
    )
    val isSosAlarmSoundEnabled: StateFlow<Boolean> = _isSosAlarmSoundEnabled.asStateFlow()

    private val _isCrashGuardEnabled = MutableStateFlow(
        sharedPrefs?.getBoolean("pref_crash_guard_enabled", true) ?: true
    )
    val isCrashGuardEnabled: StateFlow<Boolean> = _isCrashGuardEnabled.asStateFlow()

    private val _crashSensitivity = MutableStateFlow(
        CrashSensitivity.fromName(sharedPrefs?.getString("pref_crash_sensitivity", CrashSensitivity.MEDIUM.name))
    )
    val crashSensitivity: StateFlow<CrashSensitivity> = _crashSensitivity.asStateFlow()

    private val _crashDetectedEvent = MutableStateFlow<Float?>(null)
    val crashDetectedEvent: StateFlow<Float?> = _crashDetectedEvent.asStateFlow()

    private var crashDetectionManager: CrashDetectionManager? = null

    init {
        context?.let { ctx ->
            crashDetectionManager = CrashDetectionManager(ctx) { gForce ->
                scope.launch { if (_isCrashGuardEnabled.value) _crashDetectedEvent.value = gForce }
            }.apply {
                setSensitivity(_crashSensitivity.value)
                if (_isCrashGuardEnabled.value) startListening()
            }
        }
    }

    fun triggerEmergency(cur: DriverMember?, type: EmergencyType, msg: String, loc: String) {
        if (cur == null) return
        val alert = EmergencyAlert(
            id = "SOS-${UUID.randomUUID().toString().take(8)}",
            driverId = cur.id, driverName = cur.name, driverPhone = cur.phone,
            plateNumber = cur.motorcyclePlate, type = type, message = msg,
            lat = cur.currentLat, lng = cur.currentLng, locationName = loc, isActive = true
        )
        scope.launch {
            repository.addEmergencyAlert(alert)
            syncEngine.enqueueOptimisticAction("EMERGENCY", alert.id, alert.type.name)
            if (_isSosAlarmSoundEnabled.value) SosAlarmSoundManager.playHighDecibelAlarm(6000L)
            showToast("Sinyal SOS Darurat berhasil dipancarkan ke seluruh Satgas & Anggota!")
        }
    }

    fun resolveEmergency(alertId: String, cur: DriverMember?) {
        SosAlarmSoundManager.stopAlarm()
        scope.launch {
            repository.resolveEmergencyAlert(alertId, cur?.name ?: "Satgas")
            showToast("Laporan darurat ditandai Selesai.")
        }
    }

    fun respondEmergency(alertId: String) {
        scope.launch {
            repository.respondToEmergency(alertId)
            showToast("Anda merespons bantuan! Lokasi dan navigasi dibuka.")
        }
    }

    fun toggleSosAlarmSound(enabled: Boolean) {
        _isSosAlarmSoundEnabled.value = enabled
        sharedPrefs?.edit()?.putBoolean("pref_high_decibel_sos_alarm", enabled)?.apply()
        if (enabled) showToast("Sirine Darurat Desibel Tinggi DIAKTIFKAN")
        else { showToast("Sirine Darurat DINONAKTIFKAN (Mode Senyap)"); SosAlarmSoundManager.stopAlarm() }
    }

    fun testSosAlarmSound(onFinish: (() -> Unit)? = null) {
        showToast("Menguji Sirine SOS Desibel Tinggi (3 Detik)...")
        SosAlarmSoundManager.playHighDecibelAlarm(3000L, onFinish)
    }

    fun stopSosAlarmSound() = SosAlarmSoundManager.stopAlarm()

    fun toggleCrashGuard(enabled: Boolean) {
        _isCrashGuardEnabled.value = enabled
        sharedPrefs?.edit()?.putBoolean("pref_crash_guard_enabled", enabled)?.apply()
        if (enabled) { crashDetectionManager?.startListening(); showToast("Crash Guard DIAKTIFKAN") }
        else { crashDetectionManager?.stopListening(); showToast("Crash Guard DINONAKTIFKAN") }
    }

    fun setCrashSensitivity(s: CrashSensitivity) {
        _crashSensitivity.value = s
        sharedPrefs?.edit()?.putString("pref_crash_sensitivity", s.name)?.apply()
        crashDetectionManager?.setSensitivity(s)
        showToast("Sensitivitas Crash Guard: ${s.displayName}")
    }

    fun simulateCrashImpact(gForce: Float = 3.2f) { _crashDetectedEvent.value = gForce }
    fun dismissCrashCountdown() { _crashDetectedEvent.value = null; SosAlarmSoundManager.stopAlarm() }
    fun onCleared() { crashDetectionManager?.stopListening(); SosAlarmSoundManager.stopAlarm() }
}
