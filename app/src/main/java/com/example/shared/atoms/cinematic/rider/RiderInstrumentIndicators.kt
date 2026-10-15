package com.example.shared.atoms.cinematic.rider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgIndicatorDim
import com.example.ui.theme.DrgIndicatorWarn
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSegmentOn

@Composable
fun RiderInstrumentIndicators(
    state: RiderSteeringState,
    modifier: Modifier = Modifier,
    height: Dp = 62.dp
) {
    Box(modifier = modifier.fillMaxWidth().height(height)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(height)) {
            val unit = size.minDimension * 0.30f
            val leftX = size.width * 0.09f
            val rightX = size.width * 0.91f
            val centerY = size.height * 0.42f

            val leftOn = state.turnSide == -1 && state.signalOn
            val rightOn = state.turnSide == 1 && state.signalOn

            drawArrow(Offset(leftX, centerY), unit, leftOn, mirrored = false)
            drawArrow(Offset(rightX, centerY), unit, rightOn, mirrored = true)
            drawBeamIndicator(Offset(size.width * 0.245f, centerY), unit, state.highBeam)
            drawLowBeamIndicator(Offset(size.width * 0.315f, centerY), unit)
            drawBatteryIndicator(Offset(size.width * 0.685f, centerY), unit, 0.72f)
            drawFuelIndicator(Offset(size.width * 0.755f, centerY), unit, 0.45f)
            drawAbsBadge(Offset(size.width * 0.43f, centerY), unit)
            drawP2pBadge(Offset(size.width * 0.57f, centerY), unit)
        }
    }
}
