package com.example.modules.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.InsuranceClaimItem
import com.example.shared.models.ServiceMarketItem
import com.example.ui.theme.*

@Composable
fun MarketHeaderCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgAmberContainer,
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "Koperasi",
                tint = DrgAmberDark,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = "Koperasi & Atribut DRG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgAmberDark
                )
                Text(
                    text = "Harga khusus driver bersubsidi dari Kas Komunitas.",
                    fontSize = 11.sp,
                    color = DrgTextPrimary
                )
            }
        }
    }
}

@Composable
fun MarketItemCard(item: ServiceMarketItem, onOrder: (ServiceMarketItem) -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
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
                    text = "Rp ${String.format("%,d", item.priceRp).replace(',', '.')} • Disubsidi ${item.pointSubsidy} Pts",
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

@Composable
fun KoperasiWholesaleBanner() {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgGreenContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Kulakan Massal",
                    tint = DrgGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "🛒 Program Kulakan Massal Koperasi DRG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgGreenPrimary
                )
            }
            Text(
                text = "Beli Oli Motul & Ban Tubeless partai besar langsung dari Distributor Pabrik. Keuntungan usaha diputar 100% untuk Cadangan Kas SOS Anggota.",
                fontSize = 11.sp,
                color = DrgTextPrimary
            )
        }
    }
}
