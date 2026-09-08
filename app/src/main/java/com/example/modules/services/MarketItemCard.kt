package com.example.modules.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ServiceMarketItem
import com.example.ui.theme.*
import java.text.NumberFormat

@Composable
fun MarketItemCard(
    item: ServiceMarketItem,
    onOrder: (ServiceMarketItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DrgTextPrimary
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DrgRedContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${item.discountPercent}%",
                            color = DrgRedDanger,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Ambil: ${item.poskoLocation}",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "Rp ${NumberFormat.getInstance().format(item.priceRp)} • Disubsidi ${item.pointSubsidy} Pts",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgGreenPrimary
                )
            }
            Button(
                onClick = { onOrder(item) },
                colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary, contentColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "Pesan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
