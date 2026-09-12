package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.logic.CoordinateUtils
import com.example.ui.theme.*

@Composable
fun LiveMapCoordinateHeader(
    centerLat: Double,
    centerLng: Double,
    nearbyCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Pusat GPS",
                    tint = DrgGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "Area Pantau GPS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = DrgTextSecondary
                    )
                    Text(
                        text = CoordinateUtils.formatCoordinates(centerLat, centerLng),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextPrimary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DrgGreenContainer
            ) {
                Text(
                    text = "$nearbyCount Driver di Layar",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgGreenPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }
}
