package com.example.modules.dashboard.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgTrafficRed
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgRedPanicDark

/**
 * Glossy Squircle SOS Trigger with shockwave pulse halo & haptic spring bounce.
 */
@Composable
fun CockpitHeaderSosButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "sosRing")
    val ringScale by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.24f,
        animationSpec = infiniteRepeatable(tween(1300, easing = FastOutSlowInEasing), RepeatMode.Restart),
        label = "ringScale"
    )
    val ringAlpha by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1300, easing = FastOutSlowInEasing), RepeatMode.Restart),
        label = "ringAlpha"
    )

    val squircle = RoundedCornerShape(18.dp)
    val rubyGradient = Brush.linearGradient(listOf(DrgTrafficRed, DrgRedPanic, DrgRedPanicDark))

    Box(
        modifier = modifier.defaultMinSize(minWidth = 56.dp, minHeight = 52.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = ringScale
                    scaleY = ringScale
                    alpha = ringAlpha
                }
                .clip(squircle)
                .background(DrgRedPanic)
        )
        Box(
            modifier = Modifier
                .shadow(8.dp, squircle, ambientColor = DrgRedPanic, spotColor = DrgRedPanicDark)
                .clip(squircle)
                .background(rubyGradient)
                .border(1.4.dp, Color.White.copy(alpha = 0.85f), squircle)
                .bouncyClick { onClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(Icons.Default.Warning, contentDescription = "SOS Darurat", tint = Color.White, modifier = Modifier.size(18.dp))
                Text(text = "SOS", fontWeight = FontWeight.Black, fontSize = 13.sp, color = Color.White)
            }
        }
    }
}
