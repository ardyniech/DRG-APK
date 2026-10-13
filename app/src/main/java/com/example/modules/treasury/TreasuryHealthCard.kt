package com.example.modules.treasury

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.theme.*

@Composable
fun TreasuryHealthCard(modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White,
            Color.White.copy(alpha = 0.5f),
            DrgGreenPrimary.copy(alpha = 0.25f)
        )
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📊 Audit Kinerja Kas Komunitas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgGreenPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DrgGreenContainer.copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(12.dp))
                    Text("Audit Sehat", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                }
            }
            Text(
                text = "Arus kas bulan ini sangat sehat. Alokasi terbesar disalurkan untuk Santunan Duka & Bantuan Darurat Laka Lantas.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )
            Text(
                text = "Transparansi 100%: Seluruh mutasi kas diaudit real-time oleh anggota resmi.",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = DrgTextMuted
            )
        }
    }
}
