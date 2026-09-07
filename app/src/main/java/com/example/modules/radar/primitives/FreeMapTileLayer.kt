package com.example.modules.radar.primitives

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.models.FreeMapMode
import kotlin.math.roundToInt

@Composable
fun FreeMapTileLayer(
    mapMode: FreeMapMode,
    centerLat: Double,
    centerLng: Double,
    zoom: Int,
    screenWidth: Float,
    screenHeight: Float,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val visibleTiles = remember(mapMode, centerLat, centerLng, zoom, screenWidth, screenHeight) {
        if (screenWidth > 0 && screenHeight > 0) {
            MapProjection.getVisibleTiles(centerLat, centerLng, zoom, screenWidth, screenHeight)
        } else {
            emptyList()
        }
    }

    val baseBackgroundColor = if (mapMode == FreeMapMode.SATELLITE) {
        Color(0xFF0D1B15) // Deep satellite earth
    } else {
        Color(0xFFE8ECE9) // Clean open street road tint
    }

    val gridLineColor = if (mapMode == FreeMapMode.SATELLITE) {
        Color(0xFF1E3A2F).copy(alpha = 0.35f)
    } else {
        Color(0xFFCBD5E1).copy(alpha = 0.45f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseBackgroundColor)
    ) {
        // Fallback grid pattern to guarantee zero blank flicker during panning
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 64.dp.toPx()
            val cols = (size.width / step).toInt() + 1
            val rows = (size.height / step).toInt() + 1
            for (c in 0..cols) {
                drawLine(gridLineColor, Offset(c * step, 0f), Offset(c * step, size.height), 1f)
            }
            for (r in 0..rows) {
                drawLine(gridLineColor, Offset(0f, r * step), Offset(size.width, r * step), 1f)
            }
        }

    // Render visible slippy map tiles
    val tileDp = with(density) { MapProjection.TILE_SIZE.toDp() }
    val customImageLoader = remember(context) { com.example.core.cache.DRGCacheManager.getImageLoader(context) }
    val isOfflineOnly = com.example.core.cache.DRGCacheManager.isOfflineModeForced ||
        com.example.core.cache.DRGCacheManager.cachePolicy == com.example.core.cache.MapCachePolicy.OFFLINE_ONLY

    for (tile in visibleTiles) {
        val tileUrl = mapMode.buildTileUrl(tile.z, tile.x, tile.y)
        val request = remember(tileUrl, isOfflineOnly) {
            ImageRequest.Builder(context)
                .data(tileUrl)
                .addHeader("User-Agent", "DRGDriverCommunityApp/1.0 (contact@drgdriver.org)")
                .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                .apply {
                    if (isOfflineOnly) {
                        networkCachePolicy(coil.request.CachePolicy.DISABLED)
                    }
                }
                .crossfade(150)
                .build()
        }

        AsyncImage(
            model = request,
            imageLoader = customImageLoader,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .offset { IntOffset(tile.screenX.roundToInt(), tile.screenY.roundToInt()) }
                .size(tileDp)
        )
    }
    }
}
