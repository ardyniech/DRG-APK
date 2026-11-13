package com.example.modules.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.repository.CommunitySeedData
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.BadgeItem
import com.example.ui.theme.*

@Composable
fun BadgesTabContent(badges: List<BadgeItem>) {
    val displayBadges = if (badges.isNotEmpty()) badges else CommunitySeedData.defaultBadges()
    val theme = LocalAppVisualTheme.current

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        items(displayBadges) { b ->
            DrgLuminousGlassCard(
                shape = RoundedCornerShape(18.dp),
                backgroundColor = theme.glassBg,
                borderHighlightColor = if (b.isEarned) DrgAmberSecondary.copy(alpha = 0.65f) else theme.glassBorderColor,
                contentPadding = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(b.iconEmoji, fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(b.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Text(b.description, fontSize = 11.sp, color = DrgTextSecondary)
                        Text(
                            text = if (b.isEarned) "DIRAIH (${b.earnedDate})" else "Dibutuhkan ${b.requiredXp} XP",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (b.isEarned) theme.primaryColor else DrgTextMuted
                        )
                    }
                }
            }
        }
    }
}
