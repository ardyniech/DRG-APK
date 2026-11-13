package com.example.modules.dashboard.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

/**
 * Bento Grid Tile 1: Frosted Glass Live Animated Radar Sweeper
 */
@Composable
fun BentoRadarCanvasWidget(
    activeMemberCount: Int,
    hazardCount: Int,
    visualTheme: DashboardVisualTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.primaryColor
    val infinite = rememberInfiniteTransition(label = "RadarSweep")
    val angle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing)),
        label = "angle"
    )

    GlassCard(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(accent))
                    Text(text = "RADAR JALUR", fontSize = 10.sp, fontWeight = FontWeight.Black, color = accent)
                }
                Icon(Icons.Default.Radar, contentDescription = "Radar", tint = accent, modifier = Modifier.size(16.dp))
            }

            Box(modifier = Modifier.fillMaxWidth().height(64.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(62.dp)) {
                    val r = size.minDimension / 2
                    drawCircle(color = accent.copy(alpha = 0.10f), radius = r)
                    drawCircle(color = accent.copy(alpha = 0.28f), radius = r, style = Stroke(1.2.dp.toPx()))
                    drawCircle(color = accent.copy(alpha = 0.24f), radius = r * 0.62f, style = Stroke(1.dp.toPx()))
                    drawCircle(color = accent.copy(alpha = 0.35f), radius = r * 0.28f)
                    rotate(degrees = angle, pivot = center) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                0.0f to Color.Transparent,
                                0.72f to Color.Transparent,
                                0.99f to accent.copy(alpha = 0.38f),
                                1.0f to Color.Transparent,
                                center = center
                            ),
                            radius = r
                        )
                        drawLine(color = accent, start = center, end = Offset(center.x + r, center.y), strokeWidth = 2.dp.toPx())
                    }
                    drawCircle(color = accent, radius = 3.5.dp.toPx(), center = Offset(center.x + r * 0.38f, center.y - r * 0.24f))
                    val secondBlip = if (hazardCount > 0) DrgRedPanic else accent
                    drawCircle(color = secondBlip, radius = 3.5.dp.toPx(), center = Offset(center.x - r * 0.42f, center.y + r * 0.28f))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "$activeMemberCount Rekan", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextDark)
                    Text(
                        text = if (hazardCount > 0) "$hazardCount Titik Rawan" else "Jalur Kondusif",
                        fontSize = 10.sp,
                        color = if (hazardCount > 0) DrgRedPanic else DrgTextSecondary
                    )
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(accent.copy(alpha = 0.16f)).padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(text = "LIVE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = accent)
                }
            }
        }
    }
}
