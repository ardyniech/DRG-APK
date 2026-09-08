package com.example.modules.services

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun PttWaveformCanvas(
    isTransmitting: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    val amplitudeMultiplier by animateFloatAsState(
        targetValue = if (isTransmitting) 1.2f else 0.15f,
        animationSpec = tween(300),
        label = "amplitude"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DrgBackground)
            .border(1.dp, DrgGrabGreenContainer.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val path = Path()
            path.moveTo(0f, height / 2f)

            for (x in 0..width.toInt() step 6) {
                val angle = (x.toFloat() / width * 3 * Math.PI) + waveOffset
                val y = height / 2f + Math.sin(angle).toFloat() * 24f * amplitudeMultiplier
                path.lineTo(x.toFloat(), y)
            }

            drawPath(
                path = path,
                color = if (isTransmitting) DrgRedDanger else DrgGrabGreenPrimary,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}
