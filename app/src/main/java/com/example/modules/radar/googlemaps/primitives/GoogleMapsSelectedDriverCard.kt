package com.example.modules.radar.googlemaps.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgSurface
import java.util.Locale

@Composable
fun GoogleMapsSelectedDriverCard(
    driver: DriverMember,
    distanceKm: Double,
    onCallDriver: (String) -> Unit,
    onFocusLocation: (Double, Double) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = driver.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "${driver.driverId} • ${driver.motorcyclePlate} (${driver.motorcycleModel})", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup Detail", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(8.dp), color = DrgGreenPrimary.copy(alpha = 0.12f)) {
                    Text(text = "Status: ${driver.currentStatus}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgGreenPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Text(text = String.format(Locale.US, "Jarak: %.1f km", distanceKm), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onCallDriver(driver.phone) }, colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary), modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp)) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Hubungi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(onClick = { onFocusLocation(driver.currentLat, driver.currentLng) }, modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp)) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Fokus GPS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
