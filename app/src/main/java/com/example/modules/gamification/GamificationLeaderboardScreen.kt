package com.example.modules.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.*
import com.example.ui.theme.*

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
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = DrgBackground,
            contentColor = DrgAmberSecondary,
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Peringkat", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Peringkat", modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Lencana (${badges.size})", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.MilitaryTech, contentDescription = "Lencana", modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Misi (${tasks.size})", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.Assignment, contentDescription = "Misi", modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text("Tukar Poin", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.CardGiftcard, contentDescription = "Tukar", modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedTab == 4,
                onClick = { selectedTab = 4 },
                text = { Text("Riwayat", fontSize = 11.sp) },
                icon = { Icon(Icons.Default.History, contentDescription = "Riwayat", modifier = Modifier.size(16.dp)) }
            )
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
