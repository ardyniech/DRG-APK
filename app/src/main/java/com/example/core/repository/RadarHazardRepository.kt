package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RadarHazardRepository(private val db: AppDatabase) {
    val allHazards: Flow<List<HazardArea>> = db.hazardDao().getAllHazards()
    val allMapTiles: Flow<List<MapTileMetadata>> = db.mapTileDao().getAllTileMetadata()
    val totalTileCount: Flow<Int> = db.mapTileDao().getTotalTileCount()
    val totalTileSizeBytes: Flow<Long?> = db.mapTileDao().getTotalCacheSizeBytes()
    val allAppSettings: Flow<List<AppStateSetting>> = db.appStateDao().getAllStateSettings()

    suspend fun addHazard(hazard: HazardArea) = withContext(Dispatchers.IO) {
        db.hazardDao().insertHazard(hazard)
        val notif = CommunityNotification(
            id = "NTF-${java.util.UUID.randomUUID().toString().replace("-", "").take(8)}",
            title = "Peringatan Jalur Rawan Baru: ${hazard.hazardType.label}",
            message = "Lokasi: ${hazard.locationName}. Dilaporkan oleh ${hazard.reportedBy}. Harap waspada!",
            severity = NotificationSeverity.WARNING,
            senderName = hazard.reportedBy,
            senderRole = hazard.reporterRole,
            timeAgo = "Baru saja"
        )
        db.notificationDao().insertNotification(notif)
    }

    suspend fun confirmHazard(hazardId: String) = withContext(Dispatchers.IO) {
        val hazard = db.hazardDao().getHazardById(hazardId) ?: return@withContext
        db.hazardDao().updateHazard(hazard.copy(confirmCount = hazard.confirmCount + 1))
    }

    suspend fun recordTileMetadata(tile: MapTileMetadata) = withContext(Dispatchers.IO) {
        db.mapTileDao().insertOrUpdateTile(tile)
    }

    suspend fun clearMapTileCache() = withContext(Dispatchers.IO) {
        db.mapTileDao().clearAllTileMetadata()
    }

    suspend fun saveAppSetting(setting: AppStateSetting) = withContext(Dispatchers.IO) {
        db.appStateDao().saveSetting(setting)
    }
}
