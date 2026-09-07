package com.example.modules.radar.logic

import androidx.compose.ui.geometry.Offset
import kotlin.math.*

object MapProjection {
    const val TILE_SIZE = 256f

    fun lonToTileX(lon: Double, zoom: Int): Double {
        val n = (1 shl zoom).toDouble()
        return (lon + 180.0) / 360.0 * n
    }

    fun latToTileY(lat: Double, zoom: Int): Double {
        val n = (1 shl zoom).toDouble()
        val latRad = Math.toRadians(lat.coerceIn(-85.05112878, 85.05112878))
        return (1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / Math.PI) / 2.0 * n
    }

    fun tileXToLon(x: Double, zoom: Int): Double {
        val n = (1 shl zoom).toDouble()
        return x / n * 360.0 - 180.0
    }

    fun tileYToLat(y: Double, zoom: Int): Double {
        val n = (1 shl zoom).toDouble()
        val latRad = atan(sinh(Math.PI * (1.0 - 2.0 * y / n)))
        return Math.toDegrees(latRad)
    }

    fun latLngToScreen(
        lat: Double,
        lng: Double,
        centerLat: Double,
        centerLng: Double,
        zoom: Int,
        screenWidth: Float,
        screenHeight: Float
    ): Offset {
        val cx = lonToTileX(centerLng, zoom)
        val cy = latToTileY(centerLat, zoom)
        val px = lonToTileX(lng, zoom)
        val py = latToTileY(lat, zoom)
        val screenX = screenWidth / 2f + ((px - cx) * TILE_SIZE).toFloat()
        val screenY = screenHeight / 2f + ((py - cy) * TILE_SIZE).toFloat()
        return Offset(screenX, screenY)
    }

    fun screenToLatLng(
        screenX: Float,
        screenY: Float,
        centerLat: Double,
        centerLng: Double,
        zoom: Int,
        screenWidth: Float,
        screenHeight: Float
    ): Pair<Double, Double> {
        val cx = lonToTileX(centerLng, zoom)
        val cy = latToTileY(centerLat, zoom)
        val px = cx + (screenX - screenWidth / 2f) / TILE_SIZE
        val py = cy + (screenY - screenHeight / 2f) / TILE_SIZE
        return Pair(tileYToLat(py, zoom), tileXToLon(px, zoom))
    }

    data class VisibleTile(val x: Int, val y: Int, val z: Int, val screenX: Float, val screenY: Float)

    fun getVisibleTiles(
        centerLat: Double,
        centerLng: Double,
        zoom: Int,
        screenWidth: Float,
        screenHeight: Float
    ): List<VisibleTile> {
        val cx = lonToTileX(centerLng, zoom)
        val cy = latToTileY(centerLat, zoom)
        val halfW = screenWidth / 2f
        val halfH = screenHeight / 2f

        val minX = floor(cx - halfW / TILE_SIZE).toInt()
        val maxX = floor(cx + halfW / TILE_SIZE).toInt()
        val minY = floor(cy - halfH / TILE_SIZE).toInt()
        val maxY = floor(cy + halfH / TILE_SIZE).toInt()

        val maxTileIndex = (1 shl zoom) - 1
        val tiles = ArrayList<VisibleTile>((maxX - minX + 1) * (maxY - minY + 1))
        for (tx in minX..maxX) {
            val wrappedX = ((tx % (maxTileIndex + 1)) + (maxTileIndex + 1)) % (maxTileIndex + 1)
            for (ty in minY..maxY) {
                if (ty in 0..maxTileIndex) {
                    val sx = halfW + ((tx - cx) * TILE_SIZE).toFloat()
                    val sy = halfH + ((ty - cy) * TILE_SIZE).toFloat()
                    tiles.add(VisibleTile(wrappedX, ty, zoom, sx, sy))
                }
            }
        }
        return tiles
    }
}
