package com.example.modules.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun KoperasiWholesaleBanner(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgGreenContainer,
        modifier = modifier.fillMaxWidth()
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
