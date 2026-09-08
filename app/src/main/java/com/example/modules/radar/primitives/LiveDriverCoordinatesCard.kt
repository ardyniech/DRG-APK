package com.example.modules.radar.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.logic.CoordinateUtils
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun LiveDriverCoordinatesCard(
    driver: DriverMember,
    distanceKm: Double,
    onCall: (String) -> Unit,
    onFocusCoordinates: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.6f), RoundedCornerShape(16.dp)).testTag("live_driver_coordinates_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        RoleBadge(role = driver.role)
                    }
                    Text(text = "${driver.motorcycleModel} • ${driver.motorcyclePlate}", fontSize = 11.sp, color = DrgTextSecondary)
                }

                Surface(shape = RoundedCornerShape(8.dp), color = DrgGreenContainer) {
                    Text(
                        text = String.format("%.2f km", distanceKm),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📍 " + CoordinateUtils.formatCoordinates(driver.currentLat, driver.currentLng),
                    fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary
                )
                Text(
                    text = "Status: ${driver.currentStatus}",
                    fontSize = 11.sp, fontWeight = FontWeight.Medium, color = DrgGreenPrimary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onFocusCoordinates(driver.currentLat, driver.currentLng) },
                    modifier = Modifier.weight(1f).height(44.dp).testTag("recenter_coordinates_button")
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = "Fokus Koordinat", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fokus GPS", fontSize = 11.sp)
                }

                Button(
                    onClick = { onCall(driver.phone) },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    modifier = Modifier.weight(1f).height(44.dp).testTag("call_driver_button")
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Hubungi Driver", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hubungi", fontSize = 11.sp)
                }
            }
        }
    }
}
