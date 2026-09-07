package com.example.modules.members

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
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun DriverDetailDialog(
    driver: DriverMember,
    onDismiss: () -> Unit,
    onAddReview: (Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var showReviewInput by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = driver.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    RoleBadge(role = driver.role)
                }

                Text(
                    text = "Plat Motor: ${driver.motorcyclePlate} • ${driver.motorcycleModel}",
                    fontSize = 12.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "Poin Loyalitas: ${driver.loyaltyPoints} XP",
                    fontSize = 12.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "Rating Kinerja Rekan: ⭐ ${driver.rating} / 5.0",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgAmberSecondary
                )

                if (showReviewInput) {
                    Text("Beri Rating Kepadatan & Solidaritas Jalur:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { rating = star }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star Star",
                                    tint = if (star <= rating) DrgAmberSecondary else DrgOutline
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = { Text("Ulasan ketaatan / bantuan di jalan...", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            if (comment.isNotBlank()) {
                                onAddReview(rating, comment)
                                showReviewInput = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kirim Ulasan")
                    }
                } else {
                    OutlinedButton(
                        onClick = { showReviewInput = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Beri Rating & Review Driver Ini")
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DrgGrabGreenPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tutup", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
