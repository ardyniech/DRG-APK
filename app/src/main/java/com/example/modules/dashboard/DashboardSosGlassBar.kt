package com.example.modules.dashboard

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val glassShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.65f),
            Color.White.copy(alpha = 0.25f),
            DrgRedPanic.copy(alpha = 0.3f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, glassShape, ambientColor = DrgRedPanic.copy(alpha = 0.3f), spotColor = DrgRedPanic.copy(alpha = 0.25f))
            .clip(glassShape)
            .background(DrgRedPanic.copy(alpha = 0.92f))
            .border(1.2.dp, highlightBrush, glassShape)
            .clickable { onTriggerEmergency() }
            .testTag("btn_sos_floating_glass")
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.22f))
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "SOS",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
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
