package com.example

import com.example.modules.radar.logic.MapProjection
import com.example.modules.radar.logic.RealTrafficManager
import com.example.modules.radar.models.FreeMapMode
import com.example.shared.models.HazardArea
import com.example.shared.models.HazardType
import com.example.shared.models.MemberRole
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
    fun testRealTrafficManagerGoogleMapsUrlAndSummary() {
        val trafficUrl = RealTrafficManager.getGoogleMapsTrafficUrl(-7.9822, 112.6303, 14)
        assertTrue(trafficUrl.contains("data=!5m1!1e1"))
        assertTrue(trafficUrl.contains("-7.9822"))
        assertTrue(trafficUrl.contains("112.6303"))

        val mockHazards = listOf(
            HazardArea(id = "H1", title = "Macet", hazardType = HazardType.TRAFFIC_JAM, locationName = "Jl. Basuki Rahmat", description = "Macet Jl. Basuki Rahmat", lat = -7.97, lng = 112.62, reportedBy = "Driver 1", reporterRole = MemberRole.ANGGOTA),
            HazardArea(id = "H2", title = "Lubang", hazardType = HazardType.HEAVY_HOLE, locationName = "Jl. Soehat", description = "Lubang Besar", lat = -7.98, lng = 112.63, reportedBy = "Driver 2", reporterRole = MemberRole.ANGGOTA)
        )

        val activeJams = RealTrafficManager.getActiveTrafficJams(mockHazards)
        assertEquals(1, activeJams.size)
        assertEquals("Macet Jl. Basuki Rahmat", activeJams.first().description)

        val summary = RealTrafficManager.generateTrafficSummary(mockHazards)
        assertEquals(1, summary.totalJamReports)
        assertEquals(1, summary.activeAreas.size)
    }
}
