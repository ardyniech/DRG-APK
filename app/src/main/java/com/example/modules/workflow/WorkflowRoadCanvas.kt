package com.example.modules.workflow

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun WorkflowRoadCanvas(
    stage: WorkflowStage,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "roadMotion")
    val laneOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (stage.speedKmh > 0) 800 else 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "laneOffset"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        val width = size.width
        val height = size.height
        val horizonY = height * 0.35f

        // 1. Sky & Atmosphere
        drawRect(
            brush = Brush.verticalGradient(
                listOf(DrgTextDark, if (stage.id == 2) DrgSlateDarkBg else DrgSatelliteGrid)
            ),
            size = Size(width, horizonY)
        )

        // 2. Asphalt Road
        val roadPath = Path().apply {
            moveTo(width * 0.35f, horizonY)
            lineTo(width * 0.65f, horizonY)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(
            path = roadPath,
            brush = Brush.verticalGradient(listOf(DrgSlateDarkBg, DrgRoadDark))
        )

        // 3. Moving Lane Dashes
        val dashCount = 5
        repeat(dashCount) { i ->
            val progress = ((laneOffset + i.toFloat() / dashCount) % 1f)
            val dashY = horizonY + (height - horizonY) * progress
            val dashW = 4f + progress * 10f
            val dashH = 8f + progress * 24f
            drawRect(
                color = DrgLaneMarker.copy(alpha = 0.4f + progress * 0.6f),
                topLeft = Offset(width * 0.5f - dashW / 2f, dashY),
                size = Size(dashW, dashH)
            )
        }

        // 4. Motorcycle Headlamp Cone
        if (stage.speedKmh > 0) {
            val lightCone = Path().apply {
                moveTo(width * 0.5f, height * 0.62f)
                lineTo(width * 0.25f, horizonY + 15f)
                lineTo(width * 0.75f, horizonY + 15f)
                close()
            }
            drawPath(
                path = lightCone,
                brush = Brush.verticalGradient(
                    listOf(DrgHeadlightWarm.copy(alpha = 0.2f), Color.Transparent),
                    startY = height * 0.62f,
                    endY = horizonY
                )
            )
        }

        // 5. Motorcycle Indicator Center Point
        val bikeY = height * 0.75f
        drawCircle(color = DrgGreenPrimary, radius = 10f, center = Offset(width * 0.5f, bikeY))
        drawCircle(color = Color.White, radius = 5f, center = Offset(width * 0.5f, bikeY))
        if (stage.id == 3) {
            // SOS Alert Wave Ring
            drawCircle(color = DrgRedDanger.copy(alpha = 0.5f), radius = 22f + laneOffset * 25f, center = Offset(width * 0.5f, bikeY))
        }
    }
}
