package com.example.modules.admin

import androidx.compose.foundation.background
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
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun AiCommunityHealthCard(memberCount: Int) {
    var expanded by remember { mutableStateOf(false) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(36.dp).background(theme.pillBg, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Analytics, contentDescription = "AI Health Analytics", tint = theme.primaryColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("ANALISIS KESEHATAN KOMUNITAS", fontWeight = FontWeight.Black, fontSize = 12.sp, color = theme.primaryColor)
                        Text("Skor Kesehatan Total: 82/100 (STABIL)", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    }
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Tutup" else "Rincian", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
            }

            Text(
                "Komunitas DRG memiliki fondasi solidaritas lapangan yang sangat solid ($memberCount driver aktif), namun terdapat beberapa celah struktural yang perlu dibenahi.",
                fontSize = 12.sp, color = DrgTextSecondary
            )

            if (expanded) {
                HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))
                Text("🔍 Temuan Audit & Risiko Utama:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                HealthPointRow("🟢 SOLID (92%)", "Respon SOS Satgas Lapangan", "Kecepatan bantuan darurat saat anggota mengalami kecelakaan/mogok di jalan sangat memuaskan (< 7 menit).")
                HealthPointRow("🟢 TRANSPARAN (88%)", "Akuntabilitas Kas Komunitas", "Pengelolaan uang kas masuk & keluar tercatat transparan dan dapat dipantau seluruh anggota.")
                HealthPointRow("🟡 PERLU PERHATIAN (64%)", "Keaktifan Anggota Pasif", "Partisipasi absensi Kopdar & diskusi forum didominasi 35% pengurus senior.")
                HealthPointRow("🔴 RISIKO TINGGI", "Ketergantungan Figur Central", "Pengambilan keputusan darurat terlalu bergantung pada Ketua & Ketua Satgas.")
                Spacer(Modifier.height(4.dp))
                Text("🚀 4 Langkah Konkret Improvement Komunitas:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = theme.primaryColor)
                ImprovementStepRow("1", "Desentralisasi Korwil Posko", "Bentuk Koordinator Wilayah mandiri di tiap Posko.")
                ImprovementStepRow("2", "Program Onboarding Driver Baru", "Sediakan 'Buddy System' pendampingan driver senior.")
                ImprovementStepRow("3", "Piket Bergilir Satgas Radar", "Jadwalkan giliran jam siaga Satgas secara sistematis.")
                ImprovementStepRow("4", "Unit Usaha Koperasi Berkelanjutan", "Gunakan kas untuk kulakan oli/sparepart massal diskon.")
            }
        }
    }
}
