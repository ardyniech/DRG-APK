package com.example.core.location

import com.example.shared.models.PoskoLocation
import kotlin.math.*

data class PoskoProximityResult(
    val posko: PoskoLocation,
    val distanceMeters: Double,
    val isInsideCheckInRadius: Boolean
)

object PoskoProximityDetector {
    const val CHECK_IN_RADIUS_METERS = 100.0

    fun calculateDistanceMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val r = 6371000.0 // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun findNearestPosko(
        currentLat: Double,
        currentLng: Double,
        poskoList: List<PoskoLocation>
    ): PoskoProximityResult? {
        if (poskoList.isEmpty()) return null

        var nearest: PoskoLocation? = null
        var minDistance = Double.MAX_VALUE

        poskoList.forEach { posko ->
            val dist = calculateDistanceMeters(currentLat, currentLng, posko.lat, posko.lng)
            if (dist < minDistance) {
                minDistance = dist
                nearest = posko
            }
        }

        return nearest?.let {
            PoskoProximityResult(
                posko = it,
                distanceMeters = minDistance,
                isInsideCheckInRadius = minDistance <= CHECK_IN_RADIUS_METERS
            )
        }
    }
}
