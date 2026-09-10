package com.example.modules.main.primitives

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.*
import com.example.core.viewmodel.MainNavTab
import com.example.modules.community.CommunityHubScreen
import com.example.shared.models.*
import com.example.shared.utils.WhatsAppLauncher
import kotlinx.coroutines.launch

@Composable
fun CommunityTabDelegate(
    tab: MainNavTab,
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    members: List<DriverMember>,
    cashTransactions: List<KasTransaction>,
    totalIncome: Long,
    totalExpense: Long,
    netBalance: Long,
    posts: List<ForumPost>,
    workshops: List<WorkshopPartner>,
    badges: List<BadgeItem>,
    tasks: List<CommunityTask>,
    rewards: List<RewardItem>,
    poskoList: List<PoskoLocation>,
    pointTransactions: List<PointTransaction>,
    adminLogs: List<AdminLog>,
    snackbarHostState: SnackbarHostState,
    onShowAddKasDialog: () -> Unit,
    onShowAwardPointDialog: (DriverMember) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val subTabIndex = when (tab) {
        MainNavTab.FORUM -> 1
        MainNavTab.GAMIFICATION -> 2
        MainNavTab.MEMBERS -> 3
        else -> 0
    }

    CommunityHubScreen(
        currentMember = currentMember,
        members = members,
        transactions = cashTransactions,
        totalIncome = totalIncome,
        totalExpense = totalExpense,
        netBalance = netBalance,
        posts = posts,
        workshops = workshops,
        badges = badges,
        tasks = tasks,
        rewards = rewards,
        poskoList = poskoList,
        pointTransactions = pointTransactions,
        adminLogs = adminLogs,
        initialSubTab = subTabIndex,
        onAddTransactionClick = onShowAddKasDialog,
        onCreatePost = { title, content, cat, postType ->
            viewModel.addPost(title, content, cat, postType)
        },
        onToggleLike = { id, liked -> viewModel.toggleLikePost(id, liked) },
        onDeletePost = { id -> viewModel.deletePost(id) },
        onAddComment = { postId, text -> viewModel.addForumComment(postId, text) },
        onDeleteComment = { commentId -> viewModel.deleteForumComment(commentId) },
        onCallWorkshop = { phone ->
            WhatsAppLauncher.openChat(context, phone, "Halo Bengkel Rekanan DRG, saya anggota DRG ingin menanyakan perihal servis/booking.")
        },
        onAwardPointsClick = {
            val other = members.find { it.id != currentMember?.id } ?: members.firstOrNull()
            other?.let { onShowAwardPointDialog(it) }
        },
        onClaimTask = { viewModel.claimTask(it) },
        onCompleteTask = { viewModel.completeTask(it) },
        onRedeemReward = { viewModel.redeemReward(it) },
        onRecordKopdarAttendance = {
            viewModel.recordKopdarAttendance()
            scope.launch { snackbarHostState.showSnackbar("Absen Kopdar Berhasil! +50 XP Loyalitas DRG") }
        },
        onAddReview = { driverId, rating, comment ->
            viewModel.addDriverReview(driverId, rating, comment)
            scope.launch { snackbarHostState.showSnackbar("Review driver berhasil dikirim!") }
        },
        onUpdateMemberRole = { id, role -> viewModel.updateMemberRole(id, role) },
        onUpdateMemberVerification = { id, status -> viewModel.updateMemberVerification(id, status) },
        onUpdateMemberPermissions = { id, role, canKas, canVerify, canSos, canPosko, status ->
            viewModel.updateMemberPermissions(id, role, canKas, canVerify, canSos, canPosko, status)
        },
        onOrderMarketItem = { item ->
            scope.launch { snackbarHostState.showSnackbar("Pesanan ${item.title} berhasil diajukan! Silakan ambil di ${item.poskoLocation}") }
        },
        onClaimInsurance = { claim ->
            scope.launch { snackbarHostState.showSnackbar("Pengajuan ${claim.title} diterima & diproses tim pengurus!") }
        }
    )
}
