package com.example.modules.profile.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.BadgeItem
import com.example.ui.theme.*

@Composable
fun ProfileBadgesShowcase(
    badges: List<BadgeItem>,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    var selectedBadge by remember { mutableStateOf<BadgeItem?>(null) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = DrgGoldReward, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Lencana & Penghargaan Komunitas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            }
            Text(
                "${badges.count { it.isEarned }}/${badges.size} Terbuka",
                fontSize = 10.sp,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                color = theme.primaryColor
            )
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(badges) { b ->
                DrgLuminousGlassCard(
                    modifier = Modifier.widthIn(min = 86.dp),
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = if (b.isEarned) DrgGlassSurfaceBright else theme.glassBg,
                    borderHighlightColor = if (b.isEarned) DrgGoldReward.copy(alpha = 0.7f) else theme.glassBorderColor.copy(alpha = 0.5f),
                    contentPadding = 10.dp,
                    onClick = { selectedBadge = b }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = b.iconEmoji, fontSize = 22.sp)
                        Text(text = b.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (b.isEarned) DrgTextDark else DrgTextMuted)
                        Text(
                            text = if (b.isEarned) "DIRAIH" else "${b.requiredXp} XP",
                            fontSize = 8.5.sp,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Black,
                            color = if (b.isEarned) theme.primaryColor else DrgTextMuted
                        )
                    }
                }
            }
        }
    }

    selectedBadge?.let { b ->
        DrgLuminousGlassModal(
            title = b.title,
            subtitle = if (b.isEarned) "Lencana Resmi Terbuka" else "Target Penghargaan DRG",
            onDismiss = { selectedBadge = null },
            icon = Icons.Default.WorkspacePremium,
            accentColor = DrgGoldReward,
            footer = { DrgGlassPrimaryButton(text = "Tutup", onClick = { selectedBadge = null }) }
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = b.iconEmoji, fontSize = 40.sp)
                Text(text = b.description, fontSize = 12.sp, color = DrgTextSecondary, textAlign = TextAlign.Center)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (b.isEarned) theme.pillBg else DrgAmberContainer)
                        .border(0.8.dp, (if (b.isEarned) theme.primaryColor else DrgAmberSecondary).copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = if (b.isEarned) "✓ Lencana Resmi Terbuka (${b.earnedDate})" else "Target: Kumpulkan ${b.requiredXp} XP Loyalitas DRG",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (b.isEarned) theme.primaryColor else DrgAmberSecondary
                    )
                }
            }
        }
    }
}
