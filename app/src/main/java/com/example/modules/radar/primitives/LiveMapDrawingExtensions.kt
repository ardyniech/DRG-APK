package com.example.modules.radar.primitives

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

fun DrawScope.drawLiveMapGrid(gridStep: Float = 80f) {
    var x = 0f
    while (x < size.width) {
        drawLine(color = DrgOutline.copy(alpha = 0.2f), start = Offset(x, 0f), end = Offset(x, size.height), strokeWidth = 1f)
        x += gridStep
    }
    var y = 0f
    while (y < size.height) {
        drawLine(color = DrgOutline.copy(alpha = 0.2f), start = Offset(0f, y), end = Offset(size.width, y), strokeWidth = 1f)
        y += gridStep
    }
}

fun DrawScope.drawDriverMarker(pt: Offset, driver: DriverMember, isFocused: Boolean, pulseScale: Float) {
    val markerColor = when {
        driver.role.name == "SATGAS" -> DrgRedDanger
        driver.currentStatus == "Sedang Narik" -> Color(0xFFFF8F00)
        else -> DrgGreenPrimary
    }
    if (isFocused) {
        drawCircle(color = markerColor.copy(alpha = 0.35f), radius = 28f * pulseScale, center = pt, style = Stroke(width = 3f))
    }
    drawCircle(color = markerColor.copy(alpha = 0.3f), radius = 18f, center = pt)
    drawCircle(color = markerColor, radius = 10f, center = pt)
    drawCircle(color = Color.White, radius = 4f, center = pt)
}
