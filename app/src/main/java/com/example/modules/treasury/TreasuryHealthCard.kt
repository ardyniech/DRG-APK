package com.example.modules.treasury

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun TreasuryHealthCard(modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = DrgGlassSurfaceStrong,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Audit Kinerja Kas Komunitas", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = theme.primaryColor)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(theme.pillBg)
                        .border(0.8.dp, theme.primaryColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(12.dp))
                    Text("Audit Sehat", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }
            Text(
                text = "Ringkasan kas ditampilkan dari data transaksi yang tercatat di aplikasi. Gunakan rincian mutasi untuk verifikasi.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )
            Text(
                text = "Catatan: transparansi/sinkronisasi antar-anggota bergantung pada ketersediaan backend & data komunitas (saat ini lokal-first).",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = DrgTextMuted
            )
        }
    }
}
