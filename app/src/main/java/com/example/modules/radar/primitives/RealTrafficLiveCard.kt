package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun RealTrafficLiveCard(
    onOpenRealTraffic: () -> Unit,
    onReportJam: () -> Unit,
    trafficHazardsCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp), color = DrgSurface, shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(10.dp), color = DrgTrafficRed.copy(alpha = 0.14f), modifier = Modifier.size(34.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Traffic, contentDescription = null, tint = DrgTrafficRed, modifier = Modifier.size(20.dp))
                        }
                    }
                    Column {
                        Text("Pantauan Lalu Lintas Real-Time", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        Text("Layer Resmi Google Maps Traffic", fontSize = 11.sp, color = DrgTextSecondary)
                    }
                }
                val isBusy = trafficHazardsCount > 0
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isBusy) DrgTrafficOrange.copy(alpha = 0.15f) else DrgTrafficGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        if (isBusy) "$trafficHazardsCount Laporan Macet" else "Kondisi Lancar",
                        fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = if (isBusy) DrgTrafficOrange else DrgTrafficGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                "Buka layer Traffic Google Maps untuk pantauan kemacetan jalan, estimasi waktu, & kecepatan live Malang Raya.",
                fontSize = 11.sp, color = DrgTextSecondary, lineHeight = 15.sp
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onOpenRealTraffic,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgTrafficRed),
                    shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Buka Live Traffic", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                OutlinedButton(
                    onClick = onReportJam,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.AddAlert, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Lapor Macet", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgGreenPrimary)
                }
            }
        }
    }
}
