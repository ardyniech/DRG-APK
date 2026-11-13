package com.example.modules.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.SpaceGroteskFontFamily

/**
 * Detached Floating Quick-SOS / TRC Satellite Orb paired beside the liquid dock capsule.
 */
@Composable
fun DrgBottomNavSatellite(
    activeEmergencyCount: Int,
    isEmergencyActiveTab: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val hasAlert = activeEmergencyCount > 0
    val pulse by rememberInfiniteTransition(label = "navSosOrbPulse").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing)),
        label = "navSosOrbRing"
    )
    val baseFill by animateColorAsState(
        targetValue = if (hasAlert) DrgRedPanic else if (isEmergencyActiveTab) Color(0xFFB91C1C) else Color.White.copy(alpha = 0.88f),
        animationSpec = tween(280),
        label = "navSosFill"
    )
    val iconTint by animateColorAsState(
        targetValue = if (hasAlert || isEmergencyActiveTab) Color.White else DrgRedPanic,
        animationSpec = tween(250),
        label = "navSosTint"
    )
    val rimColor = if (hasAlert || isEmergencyActiveTab) Color.White.copy(alpha = 0.9f) else DrgRedPanic.copy(alpha = 0.35f)
    val glowColor = if (hasAlert) DrgRedPanic else accentColor
    val bgBrush = Brush.verticalGradient(listOf(baseFill, if (hasAlert || isEmergencyActiveTab) Color(0xFF991B1B) else Color(0xFFFFF1F2)))

    Box(modifier = modifier.size(60.dp), contentAlignment = Alignment.Center) {
        if (hasAlert) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val r = (size.minDimension / 2f) * (0.78f + pulse * 0.22f)
                drawCircle(color = DrgRedPanic.copy(alpha = (1f - pulse) * 0.55f), radius = r, style = Stroke(3.dp.toPx()))
            }
        }
        Box(
            modifier = Modifier
                .size(54.dp)
                .shadow(if (hasAlert || isEmergencyActiveTab) 14.dp else 10.dp, CircleShape, ambientColor = glowColor, spotColor = glowColor)
                .clip(CircleShape)
                .background(bgBrush)
                .border(1.4.dp, rimColor, CircleShape)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
                .testTag("bottom_nav_sos_orb"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Shield, contentDescription = "Pusat Darurat SOS", tint = iconTint, modifier = Modifier.size(20.dp))
                Text(
                    text = if (hasAlert) activeEmergencyCount.coerceAtMost(99).toString() else "SOS",
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = iconTint,
                    lineHeight = 10.sp
                )
            }
        }
    }
}
