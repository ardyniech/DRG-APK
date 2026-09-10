package com.example.modules.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.modules.forum_workshop.ForumAndWorkshopScreen
import com.example.modules.gamification.GamificationLeaderboardScreen
import com.example.modules.members.MembersAndPoskoScreen
import com.example.modules.treasury.TransparencyReportScreen
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
    commentsMap: Map<String, List<ForumComment>> = emptyMap(),
    onAddTransactionClick: () -> Unit = {},
    onCreatePost: (String, String, ForumCategory, PostType) -> Unit = { _, _, _, _ -> },
    onToggleLike: (String, Boolean) -> Unit = { _, _ -> },
    onDeletePost: (String) -> Unit = {},
    onAddComment: (String, String) -> Unit = { _, _ -> },
    onDeleteComment: (String) -> Unit = {},
    onCallWorkshop: (String) -> Unit = {},
    onAwardPointsClick: () -> Unit = {},
    onClaimTask: (String) -> Unit = {},
    onCompleteTask: (String) -> Unit = {},
    onRedeemReward: (String) -> Unit = {},
    onRecordKopdarAttendance: () -> Unit = {},
    onAddReview: (String, Int, String) -> Unit = { _, _, _ -> },
    onUpdateMemberRole: (String, MemberRole) -> Unit = { _, _ -> },
    onUpdateMemberVerification: (String, VerificationStatus) -> Unit = { _, _ -> },
    onUpdateMemberPermissions: (String, MemberRole, Boolean, Boolean, Boolean, Boolean, VerificationStatus) -> Unit = { _, _, _, _, _, _, _ -> },
    onOrderMarketItem: (ServiceMarketItem) -> Unit = {},
    onClaimInsurance: (InsuranceClaimItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(initialSubTab) }

    Column(modifier = modifier.fillMaxSize().background(DrgBackground)) {
        CommunitySubTabRow(selectedSubTab = selectedSubTab, onSelectSubTab = { selectedSubTab = it })

        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedSubTab) {
                0 -> {
                    val isTreasurerOrAdmin = currentMember != null && (
                        currentMember.role == MemberRole.BENDAHARA || currentMember.role == MemberRole.KETUA || currentMember.role == MemberRole.SEKRETARIS
                    )
                    TransparencyReportScreen(transactions = transactions, totalIncome = totalIncome, totalExpense = totalExpense, netBalance = netBalance, canAddTransaction = isTreasurerOrAdmin, onAddTransactionClick = onAddTransactionClick)
                }
                1 -> ForumAndWorkshopScreen(
                    posts = posts, workshops = workshops, currentMemberId = currentMember?.id ?: "DRG-001",
                    onCreatePost = onCreatePost, onToggleLike = onToggleLike, onDeletePost = onDeletePost,
                    commentsMap = commentsMap, onAddComment = onAddComment, onDeleteComment = onDeleteComment,
                    onCallWorkshop = onCallWorkshop
                )
                2 -> GamificationLeaderboardScreen(currentMember = currentMember, members = members, badges = badges, tasks = tasks, rewards = rewards, pointTransactions = pointTransactions, onAwardPointsClick = onAwardPointsClick, onClaimTask = onClaimTask, onCompleteTask = onCompleteTask, onRedeemReward = onRedeemReward)
                3 -> MembersAndPoskoScreen(currentMember = currentMember, members = members, poskoList = poskoList, adminLogs = adminLogs, onRecordKopdarAttendance = onRecordKopdarAttendance, onAddReview = onAddReview, onUpdateMemberRole = onUpdateMemberRole, onUpdateMemberVerification = onUpdateMemberVerification, onUpdateMemberPermissions = onUpdateMemberPermissions)
                4 -> DriverServicesScreen(onOrderMarketItem = onOrderMarketItem, onClaimInsurance = onClaimInsurance)
            }
        }
    }
}
