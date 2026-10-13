package com.example.modules.radar.googlemaps.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.DrgGoldReward
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgSurface

@Composable
fun GoogleMapsApiKeyNoticeBanner(
    onSwitchToFreeMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val apiKey = try { BuildConfig.MAPS_API_KEY } catch (_: Throwable) { "" }
    val isPlaceholder = apiKey.isBlank() || apiKey.contains("YOUR_GOOGLE_MAPS_API_KEY")

    if (isPlaceholder) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DrgSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info API Key",
                        tint = DrgGoldReward,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Mode Pratinjau Google Maps",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = DrgGreenPrimary
                        )
                        Text(
                            text = "Kunci MAPS_API_KEY dapat diatur via Secrets AI Studio",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                TextButton(
                    onClick = onSwitchToFreeMap,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Peta Bebas", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
