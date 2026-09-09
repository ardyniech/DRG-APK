package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.DrgAmberContainer
import com.example.ui.theme.DrgAmberSecondary
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgOnAmberContainer
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun WorkshopCard(
    bkl: WorkshopPartner,
    onCall: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bkl.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text(text = "📍 ${bkl.address} (${bkl.distanceKm} KM)", fontSize = 11.sp, color = DrgTextSecondary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DrgAmberSecondary)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(text = "DISC ${bkl.discountPercent}%", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Text(text = "🔧 Layanan: ${bkl.services}", fontSize = 11.sp, color = DrgTextPrimary)
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = DrgAmberSecondary, modifier = Modifier.size(13.dp))
                Text(text = "${bkl.rating} (${bkl.reviewCount} Ulasan) • Buka: ${bkl.openHours}", fontSize = 10.sp, color = DrgTextMuted)
            }

            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Hubungi", modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Hubungi Bengkel / Booking", fontSize = 11.sp)
            }
        }
    }
}
