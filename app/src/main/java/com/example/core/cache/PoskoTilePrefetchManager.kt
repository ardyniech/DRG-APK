package com.example.core.cache

import com.example.core.battery.BatteryOptimizationManager
import com.example.core.location.PoskoTileCoordinateCalculator
import com.example.shared.models.MapTileMetadata
import com.example.shared.models.PoskoLocation
import com.example.shared.models.PoskoPrefetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PoskoTilePrefetchManager(
    private val tileCacheEngine: TileMetadataCacheEngine,
    private val batteryManager: BatteryOptimizationManager? = null
) {
    suspend fun prefetchTilesForPosko(
        posko: PoskoLocation,
        radiusMeters: Double = 1000.0
    ): Result<PoskoPrefetchResult> = withContext(Dispatchers.IO) {
        try {
            if (posko.lat.isNaN() || posko.lng.isNaN()) {
                return@withContext Result.failure(IllegalArgumentException("Invalid Posko coordinates"))
            }

            val isPowerSaver = batteryManager?.isPowerSaverMode?.value ?: false
            val zoomLevels = if (isPowerSaver) listOf(14) else listOf(14, 15, 16)
            var count = 0

            zoomLevels.forEach { zoom ->
                val coords = PoskoTileCoordinateCalculator.calculateBoundingTiles(
                    lat = posko.lat,
                    lng = posko.lng,
                    zoom = zoom,
                    radiusMeters = radiusMeters
                )
                coords.forEach { coord ->
                    tileCacheEngine.recordTileCached(
                        zoom = coord.zoom,
                        tileX = coord.tileX,
                        tileY = coord.tileY,
                        mapSource = "OSM",
                        sizeBytes = 18432L,
                        isPersistedOffline = true
                    )
                    count++
                }
            }

            tileCacheEngine.saveStateString("posko_offline_${posko.id}", System.currentTimeMillis().toString())

            Result.success(
                PoskoPrefetchResult(
                    poskoId = posko.id,
                    totalTilesCached = count,
                    zoomLevels = zoomLevels,
                    isThrottledByPowerSaver = isPowerSaver
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isPoskoCachedOffline(poskoId: String): Boolean = withContext(Dispatchers.IO) {
        val state = tileCacheEngine.getStateString("posko_offline_$poskoId")
        !state.isNullOrBlank()
    }
}
