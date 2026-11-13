package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DrgGlassSurfaceBright
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextDark

@Composable
fun LiveMapControlsFabColumn(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onRecenterGps: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SmallFloatingActionButton(
            onClick = onZoomIn,
            containerColor = DrgGlassSurfaceBright,
            contentColor = DrgTextDark
        ) {
            Icon(Icons.Default.Add, contentDescription = "Zoom In")
        }
        SmallFloatingActionButton(
            onClick = onZoomOut,
            containerColor = DrgGlassSurfaceBright,
            contentColor = DrgTextDark
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
        }
        SmallFloatingActionButton(
            onClick = onRecenterGps,
            containerColor = theme.primaryColor,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = "Pusat GPS")
        }
    }
}
