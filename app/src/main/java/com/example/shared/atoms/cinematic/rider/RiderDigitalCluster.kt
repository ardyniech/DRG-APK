package com.example.shared.atoms.cinematic.rider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgClusterBezel
import com.example.ui.theme.DrgClusterFace
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSegmentOff
import com.example.ui.theme.DrgSegmentOn

@Composable
fun RiderDigitalCluster(
    state: RiderSteeringState,
    modifier: Modifier = Modifier,
    height: Dp = 148.dp
) {
    Box(
        modifier = modifier.fillMaxWidth().height(height),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(height)) {
            val faceW = size.width * 0.92f
            val faceH = size.height * 0.92f
            val faceTopLeft = Offset((size.width - faceW) / 2f, (size.height - faceH) / 2f)

            drawRoundRect(
                color = DrgClusterBezel,
                topLeft = Offset(faceTopLeft.x - 5f, faceTopLeft.y - 5f),
                size = Size(faceW + 10f, faceH + 10f),
                cornerRadius = CornerRadius(size.height * 0.20f)
            )
            drawRoundRect(
                brush = Brush.verticalGradient(
                    0f to DrgClusterFace.copy(alpha = 0.98f),
                    1f to Color(0xFF04070A)
                ),
                topLeft = faceTopLeft,
                size = Size(faceW, faceH),
                cornerRadius = CornerRadius(size.height * 0.17f)
            )

            val digitW = faceW * 0.148f
            val digitH = faceH * 0.52f
            val thickness = size.minDimension * 0.026f
            val digitsTopLeft = Offset(faceTopLeft.x + faceW * 0.085f, faceTopLeft.y + faceH * 0.17f)

            drawSevenSegmentNumber(
                value = state.speed,
                topLeft = digitsTopLeft,
                digitWidth = digitW,
                digitHeight = digitH,
                thickness = thickness,
                gap = digitW * 0.30f,
                onColor = DrgSegmentOn,
                offColor = DrgSegmentOff,
                glowColor = DrgSegmentGlow
            )

            drawUnitLabel(faceTopLeft, faceW, faceH, thickness)
            drawGearBadge(faceTopLeft, faceW, faceH, state)
            drawEcoBar(faceTopLeft, faceW, faceH, state)
            drawClusterReflection(faceTopLeft, faceW, faceH)
        }
    }
}
