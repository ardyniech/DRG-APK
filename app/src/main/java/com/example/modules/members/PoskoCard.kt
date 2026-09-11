package com.example.modules.members

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.PoskoLocation
import com.example.shared.utils.WhatsAppLauncher
import com.example.ui.theme.*

@Composable
fun PoskoCard(posko: PoskoLocation, distanceKm: Double? = null) {
    val context = LocalContext.current

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Posko",
                    tint = DrgGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = posko.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DrgTextPrimary
                    )
                    Text(
                        text = "📍 ${posko.address} (${posko.area})",
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                    Text(
                        text = "Fasilitas: ${posko.facilities} • Pj: ${posko.coordinatorName}",
                        fontSize = 10.sp,
                        color = DrgTextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (posko.phone.isNotBlank()) {
                    IconButton(
                        onClick = { WhatsAppLauncher.openDialer(context, posko.phone) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Hubungi Posko",
                            tint = DrgGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                if (distanceKm != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DrgGreenContainer
                    ) {
                        Text(
                            text = String.format("%.1f KM", distanceKm),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DrgGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
