package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun QuickTile(
    title: String,
    desc: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val tileShape = RoundedCornerShape(20.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White,
            Color.White.copy(alpha = 0.5f),
            color.copy(alpha = 0.15f)
        )
    )

    Box(
        modifier = modifier
            .shadow(4.dp, tileShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .clip(tileShape)
            .background(Color.White.copy(alpha = 0.82f))
            .border(1.2.dp, highlightBrush, tileShape)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.14f))
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
                Text(
                    text = desc,
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )
            }
        }
    }
}
