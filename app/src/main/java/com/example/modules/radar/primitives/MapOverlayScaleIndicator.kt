package com.example.modules.radar.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MapOverlayScaleIndicator(
    zoom: Int,
    activeCount: Int,
    modifier: Modifier = Modifier
) {
    val scaleText = when (zoom) {
        in 17..18 -> "100 m"
        16 -> "200 m"
        15 -> "500 m"
        14 -> "1 km"
        13 -> "2 km"
        12 -> "5 km"
        else -> "10 km"
    }

    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = DrgSurface.copy(alpha = 0.92f),
            modifier = Modifier.border(0.5.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(DrgGreenPrimary, RoundedCornerShape(3.dp))
                )
                Text(
                    text = "$activeCount Driver Siaga",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextDark
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = DrgSurface.copy(alpha = 0.92f),
            modifier = Modifier.border(0.5.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "📏 $scaleText",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = DrgTextMuted,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}
