package com.example.shared.models

data class PoskoPrefetchResult(
    val poskoId: String,
    val totalTilesCached: Int,
    val zoomLevels: List<Int>,
    val isThrottledByPowerSaver: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class PoskoOfflineStatus(
    val poskoId: String,
    val isCachedOffline: Boolean,
    val tileCount: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
