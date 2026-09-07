package com.example.modules.treasury

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun TreasuryHealthCard() {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "📊 Audit Kinerja & Kesehatan Kas DRG",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DrgGreenPrimary
            )
            Text(
                text = "Arus kas bulan ini sangat sehat. Alokasi terbesar disalurkan untuk Santunan Duka & Bantuan Darurat Laka Lantas.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )
            Text(
                text = "Transparansi 100%: Seluruh mutasi dapat diakses real-time oleh anggota resmi.",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = DrgTextMuted
            )
        }
    }
}
