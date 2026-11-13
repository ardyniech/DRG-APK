package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgIconTileBox
import com.example.ui.theme.*

/**
 * Squircle badge metrics for driver community road patrol & TRC readiness.
 */
@Composable
fun DriverCommunityAtmosphereBadges(
    area: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Squircle Box 1: Area & Jalur Kondusif
        Row(
            modifier = Modifier
                .weight(1.3f)
                .clip(RoundedCornerShape(14.dp))
                .background(DrgGreenContainer.copy(alpha = 0.55f))
                .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(14.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DrgIconTileBox(
                icon = Icons.Default.Navigation,
                tint = DrgGreenPrimary,
                size = 32.dp,
                iconSize = 16.dp,
                contentDescription = "Jalur"
            )
            Column {
                Text(text = area, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgTextDark)
                Text(text = "Jalur Kondusif", fontSize = 10.sp, color = DrgTextSecondary)
            }
        }

        // Squircle Box 2: TRC Satgas 24/7 Siaga
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(DrgSurface.copy(alpha = 0.7f))
                .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(14.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DrgIconTileBox(
                icon = Icons.Default.Shield,
                tint = DrgAmberSecondary,
                size = 32.dp,
                iconSize = 16.dp,
                contentDescription = "TRC"
            )
            Column {
                Text(text = "Satgas TRC", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgTextDark)
                Text(text = "Siaga 24/7", fontSize = 10.sp, color = DrgTextSecondary)
            }
        }
    }
}
