package com.example.modules.forum_workshop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun ReviewWorkshopDialog(
    workshop: WorkshopPartner,
    onDismiss: () -> Unit,
    onSubmitReview: (Float, String, String) -> Unit
) {
    var rating by remember { mutableFloatStateOf(5.0f) }
    var selectedService by remember { mutableStateOf("Servis Injeksi / Tune-up") }
    var comment by remember { mutableStateOf("") }
    val services = listOf("Servis Injeksi / Tune-up", "Ganti Oli Cepat", "Tambal Ban Tubeless", "Kelistrikan & Aki", "Turun Mesin")
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Beri Ulasan Bengkel",
        subtitle = "🔧 ${workshop.name}",
        icon = Icons.Default.Star,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = { onSubmitReview(rating, selectedService, comment.ifBlank { "Pelayanan memuaskan dan harga bersahabat untuk armada driver DRG." }) },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Kirim (+15 XP)", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Text("Bagikan pengalaman servis agar rekan driver DRG mendapatkan rekomendasi terbaik.", fontSize = 11.sp, color = DrgTextSecondary)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Penilaian:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
            Spacer(Modifier.width(4.dp))
            (1..5).forEach { star ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "$star Bintang",
                    tint = if (star <= rating) DrgGoldReward else Color.LightGray,
                    modifier = Modifier.size(28.dp).clickable { rating = star.toFloat() }
                )
            }
            Text(" (${rating.toInt()} / 5)", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgGoldReward)
        }
        Text("Jenis Perawatan / Servis:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            services.forEach { s ->
                FilterChip(
                    selected = selectedService == s, onClick = { selectedService = s },
                    label = { Text(s.split(" ").take(2).joinToString(" "), fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
                )
            }
        }
        OutlinedTextField(
            value = comment, onValueChange = { comment = it },
            label = { Text("Komentar & Pengalaman Driver", fontSize = 11.sp) },
            placeholder = { Text("Contoh: Pengerjaan cepat, mekanik ramah, diskon DRG langsung dipotong.", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().height(88.dp), maxLines = 3, shape = RoundedCornerShape(12.dp)
        )
    }
}
