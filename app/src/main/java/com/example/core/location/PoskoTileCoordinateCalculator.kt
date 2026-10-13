package com.example.core.location

import com.example.modules.radar.logic.MapProjection
import kotlin.math.cos
import kotlin.math.floor

data class TileCoordinate(val zoom: Int, val tileX: Int, val tileY: Int) {
    fun toCacheKey(mapSource: String = "osm"): String =
        "${mapSource.lowercase()}_${zoom}_${tileX}_${tileY}"
}

object PoskoTileCoordinateCalculator {
    private const val METERS_PER_DEGREE_LAT = 111320.0

    fun calculateBoundingTiles(
        lat: Double,
        lng: Double,
        zoom: Int,
        radiusMeters: Double = 1000.0
    ): List<TileCoordinate> {
        if (lat.isNaN() || lng.isNaN() || zoom < 1 || zoom > 20) return emptyList()

        val clampedLat = lat.coerceIn(-85.0, 85.0)
        val clampedLng = lng.coerceIn(-180.0, 180.0)

        val deltaLat = radiusMeters / METERS_PER_DEGREE_LAT
        val cosLat = cos(Math.toRadians(clampedLat)).coerceAtLeast(0.01)
        val deltaLng = radiusMeters / (METERS_PER_DEGREE_LAT * cosLat)

        val minLat = (clampedLat - deltaLat).coerceIn(-85.0, 85.0)
        val maxLat = (clampedLat + deltaLat).coerceIn(-85.0, 85.0)
        val minLng = (clampedLng - deltaLng).coerceIn(-180.0, 180.0)
        val maxLng = (clampedLng + deltaLng).coerceIn(-180.0, 180.0)

        val maxTileIndex = (1 shl zoom) - 1
        val minTileX = floor(MapProjection.lonToTileX(minLng, zoom)).toInt().coerceIn(0, maxTileIndex)
        val maxTileX = floor(MapProjection.lonToTileX(maxLng, zoom)).toInt().coerceIn(0, maxTileIndex)
        val minTileY = floor(MapProjection.latToTileY(maxLat, zoom)).toInt().coerceIn(0, maxTileIndex)
        val maxTileY = floor(MapProjection.latToTileY(minLat, zoom)).toInt().coerceIn(0, maxTileIndex)

        val list = ArrayList<TileCoordinate>((maxTileX - minTileX + 1) * (maxTileY - minTileY + 1))
        for (x in minTileX..maxTileX) {
            for (y in minTileY..maxTileY) {
                list.add(TileCoordinate(zoom, x, y))
            }
        }
        return list
    }
}
