package com.example.core.viewmodel.coordinators

import android.content.Context
import android.content.SharedPreferences
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.*
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.repository.DRGRepository
import com.example.core.sync.BackgroundSyncEngine
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class RadarSafetyCoordinator(
    private val repository: DRGRepository,
    sharedPrefs: SharedPreferences?,
    private val scope: CoroutineScope,
    private val batteryManager: BatteryOptimizationManager,
    syncEngine: BackgroundSyncEngine,
    private val showToast: (String) -> Unit
) {
    private val cacheHelper = RadarCacheHelper(repository, scope, batteryManager, syncEngine, showToast)
    private val gpsPolicyHelper = GpsPolicyHelper(repository, sharedPrefs, scope, showToast)

    private val _mapCachePolicy = MutableStateFlow(DRGCacheManager.cachePolicy)
    val mapCachePolicy: StateFlow<MapCachePolicy> = _mapCachePolicy.asStateFlow()

    private val _locationSyncProfile = MutableStateFlow(LocationSyncPowerProfile.ADAPTIVE_ECO)
    val locationSyncProfile: StateFlow<LocationSyncPowerProfile> = _locationSyncProfile.asStateFlow()

    var locationSyncer: BatteryAwareLocationSyncer? = null

    fun setMapCachePolicy(policy: MapCachePolicy) {
        _mapCachePolicy.value = policy
        cacheHelper.updateMapCachePolicy(policy)
    }

    fun setLocationSyncProfile(profile: LocationSyncPowerProfile) {
        _locationSyncProfile.value = profile
        cacheHelper.updateLocationSyncProfile(profile, locationSyncer)
    }

    fun setPowerSaverMode(enabled: Boolean) {
        batteryManager.setPowerSaverMode(enabled)
        scope.launch { runCatching { repository.saveAppSetting(AppStateSetting(key = "is_power_saver", boolValue = enabled)) } }
        showToast(if (enabled) "Mode Hemat Baterai Aktif" else "Mode Performa Maksimal Aktif")
    }

    fun setDataSaverMode(enabled: Boolean) {
        batteryManager.setDataSaverMode(enabled)
        scope.launch { runCatching { repository.saveAppSetting(AppStateSetting(key = "is_data_saver", boolValue = enabled)) } }
        showToast(if (enabled) "Mode Hemat Kuota Aktif" else "Mode Jaringan Langsung")
    }

    fun precacheMap(ctx: Context, isPowerSaver: Boolean) = cacheHelper.precacheMap(isPowerSaver)
    fun clearMapCache(ctx: Context) = cacheHelper.clearMapCache(ctx)

    fun getCacheSizeDesc(ctx: Context): String = runCatching {
        val bytes = DRGCacheManager.getCacheSizeBytes(ctx)
        when {
            bytes <= 0L -> "0 B"
            bytes < 1024L * 1024L -> String.format(java.util.Locale.US, "%.1f KB", bytes.toDouble() / 1024.0)
            else -> String.format(java.util.Locale.US, "%.1f MB", bytes.toDouble() / (1024.0 * 1024.0))
        }
    }.getOrDefault("0 B")

    fun reportHazard(cur: DriverMember?, title: String, type: HazardType, loc: String, desc: String, lat: Double, lng: Double) {
        if (cur == null) return
        val hazard = HazardArea(
            id = "HZD-${UUID.randomUUID().toString().take(8)}",
            title = title, hazardType = type, locationName = loc, description = desc,
            lat = lat, lng = lng, reportedBy = cur.name, reporterRole = cur.role,
            confirmCount = 1, timeAgo = "Baru saja"
        )
        scope.launch {
            runCatching { repository.addHazard(hazard) }
                .onSuccess { showToast("Area rawan ditandai di Radar!") }
                .onFailure { showToast("Gagal menandai area: ${it.localizedMessage}") }
        }
    }

    fun confirmHazard(hazardId: String) {
        scope.launch {
            runCatching { repository.confirmHazard(hazardId) }
                .onSuccess { showToast("Peringatan area rawan dikonfirmasi!") }
                .onFailure { showToast("Gagal konfirmasi: ${it.localizedMessage}") }
        }
    }

    fun toggleLocationSharingConsent(cur: DriverMember?, consent: Boolean) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.updateMember(cur.copy(isLocationSharingConsent = consent)) }
                .onSuccess { showToast(if (consent) "Izin lokasi live ke Radar AKTIF" else "Izin lokasi live NONAKTIF") }
                .onFailure { showToast("Gagal mengubah izin lokasi: ${it.localizedMessage}") }
        }
    }

    fun saveNotificationPref(pref: NotificationPreference) {
        scope.launch {
            runCatching { repository.saveNotificationPreference(pref) }
                .onSuccess { showToast("Preferensi notifikasi disimpan.") }
                .onFailure { showToast("Gagal menyimpan preferensi: ${it.localizedMessage}") }
        }
    }

    fun applyCommunityGpsPolicy(cur: DriverMember?) = gpsPolicyHelper.applyCommunityGpsPolicy(cur)
}
