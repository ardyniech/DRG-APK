package com.example.modules.radar.primitives

import androidx.compose.foundation.background
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
    selectedRadiusKm: Double,
    onSelectRadius: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    Text(
                        text = CoordinateUtils.formatCoordinates(centerLat, centerLng),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DrgGreenContainer
                ) {
                    Text(
                        text = "$nearbyCount Driver Aktif",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgGreenPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Radius:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = DrgTextSecondary
                )
                listOf(1.0, 3.0, 5.0, 10.0).forEach { radius ->
                    val isSelected = selectedRadiusKm == radius
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectRadius(radius) },
                        label = { Text("${radius.toInt()} km", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DrgGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.height(32.dp)
                    )
                }
            }
        }
    }
}
