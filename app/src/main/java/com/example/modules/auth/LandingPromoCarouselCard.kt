package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class PromoData(val title: String, val description: String, val emoji: String)

@Composable
fun LandingPromoCarouselCard(
    promoList: List<PromoData>,
    promoIndex: Int,
    modifier: Modifier = Modifier
) {
    val promo = promoList[promoIndex]
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(1.dp, DrgBorderLight, RoundedCornerShape(20.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(promo.emoji, fontSize = 42.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(promo.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DrgTextDark, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(promo.description, fontSize = 12.sp, color = DrgTextSlate, textAlign = TextAlign.Center, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                promoList.indices.forEach { idx ->
                    Box(
                        modifier = Modifier
                            .size(if (idx == promoIndex) 16.dp else 6.dp, 6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (idx == promoIndex) DrgGrabGreenPrimary else DrgBorderLight)
                    )
                }
            }
        }
    }
}
