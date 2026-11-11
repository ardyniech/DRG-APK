package com.example.shared.atoms.cinematic.rider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun RiderDigitalCluster(
    state: RiderSteeringState,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 148.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Digital Display Screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(22.dp))
                .border(2.dp, DrgClusterBezelBorder, RoundedCornerShape(22.dp))
                .clickable { onOpenMenu() }
                .testTag("digital_cluster_screen"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val faceW = size.width * 0.94f
                val faceH = size.height * 0.92f
                val faceTopLeft = Offset((size.width - faceW) / 2f, (size.height - faceH) / 2f)

                // High-tech negative display obsidian background
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        0f to DrgClusterScreenTop,
                        0.5f to DrgClusterScreenMid,
                        1f to DrgClusterScreenBot
                    ),
                    topLeft = faceTopLeft,
                    size = Size(faceW, faceH),
                    cornerRadius = CornerRadius(18f)
                )

                // Tachometer RPM Arc across top
                drawTachometerBar(faceTopLeft, faceW, faceH, state.rpm)

                // Speedometer digits
                val digitW = faceW * 0.145f
                val digitH = faceH * 0.48f
                val thickness = size.minDimension * 0.024f
                val digitsTopLeft = Offset(faceTopLeft.x + faceW * 0.09f, faceTopLeft.y + faceH * 0.22f)

                drawSevenSegmentNumber(
                    value = state.speed,
                    topLeft = digitsTopLeft,
                    digitWidth = digitW,
                    digitHeight = digitH,
                    thickness = thickness,
                    gap = digitW * 0.28f,
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

        Spacer(modifier = Modifier.height(6.dp))

        // Physical-style tactile buttons on lower bezel
        RiderClusterButtons(onOpenMenu = onOpenMenu)
    }
}
