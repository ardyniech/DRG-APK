package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
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
    var validationError by remember { mutableStateOf<String?>(null) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = driver.name,
        subtitle = "Plat Motor: ${driver.motorcyclePlate} • ${driver.motorcycleModel}",
        icon = Icons.Default.Badge,
        accentColor = theme.primaryColor,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Tutup", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RoleBadge(role = driver.role)
            Text("• ${driver.loyaltyPoints} XP", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
        }
        Text("Rating Kinerja Rekan: ⭐ ${driver.rating} / 5.0", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgAmberSecondary)

        if (showReviewInput) {
            Text("Beri Rating Solidaritas Jalur:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (1..5).forEach { star ->
                    IconButton(onClick = { rating = star }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Star, contentDescription = "$star Star", tint = if (star <= rating) DrgAmberSecondary else DrgOutline)
                    }
                }
            }
            OutlinedTextField(
                value = comment, onValueChange = { comment = it; validationError = null },
                placeholder = { Text("Ulasan ketaatan / bantuan di jalan...", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = {
                    if (comment.isBlank()) validationError = "Tulis komentar singkat sebelum mengirim ulasan."
                    else { onAddReview(rating, comment.trim()); showReviewInput = false }
                },
                colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Kirim Ulasan") }
        } else {
            OutlinedButton(onClick = { showReviewInput = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Beri Rating & Review Driver Ini")
            }
        }
    }
}
