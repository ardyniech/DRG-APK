package com.example.modules.radar.primitives

import androidx.compose.ui.geometry.Offset
import com.example.shared.models.DriverMember
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation
import kotlin.math.sqrt

object MapTapHandler {
    fun findDriverAtTap(
        offset: Offset,
        projectedDrivers: List<Pair<Offset, DriverMember>>
    ): DriverMember? {
        var bestMatch: DriverMember? = null
        var bestDist = Float.MAX_VALUE

        projectedDrivers.filter { it.second.isOnline }.forEach { (pt, driver) ->
            val dx = offset.x - pt.x
            val dy = offset.y - pt.y
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < 48f && dist < bestDist) {
                bestDist = dist
                bestMatch = driver
            }
        }
        return bestMatch
    }

    fun findHazardAtTap(
        offset: Offset,
        projectedHazards: List<Pair<Offset, HazardArea>>
    ): HazardArea? {
        var bestMatch: HazardArea? = null
        var bestDist = Float.MAX_VALUE

        projectedHazards.forEach { (pt, hazard) ->
            val dx = offset.x - pt.x
            val dy = offset.y - pt.y
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < 48f && dist < bestDist) {
                bestDist = dist
                bestMatch = hazard
            }
        }
        return bestMatch
    }

    fun findPoskoAtTap(
        offset: Offset,
        projectedPoskos: List<Pair<Offset, PoskoLocation>>
    ): PoskoLocation? {
        var bestMatch: PoskoLocation? = null
        var bestDist = Float.MAX_VALUE

        projectedPoskos.forEach { (pt, posko) ->
            val dx = offset.x - pt.x
            val dy = offset.y - pt.y
            val dist = sqrt(dx * dx + dy * dy)
            if (dist < 48f && dist < bestDist) {
                bestDist = dist
                bestMatch = posko
            }
        }
        return bestMatch
    }
}

