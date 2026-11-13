package com.example.modules.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.EmptyStateOrganicGraphic
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.RewardItem
import com.example.ui.theme.*

@Composable
fun RewardsTabContent(
    rewards: List<RewardItem>,
    currentMember: DriverMember?,
    onRedeem: (String) -> Unit
) {
    val memberXp = currentMember?.loyaltyPoints ?: 0
    val theme = LocalAppVisualTheme.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DrgLuminousGlassCard(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = DrgAmberContainer.copy(alpha = 0.85f),
            borderHighlightColor = DrgAmberSecondary.copy(alpha = 0.5f),
            contentPadding = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🪙 Saldo Poin Anda: $memberXp XP", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgOnAmberContainer)
            }
        }

        if (rewards.isEmpty()) {
            EmptyStateOrganicGraphic(title = "Voucher Belum Tersedia", message = "Kupon servis dan oli gratis sedang disiapkan bersama bengkel rekanan resmi.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                items(rewards) { r ->
                    val canAfford = memberXp >= r.requiredPoints
                    DrgLuminousGlassCard(
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = theme.glassBg,
                        borderHighlightColor = theme.glassBorderColor,
                        contentPadding = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(r.iconEmoji, fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(r.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                                Text(r.description, fontSize = 11.sp, color = DrgTextSecondary)
                                Text("Stok: ${r.stockAvailable} | Harga: ${r.requiredPoints} XP", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
                            }
                            Button(
                                onClick = { onRedeem(r.id) },
                                enabled = canAfford && r.stockAvailable > 0,
                                colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("Tukar", fontSize = 11.sp) }
                        }
                    }
                }
            }
        }
    }
}
