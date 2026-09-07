package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.MapTileMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface MapTileDao {
    @Query("SELECT * FROM map_tile_metadata ORDER BY lastAccessedAt DESC")
    fun getAllTileMetadata(): Flow<List<MapTileMetadata>>

    @Query("SELECT * FROM map_tile_metadata WHERE tileKey = :key LIMIT 1")
    suspend fun getTileMetadata(key: String): MapTileMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTile(tile: MapTileMetadata)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiles(tiles: List<MapTileMetadata>)

    @Query("UPDATE map_tile_metadata SET lastAccessedAt = :time, accessCount = accessCount + 1 WHERE tileKey = :key")
    suspend fun recordTileAccess(key: String, time: Long = System.currentTimeMillis())

    @Query("SELECT SUM(sizeBytes) FROM map_tile_metadata")
    fun getTotalCacheSizeBytes(): Flow<Long?>

    @Query("SELECT COUNT(*) FROM map_tile_metadata")
    fun getTotalTileCount(): Flow<Int>

    @Query("DELETE FROM map_tile_metadata WHERE tileKey IN (SELECT tileKey FROM map_tile_metadata ORDER BY lastAccessedAt ASC LIMIT :count)")
    suspend fun evictOldestTiles(count: Int)

    @Query("DELETE FROM map_tile_metadata WHERE cachedAt < :expiryThreshold AND isPersistedOffline = 0")
    suspend fun deleteExpiredTiles(expiryThreshold: Long)

    @Query("DELETE FROM map_tile_metadata")
    suspend fun clearAllTileMetadata()
}
