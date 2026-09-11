package com.example

import com.example.core.location.PoskoProximityDetector
import com.example.shared.models.PoskoLocation
import org.junit.Assert.*
import org.junit.Test

class PoskoProximityAndWatermarkTest {

    private val samplePoskos = listOf(
        PoskoLocation(
            id = "POSKO-1",
            name = "Posko Singosari TRC",
            address = "Jl. Raya Singosari No. 12",
            area = "Malang Utara",
            lat = -7.8921,
            lng = 112.6644,
            phone = "081234567891",
            facilities = "Kopi, Charger, Ban Bocor",
            coordinatorName = "Pak Slamet"
        ),
        PoskoLocation(
            id = "POSKO-2",
            name = "Posko Alun-Alun Kota",
            address = "Jl. Merdeka Barat No. 5",
            area = "Malang Tengah",
            lat = -7.9822,
            lng = 112.6303,
            phone = "081234567892",
            facilities = "Wi-Fi, Shelter Hujan",
            coordinatorName = "Mas Agus"
        )
    )

    @Test
    fun testDetectsDriverInsideCheckInRadius() {
        // Exact location of Posko Alun-Alun
        val result = PoskoProximityDetector.findNearestPosko(-7.9822, 112.6303, samplePoskos)
        assertNotNull(result)
        assertEquals("POSKO-2", result?.posko?.id)
        assertTrue(result?.distanceMeters ?: 999.0 < 5.0)
        assertTrue(result?.isInsideCheckInRadius == true)
    }

    @Test
    fun testDetectsDriverOutsideCheckInRadius() {
        // Location 5 km away
        val result = PoskoProximityDetector.findNearestPosko(-7.9500, 112.6000, samplePoskos)
        assertNotNull(result)
        assertTrue((result?.distanceMeters ?: 0.0) > 1000.0)
        assertFalse(result?.isInsideCheckInRadius == true)
    }

    @Test
    fun testEmptyPoskoListReturnsNull() {
        val result = PoskoProximityDetector.findNearestPosko(-7.9822, 112.6303, emptyList())
        assertNull(result)
    }
}
