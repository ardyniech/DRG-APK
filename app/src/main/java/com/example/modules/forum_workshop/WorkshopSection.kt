package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun WorkshopSection(
    workshops: List<WorkshopPartner>,
    onCallWorkshop: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DrgAmberContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = "Bengkel Rekanan", tint = DrgAmberSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Diskon khusus anggota DRG s/d 20% cukup tunjukkan KTA digital.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DrgOnAmberContainer
                    )
                }
            }
        }

        items(workshops) { bkl ->
            WorkshopCard(bkl = bkl, onCall = { onCallWorkshop(bkl.phone) })
        }
    }
}

@Composable
private fun WorkshopCard(
    bkl: WorkshopPartner,
    onCall: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bkl.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary)
                    Text(text = "📍 ${bkl.address} (${bkl.distanceKm} KM)", fontSize = 11.sp, color = DrgTextSecondary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DrgAmberSecondary)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "DISC ${bkl.discountPercent}%", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Text(text = "🔧 Layanan: ${bkl.services}", fontSize = 11.sp, color = DrgTextPrimary)
            Text(text = "⭐ ${bkl.rating} (${bkl.reviewCount} Ulasan) • Buka: ${bkl.openHours}", fontSize = 10.sp, color = DrgTextMuted)

            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Hubungi Bengkel", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hubungi Bengkel / Booking Servis", fontSize = 11.sp)
            }
        }
    }
}
