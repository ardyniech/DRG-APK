package com.example.modules.radar.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
fun MapControlToolbar(
    mapMode: FreeMapMode,
    onToggleMapMode: (FreeMapMode) -> Unit,
    isTrafficEnabled: Boolean,
    onToggleTraffic: (Boolean) -> Unit,
    isRadarSweepEnabled: Boolean,
    onToggleRadarSweep: (Boolean) -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenterGps: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Black.copy(alpha = 0.78f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Mode Toggle: Jalan Raya vs Satelit
            IconButton(
                onClick = {
                    val next = if (mapMode == FreeMapMode.ROAD) FreeMapMode.SATELLITE else FreeMapMode.ROAD
                    onToggleMapMode(next)
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = mapMode.icon,
                    contentDescription = mapMode.title,
                    tint = if (mapMode == FreeMapMode.SATELLITE) DrgGoldReward else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Traffic Layer Toggle
            Surface(
                onClick = { onToggleTraffic(!isTrafficEnabled) },
                shape = RoundedCornerShape(8.dp),
                color = if (isTrafficEnabled) DrgTrafficGreen.copy(alpha = 0.25f) else Color.Transparent,
                modifier = Modifier.height(28.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isTrafficEnabled) DrgTrafficGreen else Color.Gray)
                    )
                    Text(
                        text = "Traffic",
                        color = if (isTrafficEnabled) DrgTrafficGreen else Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Radar Sweep Toggle
            IconButton(
                onClick = { onToggleRadarSweep(!isRadarSweepEnabled) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Radar Sweep",
                    tint = if (isRadarSweepEnabled) DrgRadarGlow else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Zoom Controls
            IconButton(onClick = onZoomIn, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color.White, modifier = Modifier.size(16.dp))
            }
            IconButton(onClick = onZoomOut, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.White, modifier = Modifier.size(16.dp))
            }

            // Recenter GPS
            IconButton(onClick = onRecenterGps, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.MyLocation, contentDescription = "Pusat GPS", tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
            }
        }
    }
}
