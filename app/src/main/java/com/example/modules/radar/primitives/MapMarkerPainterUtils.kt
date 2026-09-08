package com.example.modules.radar.primitives

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.shared.models.DriverMember
import com.example.shared.models.HazardArea
import com.example.shared.models.PoskoLocation
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRadarGlow

object MapMarkerPainterUtils {

    fun DrawScope.drawRadarSweepEffect(maxR: Float, centerOffset: Offset, pulseRadius: Float) {
        drawCircle(color = DrgRadarGlow.copy(alpha = 0.15f), radius = maxR * 0.5f, center = centerOffset, style = Stroke(width = 1f))
        drawCircle(color = DrgRadarGlow.copy(alpha = 0.22f), radius = maxR, center = centerOffset, style = Stroke(width = 1.2f))
        drawCircle(color = DrgRadarGlow.copy(alpha = (1f - pulseRadius) * 0.35f), radius = maxR * pulseRadius, center = centerOffset, style = Stroke(width = 2f))
    }

    fun DrawScope.drawPoskoMarker(pt: Offset) {
        drawCircle(color = Color(0xFF0288D1).copy(alpha = 0.25f), radius = 22f, center = pt)
        drawCircle(color = Color(0xFF0288D1), radius = 8.5f, center = pt)
        drawCircle(color = Color.White, radius = 3.5f, center = pt)
    }

    fun DrawScope.drawHazardMarker(pt: Offset, hazard: HazardArea) {
        val hazardColor = Color(hazard.hazardType.colorHex)
        drawCircle(color = hazardColor.copy(alpha = 0.3f), radius = 24f, center = pt)
        drawCircle(color = hazardColor, radius = 9f, center = pt)
        drawCircle(color = Color.White, radius = 3.5f, center = pt)
    }

    fun DrawScope.drawDriverMarker(pt: Offset, driver: DriverMember, isTargeted: Boolean, myPt: Offset, pulseRadius: Float) {
        val pinColor = when {
            driver.role.name == "SATGAS" -> Color(0xFFE53935)
            driver.currentStatus == "Sedang Narik" -> Color(0xFFFF8F00)
            else -> DrgGreenPrimary
        }

        if (isTargeted) {
            drawCircle(
                color = pinColor.copy(alpha = 0.4f),
                radius = 24f + (pulseRadius * 10f),
                center = pt,
                style = Stroke(width = 2f)
            )
            drawLine(
                color = pinColor.copy(alpha = 0.7f),
                start = myPt,
                end = pt,
                strokeWidth = 2.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            )
        }

        drawCircle(color = pinColor.copy(alpha = 0.28f), radius = 16f, center = pt)
        drawCircle(color = pinColor, radius = 8.5f, center = pt)
        drawCircle(color = Color.White, radius = 3.5f, center = pt)
    }

    fun DrawScope.drawMyGpsPosition(myPt: Offset, isConsentGranted: Boolean, pulseRadius: Float) {
        if (isConsentGranted) {
            drawCircle(
                color = DrgGreenPrimary.copy(alpha = 0.25f),
                radius = 26f * (0.6f + (pulseRadius * 0.4f)),
                center = myPt,
                style = Stroke(width = 2f)
            )
            drawCircle(color = Color.White, radius = 9.5f, center = myPt)
            drawCircle(color = DrgGreenPrimary, radius = 6f, center = myPt)
        } else {
            drawCircle(color = Color.Gray, radius = 7f, center = myPt)
        }
    }
}
