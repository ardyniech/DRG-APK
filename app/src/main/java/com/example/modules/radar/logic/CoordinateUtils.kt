package com.example.modules.radar.logic

import com.example.shared.models.DriverMember
import java.util.Locale
import kotlin.math.*

object CoordinateUtils {

    /**
     * Calculates the distance between two coordinates in kilometers using Haversine formula.
     */
    fun calculateDistanceKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    /**
     * Formats latitude and longitude coordinates into human-readable string.
     */
    fun formatCoordinates(lat: Double, lng: Double): String {
        val latDir = if (lat >= 0) "N" else "S"
        val lngDir = if (lng >= 0) "E" else "W"
        return String.format(Locale.US, "%.4f° %s, %.4f° %s", abs(lat), latDir, abs(lng), lngDir)
    }

    /**
     * Filters driver members within specified radius in kilometers from center coordinates.
     */
    fun filterNearbyDrivers(
        members: List<DriverMember>,
        centerLat: Double,
        centerLng: Double,
        maxRadiusKm: Double
    ): List<Pair<DriverMember, Double>> {
        return members
            .filter { it.isOnline && it.isLocationSharingConsent }
            .map { driver ->
                val dist = calculateDistanceKm(centerLat, centerLng, driver.currentLat, driver.currentLng)
                Pair(driver, dist)
            }
            .filter { it.second <= maxRadiusKm }
            .sortedBy { it.second }
    }
}
