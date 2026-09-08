package com.example.modules.radar.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.models.FreeMapMode
import com.example.ui.theme.*

@Composable
fun MapTopControlBar(
    mapMode: FreeMapMode,
    onToggleMapMode: (FreeMapMode) -> Unit,
    isTrafficEnabled: Boolean,
    onToggleTraffic: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Black.copy(alpha = 0.82f),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Mode Peta (Jalan Raya vs Satelit) dengan Label & Ikon Jelas
            val isRoad = mapMode == FreeMapMode.ROAD
            Surface(
                onClick = { onToggleMapMode(if (isRoad) FreeMapMode.SATELLITE else FreeMapMode.ROAD) },
                shape = RoundedCornerShape(10.dp),
                color = if (isRoad) DrgGreenPrimary else DrgGoldReward,
                modifier = Modifier.height(34.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isRoad) Icons.Default.Map else Icons.Default.Satellite,
                        contentDescription = "Mode Peta",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isRoad) "Peta Jalan" else "Satelit",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Toggle Layer Lalu Lintas (Traffic)
            Surface(
                onClick = { onToggleTraffic(!isTrafficEnabled) },
                shape = RoundedCornerShape(10.dp),
                color = if (isTrafficEnabled) DrgTrafficGreen.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.08f),
                modifier = Modifier.height(34.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Traffic,
                        contentDescription = "Lalu Lintas",
                        tint = if (isTrafficEnabled) DrgTrafficGreen else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Traffic",
                        color = if (isTrafficEnabled) DrgTrafficGreen else Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun MapSideControlColumn(
    isRadarSweepEnabled: Boolean,
    onToggleRadarSweep: (Boolean) -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenterGps: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Recenter GPS Button
        SmallFloatingActionButton(
            onClick = onRecenterGps,
            containerColor = DrgGreenPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Pusat GPS", modifier = Modifier.size(20.dp))
        }

        // Radar Sweep Toggle Button
        SmallFloatingActionButton(
            onClick = { onToggleRadarSweep(!isRadarSweepEnabled) },
            containerColor = if (isRadarSweepEnabled) DrgRadarGlow else Color.Black.copy(alpha = 0.78f),
            contentColor = if (isRadarSweepEnabled) Color.Black else Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.Sensors, contentDescription = "Pindai Radar", modifier = Modifier.size(20.dp))
        }

        // Zoom In Button
        SmallFloatingActionButton(
            onClick = onZoomIn,
            containerColor = Color.Black.copy(alpha = 0.78f),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Perbesar Map", modifier = Modifier.size(20.dp))
        }

        // Zoom Out Button
        SmallFloatingActionButton(
            onClick = onZoomOut,
            containerColor = Color.Black.copy(alpha = 0.78f),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Perkecil Map", modifier = Modifier.size(20.dp))
        }
    }
}
