package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_tile_metadata")
data class MapTileMetadata(
    @PrimaryKey val tileKey: String,
    val zoom: Int,
    val tileX: Int,
    val tileY: Int,
    val mapSource: String = "OSM",
    val sizeBytes: Long = 18432L,
    val cachedAt: Long = System.currentTimeMillis(),
    val lastAccessedAt: Long = System.currentTimeMillis(),
    val accessCount: Int = 1,
    val isPersistedOffline: Boolean = true
)
