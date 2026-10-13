package com.example.modules.members

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.members.primitives.PoskoOfflineMapButton
import com.example.shared.models.PoskoLocation
import com.example.shared.utils.WhatsAppLauncher
import com.example.ui.theme.*

@Composable
fun PoskoCard(posko: PoskoLocation, distanceKm: Double? = null) {
    val context = LocalContext.current
    var showContactModal by remember { mutableStateOf(false) }
    val cardShape = RoundedCornerShape(20.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.25f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { WhatsAppLauncher.openGoogleMapsAddress(context, "${posko.name}, ${posko.address}") },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Place, contentDescription = "Peta", tint = DrgGreenPrimary, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = posko.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text(text = "📍 ${posko.address} (${posko.area})", fontSize = 11.sp, color = DrgTextSecondary)
                    Text(text = "Fasilitas: ${posko.facilities} • Pj: ${posko.coordinatorName}", fontSize = 10.sp, color = DrgTextMuted)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                PoskoOfflineMapButton(posko = posko)
                if (posko.phone.isNotBlank()) {
                    IconButton(onClick = { showContactModal = true }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Phone, contentDescription = "Hubungi", tint = DrgGreenPrimary, modifier = Modifier.size(20.dp))
                    }
                }
                if (distanceKm != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(shape = RoundedCornerShape(10.dp), color = DrgGreenContainer) {
                        Text(
                            text = String.format("%.1f KM", distanceKm),
                            fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    if (showContactModal) {
        PoskoContactOptionsModal(posko = posko, onDismiss = { showContactModal = false })
    }
}
