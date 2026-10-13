package com.example.shared.atoms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgGlassAuraGold
import com.example.ui.theme.DrgGlassAuraGreen
import com.example.ui.theme.DrgGlassAuraTeal

@Composable
fun DrgAtmosphericGlow(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Top-left luminous emerald/teal aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraGreen.copy(alpha = 0.28f),
                    DrgGlassAuraTeal.copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(w * 0.15f, h * 0.10f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.15f, h * 0.10f)
        )

        // Top-right warm sunrise aura (amber flare for driver spirit)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraGold.copy(alpha = 0.22f),
                    Color(0xFFFEF08A).copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(w * 0.88f, h * 0.25f),
                radius = w * 0.55f
            ),
            radius = w * 0.55f,
            center = Offset(w * 0.88f, h * 0.25f)
        )

        // Mid-screen fresh emerald pulse
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DrgGlassAuraGreen.copy(alpha = 0.16f),
                    Color.Transparent
                ),
                center = Offset(w * 0.35f, h * 0.60f),
                radius = w * 0.60f
            ),
            radius = w * 0.60f,
            center = Offset(w * 0.35f, h * 0.60f)
        )
    }
}
