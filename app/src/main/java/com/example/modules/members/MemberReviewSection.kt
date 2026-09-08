package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MemberReviewSection(
    memberId: String,
    onSubmitReview: (String, Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Apresiasi & Ulasan Solidaritas", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenPrimary)
        Text(text = "Beri rating etika berkendara dan kesetiakawanan di jalan.", fontSize = 10.sp, color = DrgTextSecondary)

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { star ->
                IconButton(onClick = { rating = star }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "$star Bintang",
                        tint = if (star <= rating) DrgAmberSecondary else DrgTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Contoh: Rekan sangat solid membantu mogok...", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().height(70.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Button(
            onClick = { onSubmitReview(memberId, rating, comment) },
            colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(36.dp)
        ) {
            Text("Kirim Ulasan & Rating", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
