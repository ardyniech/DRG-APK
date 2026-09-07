package com.example.core.cache

import com.example.core.database.dao.AppStateDao
import com.example.core.database.dao.MapTileDao
import com.example.shared.models.AppStateSetting
import com.example.shared.models.MapTileMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TileMetadataCacheEngine(
    private val mapTileDao: MapTileDao,
    private val appStateDao: AppStateDao
) {
    suspend fun recordTileCached(
        zoom: Int,
        tileX: Int,
        tileY: Int,
        mapSource: String = "OSM",
        sizeBytes: Long = 18432L,
        isPersistedOffline: Boolean = true
    ) = withContext(Dispatchers.IO) {
        val key = "${mapSource.lowercase()}_${zoom}_${tileX}_${tileY}"
        val existing = mapTileDao.getTileMetadata(key)
        val now = System.currentTimeMillis()
        if (existing != null) {
            mapTileDao.recordTileAccess(key, now)
        } else {
            val metadata = MapTileMetadata(
                tileKey = key,
                zoom = zoom,
                tileX = tileX,
                tileY = tileY,
                mapSource = mapSource,
                sizeBytes = sizeBytes,
                cachedAt = now,
                lastAccessedAt = now,
                accessCount = 1,
                isPersistedOffline = isPersistedOffline
            )
            mapTileDao.insertOrUpdateTile(metadata)
        }
    }

    suspend fun recordTileHit(key: String, sizeBytes: Long = 18432L): Long = withContext(Dispatchers.IO) {
        mapTileDao.recordTileAccess(key)
        sizeBytes
    }

    suspend fun evictOldestTilesIfNeeded(maxTileCount: Int = 1000) = withContext(Dispatchers.IO) {
        mapTileDao.evictOldestTiles(100)
    }

    suspend fun saveStateString(key: String, value: String) = withContext(Dispatchers.IO) {
        appStateDao.saveSetting(AppStateSetting(key = key, stringValue = value))
    }

    suspend fun getStateString(key: String): String? = withContext(Dispatchers.IO) {
        appStateDao.getSetting(key)?.stringValue
    }

    suspend fun saveStateBoolean(key: String, value: Boolean) = withContext(Dispatchers.IO) {
        appStateDao.saveSetting(AppStateSetting(key = key, boolValue = value))
    }

    suspend fun getStateBoolean(key: String, default: Boolean = false): Boolean = withContext(Dispatchers.IO) {
        appStateDao.getSetting(key)?.boolValue ?: default
    }
}
