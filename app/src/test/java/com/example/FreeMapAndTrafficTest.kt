package com.example

import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.logic.TrafficDataRepository
import com.example.modules.radar.models.FreeMapMode
import com.example.modules.radar.models.TrafficStatus
import org.junit.Assert.*
import org.junit.Test

class FreeMapAndTrafficTest {

    @Test
    fun testFreeMapModesUrlBuilding() {
        val roadUrl = FreeMapMode.ROAD.buildTileUrl(14, 13318, 8560)
        assertEquals("https://tile.openstreetmap.org/14/13318/8560.png", roadUrl)

        val satUrl = FreeMapMode.SATELLITE.buildTileUrl(14, 13318, 8560)
        assertEquals("https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/14/8560/13318", satUrl)
    }

    @Test
    fun testMapProjectionCalculations() {
        val lat = -7.9822 // Malang
        val lng = 112.6303
        val zoom = 14

        val tileX = MapProjection.lonToTileX(lng, zoom)
        val tileY = MapProjection.latToTileY(lat, zoom)

        assertTrue(tileX > 0)
        assertTrue(tileY > 0)

        val tiles = MapProjection.getVisibleTiles(lat, lng, zoom, 1080f, 1920f)
        assertTrue("Visible tiles should not be empty", tiles.isNotEmpty())
    }

    @Test
    fun testTrafficDataSegments() {
        val segments = TrafficDataRepository.getRoadSegments()
        assertTrue("Segments should exist", segments.isNotEmpty())

        val smoothRoad = segments.find { it.status == TrafficStatus.SMOOTH }
        assertNotNull(smoothRoad)
        assertTrue(smoothRoad!!.avgSpeedKmh > 35)

        val jammedRoad = segments.find { it.status == TrafficStatus.JAMMED }
        assertNotNull(jammedRoad)
        assertTrue(jammedRoad!!.avgSpeedKmh < 15)
    }
}
