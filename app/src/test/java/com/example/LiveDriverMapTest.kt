package com.example

import com.example.modules.radar.logic.CoordinateUtils
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LiveDriverMapTest {

    @Test
    fun testHaversineDistanceCalculation() {
        // Malang Center (-7.9822, 112.6303) to Klojen (-7.9839, 112.6214)
        val distanceKm = CoordinateUtils.calculateDistanceKm(
            -7.9822, 112.6303,
            -7.9839, 112.6214
        )
        assertTrue(distanceKm > 0.5)
        assertTrue(distanceKm < 2.0)
    }

    @Test
    fun testFormatCoordinates() {
        val formatted = CoordinateUtils.formatCoordinates(-7.9839, 112.6214)
        assertEquals("7.9839° S, 112.6214° E", formatted)
    }

    @Test
    fun testFilterNearbyDriversByRadius() {
        val testMembers = listOf(
            DriverMember(
                id = "DRG-001",
                name = "Ahmad Driver",
                driverId = "DRG001",
                phone = "08123456789",
                role = MemberRole.ANGGOTA,
                motorcyclePlate = "N 1234 AB",
                motorcycleModel = "Honda Vario 125",
                currentLat = -7.9839,
                currentLng = 112.6214,
                isOnline = true,
                isLocationSharingConsent = true
            ),
            DriverMember(
                id = "DRG-002",
                name = "Budi Satgas",
                driverId = "DRG002",
                phone = "08198765432",
                role = MemberRole.SATGAS,
                motorcyclePlate = "N 5678 CD",
                motorcycleModel = "Yamaha NMAX",
                currentLat = -8.5000, // Far away (~50km)
                currentLng = 113.0000,
                isOnline = true,
                isLocationSharingConsent = true
            )
        )

        val centerLat = -7.9822
        val centerLng = 112.6303

        val nearbyWithin5km = CoordinateUtils.filterNearbyDrivers(
            testMembers,
            centerLat,
            centerLng,
            maxRadiusKm = 5.0
        )

        assertEquals(1, nearbyWithin5km.size)
        assertEquals("DRG-001", nearbyWithin5km[0].first.id)
    }
}
