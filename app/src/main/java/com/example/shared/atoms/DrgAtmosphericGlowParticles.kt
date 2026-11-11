package com.example.shared.atoms

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.DrgGlassAuraGold
import com.example.ui.theme.DrgGlassAuraGreen
import kotlin.math.PI
import kotlin.math.sin

internal fun DrawScope.drawAtmosphericParticles(w: Float, h: Float, t: Float) {
    for (i in 0..6) {
        val px = w * ((0.14f * i + t * 0.18f * (i + 1)) % 1f)
        val py = h * ((0.18f * (i * 2 + 1) + sin((t + i) * PI).toFloat() * 0.12f) % 1f)
        val pRadius = 2.5f + (i % 3) * 1.5f
        val pAlpha = (0.35f + sin((t * 4 + i) * PI).toFloat() * 0.2f).coerceIn(0.1f, 0.7f)
        drawCircle(
            color = if (i % 2 == 0) DrgGlassAuraGreen.copy(alpha = pAlpha) else DrgGlassAuraGold.copy(alpha = pAlpha),
            radius = pRadius,
            center = Offset(px, py)
        )
    }
}
