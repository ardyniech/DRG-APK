package com.example.modules.gamification

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.BadgeItem
import com.example.ui.theme.*

@Composable
fun BadgesTabContent(badges: List<BadgeItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(badges) { b ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DrgSurface,
                modifier = Modifier.fillMaxWidth().border(1.dp, if (b.isEarned) DrgAmberSecondary else DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = b.iconEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = b.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Text(text = b.description, fontSize = 11.sp, color = DrgTextSecondary)
                        Text(
                            text = if (b.isEarned) "DIERAIH (${b.earnedDate})" else "Dibutuhkan ${b.requiredXp} XP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (b.isEarned) DrgGreenPrimary else DrgTextMuted
                        )
                    }
                }
            }
        }
    }
}
