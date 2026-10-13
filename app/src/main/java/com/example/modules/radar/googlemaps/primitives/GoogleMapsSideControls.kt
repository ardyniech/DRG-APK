package com.example.modules.radar.googlemaps.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgGreenPrimary

@Composable
fun GoogleMapsSideControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SmallFloatingActionButton(
            onClick = onZoomIn,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Perbesar Peta", tint = DrgGreenPrimary)
        }

        SmallFloatingActionButton(
            onClick = onZoomOut,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Perkecil Peta", tint = DrgGreenPrimary)
        }

        SmallFloatingActionButton(
            onClick = onRecenter,
            containerColor = DrgGreenPrimary,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Fokus Lokasi Saya", tint = MaterialTheme.colorScheme.surface)
        }
    }
}
