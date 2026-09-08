package com.example.modules.gamification

import androidx.compose.foundation.border
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

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DrgAmberContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🪙 Saldo Poin Poin Anda: $memberXp XP", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgOnAmberContainer)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(rewards) { r ->
                val canAfford = memberXp >= r.requiredPoints
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DrgSurface,
                    modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = r.iconEmoji, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = r.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                            Text(text = r.description, fontSize = 11.sp, color = DrgTextSecondary)
                            Text(text = "Stok: ${r.stockAvailable} | Harga: ${r.requiredPoints} XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
                        }
                        Button(
                            onClick = { onRedeem(r.id) },
                            enabled = canAfford && r.stockAvailable > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary)
                        ) {
                            Text("Tukar", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
