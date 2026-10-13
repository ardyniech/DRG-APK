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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun WorkshopCard(
    bkl: WorkshopPartner,
    onCall: () -> Unit
) {
    val cardShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgAmberSecondary.copy(alpha = 0.25f))
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
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = "DISC ${bkl.discountPercent}%", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Text(text = "🔧 Layanan: ${bkl.services}", fontSize = 11.sp, color = DrgTextPrimary)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = DrgGoldReward, modifier = Modifier.size(14.dp))
                Text(text = "${bkl.rating} (${bkl.reviewCount} ulasan) • Buka: ${bkl.openHours}", fontSize = 10.sp, color = DrgTextMuted)
            }

            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(38.dp)
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Hubungi", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hubungi Bengkel / Booking", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
