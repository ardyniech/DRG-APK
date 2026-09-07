package com.example.modules.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AiCommunityHealthCard(memberCount: Int) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, DrgGreenPrimary, RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(DrgGreenContainer, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "AI Health Analytics",
                            tint = DrgGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "ANALISIS KESEHATAN KOMUNITAS AI",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = DrgGreenPrimary
                        )
                        Text(
                            text = "Skor Kesehatan Total: 82/100 (SEHAT & STABIL)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = DrgTextPrimary
                        )
                    }
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Sembunyikan" else "Rincian Jujur", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                }
            }

            Text(
                text = "Komunitas DRG memiliki fondasi solidaritas lapangan yang sangat solid ($memberCount driver aktif), namun terdapat beberapa celah struktural yang perlu dibenahi.",
                fontSize = 12.sp,
                color = DrgTextSecondary
            )

            if (expanded) {
                Divider(color = DrgOutline.copy(alpha = 0.5f))
                Text("🔍 Temuan Audit & Risiko Utama:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)

                HealthPointRow(status = "🟢 SOLID (92%)", title = "Respon SOS Satgas Lapangan", desc = "Kecepatan bantuan darurat saat anggota mengalami kecelakaan/mogok di jalan sangat memuaskan (< 7 menit).")
                HealthPointRow(status = "🟢 TRANSPARAN (88%)", title = "Akuntabilitas Kas Komunitas", desc = "Pengelolaan uang kas masuk & keluar tercatat transparan dan dapat dipantau seluruh anggota.")
                HealthPointRow(status = "🟡 PERLU PERHATIAN (64%)", title = "Keaktifan Anggota Pasif", desc = "Partisipasi absensi Kopdar & diskusi forum didominasi 35% pengurus senior.")
                HealthPointRow(status = "🔴 RISIKO TINGGI", title = "Ketergantungan Figur Central", desc = "Pengambilan keputusan darurat terlalu bergantung pada Ketua & Ketua Satgas.")

                Spacer(modifier = Modifier.height(4.dp))
                Text("🚀 4 Langkah Konkret Improvement Komunitas:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgGreenDark)

                ImprovementStepRow(num = "1", title = "Desentralisasi Korwil Posko", desc = "Bentuk Koordinator Wilayah mandiri di tiap Posko.")
                ImprovementStepRow(num = "2", title = "Program Onboarding Driver Baru", desc = "Sediakan 'Buddy System' pendampingan driver senior.")
                ImprovementStepRow(num = "3", title = "Piket Bergilir Satgas Radar", desc = "Jadwalkan giliran jam siaga Satgas secara sistematis.")
                ImprovementStepRow(num = "4", title = "Unit Usaha Koperasi Berkelanjutan", desc = "Gunakan kas untuk kulakan oli/sparepart massal diskon.")
            }
        }
    }
}

@Composable
private fun HealthPointRow(status: String, title: String, desc: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
            Text(status, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenDark)
        }
        Text(desc, fontSize = 11.sp, color = DrgTextSecondary)
    }
}

@Composable
private fun ImprovementStepRow(num: String, title: String, desc: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Box(
            modifier = Modifier.size(20.dp).background(DrgGreenContainer, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(num, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenPrimary)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
            Text(desc, fontSize = 11.sp, color = DrgTextSecondary)
        }
    }
}
