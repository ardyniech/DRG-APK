package com.example.modules.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.*

class DrgAppState(
    val isLoggedIn: State<Boolean>,
    val currentTab: State<MainNavTab>,
    val currentMember: State<DriverMember?>,
    val members: State<List<DriverMember>>,
    val activeAlerts: State<List<EmergencyAlert>>,
    val cashTransactions: State<List<KasTransaction>>,
    val totalIncome: State<Long>,
    val totalExpense: State<Long>,
    val netBalance: State<Long>,
    val posts: State<List<ForumPost>>,
    val workshops: State<List<WorkshopPartner>>,
    val poskoList: State<List<PoskoLocation>>,
    val notifications: State<List<CommunityNotification>>,
    val badges: State<List<BadgeItem>>,
    val tasks: State<List<CommunityTask>>,
    val rewards: State<List<RewardItem>>,
    val hazards: State<List<HazardArea>>,
    val pointTransactions: State<List<PointTransaction>>,
    val adminLogs: State<List<AdminLog>>,
    val isSosAlarmEnabled: State<Boolean>,
    val isCrashGuardEnabled: State<Boolean>,
    val crashSensitivity: State<CrashSensitivity>,
    val crashDetectedEvent: State<Float?>,
    val isPowerSaverMode: State<Boolean>,
    val isDataSaverMode: State<Boolean>,
    val mapCachePolicy: State<MapCachePolicy>,
    val locationSyncProfile: State<LocationSyncPowerProfile>
)

@Composable
fun rememberDrgAppState(viewModel: DRGViewModel, cashViewModel: CashManagementViewModel): DrgAppState {
    return DrgAppState(
        isLoggedIn = viewModel.isLoggedIn.collectAsState(),
        currentTab = viewModel.currentTab.collectAsState(),
        currentMember = viewModel.currentMember.collectAsState(),
        members = viewModel.allMembers.collectAsState(),
        activeAlerts = viewModel.activeAlerts.collectAsState(),
        cashTransactions = cashViewModel.transactions.collectAsState(),
        totalIncome = cashViewModel.totalIncome.collectAsState(),
        totalExpense = cashViewModel.totalExpense.collectAsState(),
        netBalance = cashViewModel.netBalance.collectAsState(),
        posts = viewModel.posts.collectAsState(),
        workshops = viewModel.workshops.collectAsState(),
        poskoList = viewModel.poskoList.collectAsState(),
        notifications = viewModel.notifications.collectAsState(),
        badges = viewModel.badges.collectAsState(),
        tasks = viewModel.tasks.collectAsState(),
        rewards = viewModel.rewards.collectAsState(),
        hazards = viewModel.hazards.collectAsState(),
        pointTransactions = viewModel.pointTransactions.collectAsState(),
        adminLogs = viewModel.adminLogs.collectAsState(),
        isSosAlarmEnabled = viewModel.isSosAlarmSoundEnabled.collectAsState(),
        isCrashGuardEnabled = viewModel.isCrashGuardEnabled.collectAsState(),
        crashSensitivity = viewModel.crashSensitivity.collectAsState(),
        crashDetectedEvent = viewModel.crashDetectedEvent.collectAsState(),
        isPowerSaverMode = viewModel.isPowerSaverMode.collectAsState(),
        isDataSaverMode = viewModel.isDataSaverMode.collectAsState(),
        mapCachePolicy = viewModel.mapCachePolicy.collectAsState(),
        locationSyncProfile = viewModel.locationSyncProfile.collectAsState()
    )
}
