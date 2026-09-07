package com.example.modules.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.forum_workshop.ForumAndWorkshopScreen
import com.example.modules.gamification.GamificationLeaderboardScreen
import com.example.modules.members.MembersAndPoskoScreen
import com.example.modules.treasury.TransparencyReportScreen
import com.example.modules.treasury.TreasuryScreen
import com.example.modules.services.DriverServicesScreen
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun CommunityHubScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    transactions: List<KasTransaction>,
    totalIncome: Long = 0,
    totalExpense: Long = 0,
    netBalance: Long = 0,
    posts: List<ForumPost>,
    workshops: List<WorkshopPartner>,
    badges: List<BadgeItem>,
    tasks: List<CommunityTask>,
    rewards: List<RewardItem>,
    poskoList: List<PoskoLocation>,
    pointTransactions: List<PointTransaction> = emptyList(),
    adminLogs: List<AdminLog> = emptyList(),
    initialSubTab: Int = 0,
    onAddTransactionClick: () -> Unit = {},
    onCreatePost: (String, String, ForumCategory, PostType) -> Unit = { _, _, _, _ -> },
    onToggleLike: (String, Boolean) -> Unit = { _, _ -> },
    onDeletePost: (String) -> Unit = {},
    onCallWorkshop: (String) -> Unit = {},
    onAwardPointsClick: () -> Unit = {},
    onClaimTask: (String) -> Unit = {},
    onCompleteTask: (String) -> Unit = {},
    onRedeemReward: (String) -> Unit = {},
    onRecordKopdarAttendance: () -> Unit = {},
    onAddReview: (String, Int, String) -> Unit = { _, _, _ -> },
    onUpdateMemberRole: (String, MemberRole) -> Unit = { _, _ -> },
    onUpdateMemberVerification: (String, VerificationStatus) -> Unit = { _, _ -> },
    onOrderMarketItem: (ServiceMarketItem) -> Unit = {},
    onClaimInsurance: (InsuranceClaimItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableStateOf(initialSubTab) }
    val subTabs = listOf(
        "Kas DRG" to Icons.Default.AccountBalanceWallet,
        "Forum" to Icons.Default.Forum,
        "Misi & Poin" to Icons.Default.EmojiEvents,
        "Anggota" to Icons.Default.People,
        "Layanan & Koperasi" to Icons.Default.ShoppingBag
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
    ) {
        // Clean Top Segmented Tab Row
        Surface(
            color = DrgSurface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedSubTab,
                edgePadding = 12.dp,
                containerColor = DrgSurface,
                contentColor = DrgAmberSecondary,
                divider = {}
            ) {
                subTabs.forEachIndexed { index, (title, icon) ->
                    val isSelected = selectedSubTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedSubTab = index },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) DrgAmberSecondary else DrgTextMuted
                                )
                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) DrgAmberSecondary else DrgTextSecondary
                                )
                            }
                        }
                    )
                }
            }
        }

        // Sub-screen Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedSubTab) {
                0 -> {
                    val isTreasurerOrAdmin = currentMember != null && (
                        currentMember.role == MemberRole.BENDAHARA || 
                        currentMember.role == MemberRole.KETUA || 
                        currentMember.role == MemberRole.SEKRETARIS
                    )
                    TransparencyReportScreen(
                        transactions = transactions,
                        totalIncome = totalIncome,
                        totalExpense = totalExpense,
                        netBalance = netBalance,
                        canAddTransaction = isTreasurerOrAdmin,
                        onAddTransactionClick = onAddTransactionClick
                    )
                }
                1 -> ForumAndWorkshopScreen(
                    posts = posts,
                    workshops = workshops,
                    currentMemberId = currentMember?.id ?: "DRG-001",
                    onCreatePost = onCreatePost,
                    onToggleLike = onToggleLike,
                    onDeletePost = onDeletePost,
                    onCallWorkshop = onCallWorkshop
                )
                2 -> GamificationLeaderboardScreen(
                    currentMember = currentMember,
                    members = members,
                    badges = badges,
                    tasks = tasks,
                    rewards = rewards,
                    pointTransactions = pointTransactions,
                    onAwardPointsClick = onAwardPointsClick,
                    onClaimTask = onClaimTask,
                    onCompleteTask = onCompleteTask,
                    onRedeemReward = onRedeemReward
                )
                3 -> MembersAndPoskoScreen(
                    currentMember = currentMember,
                    members = members,
                    poskoList = poskoList,
                    adminLogs = adminLogs,
                    onRecordKopdarAttendance = onRecordKopdarAttendance,
                    onAddReview = onAddReview,
                    onUpdateMemberRole = onUpdateMemberRole,
                    onUpdateMemberVerification = onUpdateMemberVerification
                )
                4 -> DriverServicesScreen(
                    onOrderMarketItem = onOrderMarketItem,
                    onClaimInsurance = onClaimInsurance
                )
            }
        }
    }
}
