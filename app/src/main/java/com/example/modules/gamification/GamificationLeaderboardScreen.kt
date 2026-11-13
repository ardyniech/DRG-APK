package com.example.modules.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark

@Composable
fun GamificationLeaderboardScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    badges: List<BadgeItem>,
    tasks: List<CommunityTask>,
    rewards: List<RewardItem>,
    pointTransactions: List<PointTransaction> = emptyList(),
    onAwardPointsClick: () -> Unit,
    onClaimTask: (String) -> Unit,
    onCompleteTask: (String) -> Unit,
    onRedeemReward: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val accent = theme.primaryColor
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Peringkat" to Icons.Default.Leaderboard,
        "Lencana (${badges.size})" to Icons.Default.MilitaryTech,
        "Misi (${tasks.size})" to Icons.Default.Assignment,
        "Tukar Poin" to Icons.Default.CardGiftcard,
        "Riwayat" to Icons.Default.History
    )

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        DrgLuminousGlassCard(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = theme.glassBg,
            borderHighlightColor = theme.glassBorderColor,
            contentPadding = 4.dp
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
                itemsIndexed(tabs) { idx, (label, icon) ->
                    val active = selectedTab == idx
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (active) accent else Color.White.copy(alpha = 0.65f))
                            .border(1.dp, if (active) accent else Color.White, RoundedCornerShape(14.dp))
                            .bouncyClick { selectedTab = idx }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(icon, contentDescription = label, tint = if (active) Color.White else DrgTextDark, modifier = Modifier.size(14.dp))
                        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (selectedTab) {
            0 -> LeaderboardTabContent(members = members, onAwardPointsClick = onAwardPointsClick)
            1 -> BadgesTabContent(badges = badges)
            2 -> TasksTabContent(tasks = tasks, currentMember = currentMember, onClaim = onClaimTask, onComplete = onCompleteTask)
            3 -> RewardsTabContent(rewards = rewards, currentMember = currentMember, onRedeem = onRedeemReward)
            4 -> PointHistoryTabContent(pointTransactions = pointTransactions, currentMember = currentMember)
        }
    }
}
