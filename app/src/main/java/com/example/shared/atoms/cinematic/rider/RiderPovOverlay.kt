package com.example.shared.atoms.cinematic.rider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgWindscreenTint

@Composable
fun RiderPovOverlay(
    state: RiderSteeringState,
    modifier: Modifier = Modifier,
    showHands: Boolean = true
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth
        val barY = screenHeight * 0.815f
        val clusterHeight = screenHeight * 0.145f
        val clusterTop = screenHeight * 0.495f
        val indicatorTop = clusterTop + clusterHeight * 1.06f

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = DrgWindscreenTint)
        }

        RiderDigitalCluster(
            state = state,
            modifier = Modifier
                .offset(x = 0.dp, y = clusterTop)
                .height(clusterHeight)
                .padding(horizontal = screenWidth * 0.10f)
        )

        RiderInstrumentIndicators(
            state = state,
            modifier = Modifier
                .offset(x = 0.dp, y = indicatorTop)
                .height(screenHeight * 0.055f)
                .padding(horizontal = screenWidth * 0.04f)
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCockpitShadow(barY)
            drawHandlebar(state.steer, barY.toPx())
            if (showHands) drawRiderHands(state.steer, barY.toPx())
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCockpitShadow(barY: androidx.compose.ui.unit.Dp) {
    val top = barY.toPx() - size.height * 0.06f
    drawRect(
        brush = Brush.verticalGradient(
            0f to Color.Transparent,
            1f to Color.Black.copy(alpha = 0.78f)
        ),
        topLeft = Offset(0f, top),
        size = Size(size.width, size.height - top)
    )
}