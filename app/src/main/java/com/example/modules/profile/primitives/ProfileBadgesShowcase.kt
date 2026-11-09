package com.example.modules.profile.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shared.models.BadgeItem
import com.example.ui.theme.*

@Composable
fun ProfileBadgesShowcase(
    badges: List<BadgeItem>,
    modifier: Modifier = Modifier
) {
    var selectedBadge by remember { mutableStateOf<BadgeItem?>(null) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = DrgGoldReward, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Lencana & Penghargaan Komunitas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
            }
            Text("${badges.count { it.isEarned }}/${badges.size} Terbuka", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(badges) { b ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (b.isEarned) DrgSurface else Color(0xFFF1F5F9),
                    modifier = Modifier
                        .clickable { selectedBadge = b }
                        .border(1.2.dp, if (b.isEarned) DrgGoldReward else DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = b.iconEmoji, fontSize = 22.sp)
                        Text(text = b.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (b.isEarned) DrgTextPrimary else DrgTextMuted)
                        Text(
                            text = if (b.isEarned) "DIRAIH" else "${b.requiredXp} XP",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = if (b.isEarned) DrgGreenPrimary else DrgTextMuted
                        )
                    }
                }
            }
        }
    }

    selectedBadge?.let { b ->
        Dialog(onDismissRequest = { selectedBadge = null }) {
            Surface(shape = RoundedCornerShape(16.dp), color = DrgSurface, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = b.iconEmoji, fontSize = 38.sp)
                    Text(text = b.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text(text = b.description, fontSize = 12.sp, color = DrgTextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Surface(shape = RoundedCornerShape(8.dp), color = if (b.isEarned) DrgGreenContainer else DrgAmberContainer) {
                        Text(
                            text = if (b.isEarned) "✓ Lencana Resmi Terbuka (${b.earnedDate})" else "Target: Kumpulkan ${b.requiredXp} XP Loyalitas DRG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (b.isEarned) DrgGreenDark else DrgAmberSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    Button(onClick = { selectedBadge = null }, colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary)) {
                        Text("Tutup")
                    }
                }
            }
        }
    }
}
