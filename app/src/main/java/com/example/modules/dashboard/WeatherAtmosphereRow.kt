package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun WeatherAtmosphereRow(
    baseArea: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(DrgGreenContainer.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Cuaca",
                tint = DrgGoldReward,
                modifier = Modifier.size(18.dp)
            )
            Column {
                Text(
                    text = "28°C Cerah Berawan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DrgTextDark
                )
                Text(
                    text = "${baseArea ?: "Malang Raya"} • Jalur Aman",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(DrgGrabGreenPrimary)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            Text("Satgas 24/7", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
