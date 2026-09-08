package com.example.core.viewmodel.coordinators

import android.content.Context
import android.content.SharedPreferences
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.DRGCacheManager
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.repository.DRGRepository
import com.example.core.sync.BackgroundSyncEngine
import com.example.shared.models.*
import com.example.shared.utils.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class RadarSafetyCoordinator(
    private val repository: DRGRepository,
    private val sharedPrefs: SharedPreferences?,
    private val scope: CoroutineScope,
    private val batteryManager: BatteryOptimizationManager,
    syncEngine: BackgroundSyncEngine,
    private val showToast: (String) -> Unit
) {
    private val cacheHelper = RadarCacheHelper(repository, scope, batteryManager, syncEngine, showToast)

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
        scope.launch { repository.saveAppSetting(AppStateSetting(key = "is_power_saver", boolValue = enabled)) }
        showToast(if (enabled) "Mode Hemat Baterai Aktif" else "Mode Performa Maksimal Aktif")
    }

    fun setDataSaverMode(enabled: Boolean) {
        batteryManager.setDataSaverMode(enabled)
        scope.launch { repository.saveAppSetting(AppStateSetting(key = "is_data_saver", boolValue = enabled)) }
        showToast(if (enabled) "Mode Hemat Kuota Aktif" else "Mode Jaringan Langsung")
    }

    fun precacheMap(ctx: Context, isPowerSaver: Boolean) = cacheHelper.precacheMap(isPowerSaver)
    fun clearMapCache(ctx: Context) = cacheHelper.clearMapCache(ctx)

    fun getCacheSizeDesc(ctx: Context): String {
        return try {
            val bytes = DRGCacheManager.getCacheSizeBytes(ctx)
            if (bytes <= 0L) {
                "0 B"
            } else if (bytes < 1024L * 1024L) {
                val kb = bytes.toDouble() / 1024.0
                String.format(java.util.Locale.US, "%.1f KB", kb)
            } else {
                val mb = bytes.toDouble() / (1024.0 * 1024.0)
                String.format(java.util.Locale.US, "%.1f MB", mb)
            }
        } catch (_: Exception) {
            "0 B"
        }
    }

    fun reportHazard(cur: DriverMember?, title: String, type: HazardType, loc: String, desc: String, lat: Double, lng: Double) {
        if (cur == null) return
        val hazard = HazardArea(
            id = "HZD-${UUID.randomUUID().toString().take(8)}",
            title = title, hazardType = type, locationName = loc, description = desc,
            lat = lat, lng = lng, reportedBy = cur.name, reporterRole = cur.role,
            confirmCount = 1, timeAgo = "Baru saja"
        )
        scope.launch { repository.addHazard(hazard); showToast("Area rawan ditandai di Radar!") }
    }

    fun confirmHazard(hazardId: String) {
        scope.launch { repository.confirmHazard(hazardId); showToast("Peringatan area rawan dikonfirmasi!") }
    }

    fun toggleLocationSharingConsent(cur: DriverMember?, consent: Boolean) {
        if (cur == null) return
        scope.launch {
            repository.updateMember(cur.copy(isLocationSharingConsent = consent))
            showToast(if (consent) "Izin lokasi live ke Radar AKTIF" else "Izin lokasi live NONAKTIF")
        }
    }

    fun saveNotificationPref(pref: NotificationPreference) {
        scope.launch { repository.saveNotificationPreference(pref); showToast("Preferensi notifikasi disimpan.") }
    }

    fun applyCommunityGpsPolicy(cur: DriverMember?) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (currentHour in 6..17) {
            val todayStr = DateTimeUtils.formatCurrentDateTimeReadable().take(11)
            val lastDate = sharedPrefs?.getString("last_gps_auto_trigger_date", "") ?: ""
            if (lastDate != todayStr && cur != null && !cur.isLocationSharingConsent) {
                scope.launch {
                    repository.updateMember(cur.copy(isLocationSharingConsent = true))
                    sharedPrefs?.edit()?.putString("last_gps_auto_trigger_date", todayStr)?.apply()
                    showToast("GPS Live diaktifkan otomatis (06.00 - 18.00)")
                }
            }
        }
    }
}
