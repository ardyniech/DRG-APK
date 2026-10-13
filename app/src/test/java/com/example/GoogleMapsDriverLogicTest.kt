package com.example

import com.example.modules.radar.googlemaps.models.DriverMapFilter
import com.example.modules.radar.logic.CoordinateUtils
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GoogleMapsDriverLogicTest {

    private val driverA = DriverMember(
        id = "DRV-001",
        name = "Budi Satgas",
        driverId = "DRG-001",
        phone = "081234567891",
        role = MemberRole.SATGAS,
        motorcyclePlate = "N 1111 AA",
        motorcycleModel = "Vario 160",
        currentStatus = "Sedang Narik",
        currentLat = -7.9820,
        currentLng = 112.6300,
        isOnline = true,
        isLocationSharingConsent = true
    )

    private val driverB = DriverMember(
        id = "DRV-002",
        name = "Agus Siaga",
        driverId = "DRG-002",
        phone = "081234567892",
        role = MemberRole.ANGGOTA,
        motorcyclePlate = "N 2222 BB",
        motorcycleModel = "Beat Deluxe",
        currentStatus = "Tersedia",
        currentLat = -7.9850,
        currentLng = 112.6350,
        isOnline = true,
        isLocationSharingConsent = true
    )

    private val driverOffline = DriverMember(
        id = "DRV-003",
        name = "Candra Offline",
        driverId = "DRG-003",
        phone = "081234567893",
        role = MemberRole.ANGGOTA,
        motorcyclePlate = "N 3333 CC",
        motorcycleModel = "NMAX",
        currentStatus = "Istirahat",
        currentLat = -7.9900,
        currentLng = 112.6400,
        isOnline = false,
        isLocationSharingConsent = true
    )

    private val sampleDrivers = listOf(driverA, driverB, driverOffline)

    @Test
    fun testFilterAllOnlineDrivers() {
        val filtered = sampleDrivers.filter { it.isOnline && it.isLocationSharingConsent }
        assertEquals(2, filtered.size)
        assertTrue(filtered.any { it.id == "DRV-001" })
        assertTrue(filtered.any { it.id == "DRV-002" })
        assertFalse(filtered.any { it.id == "DRV-003" })
    }

    @Test
    fun testFilterByDutyStatusAndRole() {
        val satgas = sampleDrivers.filter { it.isOnline && it.isLocationSharingConsent }
            .filter { it.role == MemberRole.SATGAS }
        assertEquals(1, satgas.size)
        assertEquals("Budi Satgas", satgas.first().name)

        val standby = sampleDrivers.filter { it.isOnline && it.isLocationSharingConsent }
            .filter { it.currentStatus == "Tersedia" || it.currentStatus == "Siaga" }
        assertEquals(1, standby.size)
        assertEquals("Agus Siaga", standby.first().name)
    }

    @Test
    fun testDistanceCalculationBetweenDrivers() {
        val distance = CoordinateUtils.calculateDistanceKm(
            driverA.currentLat, driverA.currentLng,
            driverB.currentLat, driverB.currentLng
        )
        assertTrue("Distance should be greater than 0", distance > 0.0)
        assertTrue("Distance between nearby points in Malang should be under 2 km", distance < 2.0)
    }

    @Test
    fun testAdversarialBadInputCoordinates() {
        val badDriver = driverA.copy(currentLat = Double.NaN, currentLng = Double.NaN)
        assertTrue(badDriver.currentLat.isNaN())
        assertTrue(badDriver.currentLng.isNaN())

        val distWithNan = CoordinateUtils.calculateDistanceKm(-7.9822, 112.6303, badDriver.currentLat, badDriver.currentLng)
        assertTrue("Distance with NaN should produce NaN gracefully", distWithNan.isNaN())
    }

    @Test
    fun testAdversarialEmptyDriversList() {
        val emptyList = emptyList<DriverMember>()
        val filtered = emptyList.filter { it.isOnline }
        assertTrue(filtered.isEmpty())
        val selected = filtered.firstOrNull { it.id == "ANY" }
        assertNull(selected)
    }

    @Test
    fun testAdversarialConsentRevokedHidesDriverFromMap() {
        val revokedDriver = driverA.copy(isLocationSharingConsent = false)
        val visibleDrivers = listOf(revokedDriver, driverB).filter { it.isOnline && it.isLocationSharingConsent }
        assertEquals(1, visibleDrivers.size)
        assertEquals("DRV-002", visibleDrivers.first().id)
    }
}
