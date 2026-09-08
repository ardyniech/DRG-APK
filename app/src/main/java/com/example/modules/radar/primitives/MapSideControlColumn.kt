package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

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
        SmallFloatingActionButton(
            onClick = onRecenterGps,
            containerColor = DrgGreenPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Pusat GPS", modifier = Modifier.size(20.dp))
        }

        SmallFloatingActionButton(
            onClick = { onToggleRadarSweep(!isRadarSweepEnabled) },
            containerColor = if (isRadarSweepEnabled) DrgRadarGlow else Color.Black.copy(alpha = 0.78f),
            contentColor = if (isRadarSweepEnabled) Color.Black else Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.Sensors, contentDescription = "Pindai Radar", modifier = Modifier.size(20.dp))
        }

        SmallFloatingActionButton(
            onClick = onZoomIn,
            containerColor = Color.Black.copy(alpha = 0.78f),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(42.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Perbesar Map", modifier = Modifier.size(20.dp))
        }

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
