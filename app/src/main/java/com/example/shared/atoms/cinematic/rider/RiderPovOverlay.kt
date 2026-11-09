package com.example.shared.atoms.cinematic.rider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun RiderPovOverlay(
    state: RiderSteeringState,
    onOpenMenu: () -> Unit = {},
    modifier: Modifier = Modifier,
    showHands: Boolean = true
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                translationX = state.vibeX
                translationY = state.vibeY
                rotationZ = state.leanAngle * 0.18f
            }
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        val windscreenTop = screenHeight * 0.41f
        val cowlTop = screenHeight * 0.58f
        val clusterTop = screenHeight * 0.585f
        val clusterHeight = screenHeight * 0.145f
        val indicatorTop = clusterTop + clusterHeight * 0.76f
        val barY = screenHeight * 0.81f

        // 1. Windscreen and fairing cowl
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawWindscreen(windscreenTop.toPx(), cowlTop.toPx() + 20f, state.steer)
            drawFairingCowl(cowlTop.toPx(), size.height * 1.05f)
        }

        // 2. Embedded Digital instrument cluster with interactive buttons
        RiderDigitalCluster(
            state = state,
            onOpenMenu = onOpenMenu,
            modifier = Modifier
                .offset(x = 0.dp, y = clusterTop)
                .height(clusterHeight)
                .padding(horizontal = screenWidth * 0.10f)
        )

        // 3. Status warning indicators
        RiderInstrumentIndicators(
            state = state,
            modifier = Modifier
                .offset(x = 0.dp, y = indicatorTop)
                .height(screenHeight * 0.040f)
                .padding(horizontal = screenWidth * 0.08f)
        )

        // 4. Handlebar with smartphone GPS nav, mirrors, and riding hands
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCockpitShadow(barY.toPx())
            drawHandlebar(state.steer, barY.toPx(), state.speed)
            if (showHands) drawRiderHands(state.steer, barY.toPx())
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCockpitShadow(barY: Float) {
    val top = barY - size.height * 0.05f
    drawRect(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            1f to Color.Black.copy(alpha = 0.88f)
        ),
        topLeft = Offset(0f, top),
        size = Size(size.width, size.height - top)
    )
}