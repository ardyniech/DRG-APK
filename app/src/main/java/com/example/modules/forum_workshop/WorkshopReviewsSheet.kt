package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(20.dp), color = DrgSurface, modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Ulasan Rekan Driver DRG", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        Text(text = workshop.name, fontSize = 12.sp, color = DrgGreenPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = DrgGoldReward, modifier = Modifier.size(16.dp))
                        Text(text = " ${workshop.rating} (${workshop.reviewCount})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                }

                HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f))

                LazyColumn(modifier = Modifier.weight(1f, fill = false), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (reviews.isEmpty()) {
                        item {
                            Text("Belum ada ulasan tertulis. Jadilah driver pertama yang memberikan ulasan!", fontSize = 11.sp, color = DrgTextSecondary, modifier = Modifier.padding(vertical = 16.dp))
                        }
                    } else {
                        items(reviews) { r ->
                            Surface(shape = RoundedCornerShape(10.dp), color = DrgBackground, modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "${r.reviewerName} (${r.reviewerRole.title})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = null, tint = DrgGoldReward, modifier = Modifier.size(12.dp))
                                            Text(text = "${r.rating.toInt()}/5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                                        }
                                    }
                                    Text(text = "Layanan: ${r.tag} • ${r.dateText}", fontSize = 9.sp, color = DrgGreenPrimary, fontWeight = FontWeight.SemiBold)
                                    Text(text = "\"${r.comment}\"", fontSize = 11.sp, color = DrgTextSlate)
                                }
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Tutup", fontSize = 11.sp) }
                    Button(onClick = { onDismiss(); onOpenWriteReview() }, colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary), modifier = Modifier.weight(1f)) {
                        Text("Tulis Ulasan", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
