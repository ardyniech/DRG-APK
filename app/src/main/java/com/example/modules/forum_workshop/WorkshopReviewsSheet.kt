package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverReview
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun WorkshopReviewsSheet(
    workshop: WorkshopPartner,
    reviews: List<DriverReview>,
    onDismiss: () -> Unit,
    onOpenWriteReview: () -> Unit
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassModal(
        title = "Ulasan Rekan Driver DRG",
        subtitle = "${workshop.name} • ⭐ ${workshop.rating} (${workshop.reviewCount})",
        icon = Icons.Default.Star,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Tutup", fontSize = 11.sp) }
                Button(
                    onClick = { onDismiss(); onOpenWriteReview() },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Tulis Ulasan", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        if (reviews.isEmpty()) {
            Text("Belum ada ulasan tertulis. Jadilah driver pertama yang memberikan ulasan!", fontSize = 11.sp, color = DrgTextSecondary, modifier = Modifier.padding(vertical = 12.dp))
        } else {
            reviews.forEach { r ->
                DrgLuminousGlassCard(shape = RoundedCornerShape(14.dp), backgroundColor = theme.pillBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 10.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("${r.reviewerName} (${r.reviewerRole.title})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = DrgGoldReward, modifier = Modifier.size(12.dp))
                                Text("${r.rating.toInt()}/5", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                            }
                        }
                        Text("Layanan: ${r.tag} • ${r.dateText}", fontSize = 9.sp, color = theme.primaryColor, fontWeight = FontWeight.SemiBold)
                        Text("\"${r.comment}\"", fontSize = 11.sp, color = DrgTextSlate)
                    }
                }
            }
        }
    }
}
