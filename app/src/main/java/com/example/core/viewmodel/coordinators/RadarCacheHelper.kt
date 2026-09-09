package com.example.core.viewmodel.coordinators

import android.content.Context
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.DRGCacheManager
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.repository.DRGRepository
import com.example.core.sync.BackgroundSyncEngine
import com.example.shared.models.AppStateSetting
import com.example.shared.models.MapTileMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RadarCacheHelper(
    private val repository: DRGRepository,
    private val scope: CoroutineScope,
    private val batteryManager: BatteryOptimizationManager,
    private val syncEngine: BackgroundSyncEngine,
    private val showToast: (String) -> Unit
) {
    fun updateMapCachePolicy(policy: MapCachePolicy) {
        DRGCacheManager.cachePolicy = policy
        DRGCacheManager.isOfflineModeForced = (policy == MapCachePolicy.OFFLINE_ONLY)
        scope.launch { runCatching { repository.saveAppSetting(AppStateSetting(key = "map_cache_policy", stringValue = policy.name)) } }
        showToast("Kebijakan Cache: ${policy.title}")
    }

    fun updateLocationSyncProfile(profile: LocationSyncPowerProfile, syncer: BatteryAwareLocationSyncer?) {
        syncer?.setProfile(profile)
        scope.launch { runCatching { repository.saveAppSetting(AppStateSetting(key = "location_sync_profile", stringValue = profile.name)) } }
        showToast("Profil GPS: ${profile.title}")
    }

    fun precacheMap(isPowerSaver: Boolean) {
        scope.launch {
            runCatching {
                showToast("Mengunduh pre-cache peta area Malang...")
                syncEngine.triggerBackgroundSync(isPowerSaver)
                batteryManager.recordDataSaved(14L * 1024L * 1024L)
                val initialTiles = listOf(
                    MapTileMetadata("osm_14_13045_8496", 14, 13045, 8496, "OSM", 21500L),
                    MapTileMetadata("osm_14_13046_8496", 14, 13046, 8496, "OSM", 19800L),
                    MapTileMetadata("osm_14_13045_8497", 14, 13045, 8497, "OSM", 23400L),
                    MapTileMetadata("osm_15_26091_16993", 15, 26091, 16993, "OSM", 25100L),
                    MapTileMetadata("osm_15_26092_16993", 15, 26092, 16993, "OSM", 22900L)
                )
                initialTiles.forEach { repository.recordTileMetadata(it) }
            }.onSuccess {
                showToast("Pre-cache selesai! Peta siap offline.")
            }.onFailure { error ->
                showToast("Gagal pre-cache peta: ${error.localizedMessage}")
            }
        }
    }

    fun clearMapCache(ctx: Context) {
        scope.launch {
            runCatching {
                DRGCacheManager.clearCache(ctx)
                repository.clearMapTileCache()
            }.onSuccess {
                showToast("Cache peta lokal berhasil dibersihkan.")
            }.onFailure { error ->
                showToast("Gagal membersihkan cache: ${error.localizedMessage}")
            }
        }
    }
}
