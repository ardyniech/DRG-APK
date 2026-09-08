package com.example.modules.emergency

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SosCountdownCircle(secondsLeft: Int, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseScale"
    )

    Surface(
        shape = CircleShape,
        color = DrgRedContainer,
        modifier = modifier.size(90.dp).scale(pulseScale)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "$secondsLeft",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = DrgRedDanger
            )
        }
    }
}
