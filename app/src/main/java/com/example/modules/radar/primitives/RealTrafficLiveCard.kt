package com.example.modules.radar.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun RealTrafficLiveCard(
    onOpenRealTraffic: () -> Unit,
    onReportJam: () -> Unit,
    trafficHazardsCount: Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val isBusy = trafficHazardsCount > 0
    val statusColor = if (isBusy) DrgTrafficOrange else theme.primaryColor

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = DrgGlassSurfaceStrong,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(DrgTrafficRed.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Traffic, contentDescription = null, tint = DrgTrafficRed, modifier = Modifier.size(20.dp)) }
                    Column {
                        Text("Pantauan Lalu Lintas Real-Time", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                        Text("Layer Resmi Google Maps Traffic", fontSize = 11.sp, color = DrgTextSecondary)
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.14f))
                        .border(0.8.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        if (isBusy) "$trafficHazardsCount Laporan Macet" else "Kondisi Lancar",
                        fontSize = 10.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Text(
                "Buka layer Traffic Google Maps untuk pantauan kemacetan jalan, estimasi waktu, & kecepatan live Malang Raya.",
                fontSize = 11.sp,
                color = DrgTextSecondary,
                lineHeight = 15.sp
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onOpenRealTraffic,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgTrafficRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Buka Live Traffic", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                OutlinedButton(
                    onClick = onReportJam,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, theme.primaryColor.copy(alpha = 0.45f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.AddAlert, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Lapor Macet", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }
        }
    }
}
