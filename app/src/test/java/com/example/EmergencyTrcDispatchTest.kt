package com.example

import com.example.shared.models.EmergencyAlert
import com.example.shared.models.EmergencyType
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.*

class EmergencyTrcDispatchTest {

    @Test
    fun testGpsCoordinateBoundsValidation() {
        val validLat = -6.2088
        val validLng = 106.8456
        val invalidLat = 105.0
        val invalidLng = -200.0

        val isValidCoord: (Double, Double) -> Boolean = { lat, lng ->
            lat in -90.0..90.0 && lng in -180.0..180.0
        }

        assertTrue(isValidCoord(validLat, validLng))
        assertFalse(isValidCoord(invalidLat, validLng))
        assertFalse(isValidCoord(validLat, invalidLng))
    }

    @Test
    fun testHaversineDistanceCalculationAccuracy() {
        // Monas to Bundaran HI (roughly 2.2 km)
        val lat1 = -6.1754
        val lon1 = 106.8272
        val lat2 = -6.1950
        val lon2 = 106.8231

        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceKm = earthRadiusKm * c

        assertTrue("Distance should be around 2.2km", distanceKm in 2.0..2.5)
    }

    @Test
    fun testEmergencyLifecycleStateTransitions() {
        val alert = EmergencyAlert(
            id = "EMG-001",
            driverId = "DRG-JKT-01",
            driverName = "Suryo",
            driverPhone = "08123456789",
            plateNumber = "B 1234 DRG",
            type = EmergencyType.MOGOK,
            message = "Rantai putus di Flyover",
            lat = -6.200,
            lng = 106.816,
            locationName = "Flyover Kuningan",
            isActive = true,
            responderCount = 0
        )

        assertTrue(alert.isActive)
        assertEquals(0, alert.responderCount)

        // TRC responders join
        val updatedAlert = alert.copy(responderCount = alert.responderCount + 2)
        assertEquals(2, updatedAlert.responderCount)

        // Emergency resolved
        val resolvedAlert = updatedAlert.copy(isActive = false, resolvedBy = "Satgas TRC Timur")
        assertFalse(resolvedAlert.isActive)
        assertEquals("Satgas TRC Timur", resolvedAlert.resolvedBy)
    }
}
