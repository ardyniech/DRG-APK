package com.example.modules.forum_workshop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.window.Dialog
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

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(20.dp), color = DrgSurface, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Beri Ulasan Bengkel", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(text = "🔧 ${workshop.name}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DrgGreenPrimary)
                Text(text = "Bagikan pengalaman servis agar rekan driver DRG mendapatkan rekomendasi terbaik.", fontSize = 11.sp, color = DrgTextSecondary)

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Penilaian:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    (1..5).forEach { star ->
                        val isFilled = star <= rating
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "$star Bintang",
                            tint = if (isFilled) DrgGoldReward else Color.LightGray,
                            modifier = Modifier.size(28.dp).clickable { rating = star.toFloat() }
                        )
                    }
                    Text(" (${rating.toInt()} / 5)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgGoldReward)
                }

                Text("Jenis Perawatan / Servis:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    services.take(3).forEach { s ->
                        FilterChip(
                            selected = selectedService == s,
                            onClick = { selectedService = s },
                            label = { Text(s.split(" ").take(2).joinToString(" "), fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
                        )
                    }
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Komentar & Pengalaman Driver", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Pengerjaan cepat, mekanik ramah, diskon DRG 15% langsung dipotong.", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().height(88.dp),
                    maxLines = 3
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Batal") }
                    Button(
                        onClick = { onSubmitReview(rating, selectedService, comment.ifBlank { "Pelayanan memuaskan dan harga bersahabat untuk armada driver DRG." }) },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        modifier = Modifier.weight(1f)
                    ) { Text("Kirim (+15 XP)") }
                }
            }
        }
    }
}
