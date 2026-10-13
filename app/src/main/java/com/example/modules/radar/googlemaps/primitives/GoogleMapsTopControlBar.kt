package com.example.modules.radar.googlemaps.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgSurface
import com.google.maps.android.compose.MapType

@Composable
fun GoogleMapsTopControlBar(
    mapType: MapType,
    onCycleMapType: () -> Unit,
    isTrafficEnabled: Boolean,
    onToggleTraffic: () -> Unit,
    onlineDriverCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface.copy(alpha = 0.95f),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(DrgGreenPrimary)
                )
                Text(
                    text = "$onlineDriverCount Driver Terhubung",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgGreenPrimary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Traffic Toggle Button
                FilledTonalIconButton(
                    onClick = onToggleTraffic,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (isTrafficEnabled) DrgGreenPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Traffic,
                        contentDescription = "Pantau Macet",
                        tint = if (isTrafficEnabled) DrgRedPanic else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Map Layer Type Toggle
                FilledTonalIconButton(
                    onClick = onCycleMapType,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Ubah Tipe Peta (${mapType.name})",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
