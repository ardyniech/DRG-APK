package com.example.modules.radar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.logic.CoordinateUtils
import com.example.modules.radar.primitives.DriverRadarCard
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun DriverRadarScreen(
    members: List<DriverMember>,
    currentMember: DriverMember? = null,
    onCallDriver: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var maxRadiusKm by remember { mutableStateOf(5.0f) }
    val centerLat = currentMember?.currentLat ?: -7.9822
    val centerLng = currentMember?.currentLng ?: 112.6303

    val nearbyDrivers = remember(members, centerLat, centerLng, maxRadiusKm) {
        CoordinateUtils.filterNearbyDrivers(members, centerLat, centerLng, maxRadiusKm.toDouble())
            .filter { it.first.id != currentMember?.id }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DrgSurface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Radar, contentDescription = "Radar", tint = DrgGreenPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Radar Rekan Terdekat (Privasi Terjaga)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Koordinat GPS exact disamarkan untuk privasi pengemudi. Menampilkan jarak proksimitas real-time dalam radius ${maxRadiusKm.toInt()} km.",
                    fontSize = 12.sp, color = DrgTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Radius: ${maxRadiusKm.toInt()} km", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    Slider(
                        value = maxRadiusKm,
                        onValueChange = { maxRadiusKm = it },
                        valueRange = 1f..20f,
                        steps = 19,
                        modifier = Modifier.weight(1f).padding(start = 12.dp),
                        colors = SliderDefaults.colors(thumbColor = DrgGreenPrimary, activeTrackColor = DrgGreenPrimary)
                    )
                }
            }
        }

        Text(
            text = "Ditemukan ${nearbyDrivers.size} Rekan dalam Jangkauan Radar",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DrgTextPrimary
        )

        if (nearbyDrivers.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text("Tidak ada rekan aktif dalam radius ${maxRadiusKm.toInt()} km", fontSize = 13.sp, color = DrgTextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(nearbyDrivers) { (driver, distance) ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DriverRadarCard(driver = driver, onCall = { onCallDriver(driver.phone) })
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Proximity", tint = DrgGreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Proksimitas: ~${String.format(java.util.Locale.US, "%.1f", distance)} km • Area: ${driver.baseArea} (Koordinat disamarkan)",
                                fontSize = 11.sp,
                                color = DrgTextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
