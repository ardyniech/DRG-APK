package com.example.modules.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgRedPanic

@Composable
fun DashboardSosGlassBar(
    onTriggerEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pulseAnim = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            pulseAnim.animateTo(1.05f, animationSpec = tween(750, easing = FastOutSlowInEasing))
            pulseAnim.animateTo(0.96f, animationSpec = tween(750, easing = FastOutSlowInEasing))
        }
    }

    val glassShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.75f),
            Color.White.copy(alpha = 0.35f),
            DrgRedPanic.copy(alpha = 0.4f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, glassShape, ambientColor = DrgRedPanic.copy(alpha = 0.35f), spotColor = DrgRedPanic.copy(alpha = 0.3f))
            .clip(glassShape)
            .background(DrgRedPanic.copy(alpha = 0.94f))
            .border(1.2.dp, highlightBrush, glassShape)
            .clickable { onTriggerEmergency() }
            .testTag("btn_sos_floating_glass")
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .graphicsLayer {
                            scaleX = pulseAnim.value
                            scaleY = pulseAnim.value
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .border(1.2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "SOS",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "TOMBOL DARURAT & PANTAUAN TRC",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Mogok, Kecelakaan, Jalur Rawan • Siaga 24 Jam",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Buka SOS",
                tint = Color.White
            )
        }
    }
}
