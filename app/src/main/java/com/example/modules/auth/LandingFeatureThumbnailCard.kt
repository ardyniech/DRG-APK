package com.example.modules.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LandingFeatureThumbnailCard(
    feature: AppFeatureThumbnail,
    modifier: Modifier = Modifier
) {
    val hasBg = feature.drawableResId != null
    val textColor = if (hasBg) Color.White else DrgTextDark
    val subTextColor = if (hasBg) Color.White.copy(0.85f) else DrgTextMuted

    val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
    val zoomScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "zoom"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9.2f)
            .border(1.dp, DrgBorderLight, RoundedCornerShape(20.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (hasBg) {
                Image(
                    painter = painterResource(id = feature.drawableResId!!),
                    contentDescription = feature.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = zoomScale
                            scaleY = zoomScale
                        }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(0.12f), Color.Black.copy(0.40f), Color.Black.copy(0.92f))
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(listOf(feature.accentColor.copy(0.12f), feature.accentColor.copy(0.03f))))
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (hasBg) Color.Black.copy(0.50f) else feature.accentColor.copy(0.15f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(text = feature.badgeText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (hasBg) Color.White else feature.accentColor)
                    }

                    Box(
                        modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(feature.accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = feature.icon, contentDescription = feature.title, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = feature.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = feature.description, fontSize = 12.sp, color = subTextColor, maxLines = 2, lineHeight = 16.sp, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}
