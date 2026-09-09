package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgGreenContainer
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgTextPrimary

@Composable
fun SharingDriverItem(
    driver: DriverMember,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseRing")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val itemBorderColor = if (isSelected) DrgGreenPrimary else DrgOutline.copy(alpha = 0.4f)
    val itemBgColor = if (isSelected) DrgGreenContainer.copy(alpha = 0.4f) else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(itemBgColor)
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .scale(pulseScale)
                    .border(1.5.dp, DrgGreenPrimary.copy(alpha = 0.45f), CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(DrgGreenContainer)
                    .border(2.dp, itemBorderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = driver.name.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DrgGreenPrimary
                )
            }
        }

        Text(
            text = driver.name.split(" ").firstOrNull() ?: driver.name,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 10.sp,
            color = DrgTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
