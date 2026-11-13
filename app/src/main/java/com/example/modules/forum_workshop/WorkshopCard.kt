package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassCard
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun WorkshopCard(
    bkl: WorkshopPartner,
    onCall: () -> Unit,
    onRateClick: () -> Unit = {},
    onViewReviewsClick: () -> Unit = {},
    onBookingClick: () -> Unit = onCall
) {
    DrgGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp,
        borderBrush = Brush.linearGradient(
            listOf(Color.White.copy(alpha = 0.96f), Color.White.copy(alpha = 0.52f), DrgAmberSecondary.copy(alpha = 0.35f))
        )
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
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(DrgAmberSecondary).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = "DISC ${bkl.discountPercent}%", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Text(text = "🔧 Layanan: ${bkl.services}", fontSize = 11.sp, color = DrgTextPrimary)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = DrgGoldReward, modifier = Modifier.size(14.dp))
                Text(text = "${bkl.rating} (${bkl.reviewCount} ulasan) • Buka: ${bkl.openHours}", fontSize = 10.sp, color = DrgTextMuted)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = onViewReviewsClick,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(36.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text("Ulasan (${bkl.reviewCount})", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
                OutlinedButton(
                    onClick = onRateClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgAmberWarning),
                    modifier = Modifier.weight(1f).height(36.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Beri Nilai", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onBookingClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1.2f).height(36.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Hubungi", modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Booking", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
