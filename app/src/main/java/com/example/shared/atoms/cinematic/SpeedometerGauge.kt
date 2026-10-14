package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark
import kotlin.math.roundToInt

@Composable
fun SpeedometerGauge(
    modifier: Modifier = Modifier,
    diameter: Dp = 132.dp,
    maxSpeed: Int = 120
) {
    val transition = rememberInfiniteTransition(label = "speedometer")
    val rawSpeed by transition.animateFloat(
        initialValue = 0f,
        targetValue = maxSpeed.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5200
                0f at 0
                0.42f at 900
                0.78f at 1800
                1f at 2900
                0.34f at 4100
                0f at 5200
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "speed"
    )
    val needle by animateFloatAsState(
        targetValue = rawSpeed,
        animationSpec = spring(dampingRatio = 0.58f, stiffness = 190f),
        label = "needle"
    )
    val progress = (needle / maxSpeed).coerceIn(0f, 1f)

    Box(modifier = modifier.size(diameter), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = size.minDimension * 0.085f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(stroke / 2f, stroke / 2f)

            drawArc(
                color = Color.White.copy(alpha = 0.24f),
                startAngle = GaugeStartAngle,
                sweepAngle = GaugeSweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
                startAngle = GaugeStartAngle,
                sweepAngle = GaugeSweepAngle * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawGaugeTicks(stroke)
            drawGaugeNeedle(progress, stroke, DrgGrabGreenPrimary)
        }
        Text(
            text = "${needle.roundToInt()}",
            color = DrgTextDark,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}