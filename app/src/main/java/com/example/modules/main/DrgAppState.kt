package com.example.modules.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.sync.SyncStatus
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
    val locationSyncProfile: State<LocationSyncPowerProfile>,
    val syncStatus: State<SyncStatus>
)

@Composable
fun rememberDrgAppState(viewModel: DRGViewModel, cashViewModel: CashManagementViewModel): DrgAppState {
    return DrgAppState(
        isLoggedIn = viewModel.isLoggedIn.collectAsStateWithLifecycle(),
        currentTab = viewModel.currentTab.collectAsStateWithLifecycle(),
        currentMember = viewModel.currentMember.collectAsStateWithLifecycle(),
        members = viewModel.allMembers.collectAsStateWithLifecycle(),
        activeAlerts = viewModel.activeAlerts.collectAsStateWithLifecycle(),
        cashTransactions = cashViewModel.transactions.collectAsStateWithLifecycle(),
        totalIncome = cashViewModel.totalIncome.collectAsStateWithLifecycle(),
        totalExpense = cashViewModel.totalExpense.collectAsStateWithLifecycle(),
        netBalance = cashViewModel.netBalance.collectAsStateWithLifecycle(),
        posts = viewModel.posts.collectAsStateWithLifecycle(),
        workshops = viewModel.workshops.collectAsStateWithLifecycle(),
        poskoList = viewModel.poskoList.collectAsStateWithLifecycle(),
        notifications = viewModel.notifications.collectAsStateWithLifecycle(),
        badges = viewModel.badges.collectAsStateWithLifecycle(),
        tasks = viewModel.tasks.collectAsStateWithLifecycle(),
        rewards = viewModel.rewards.collectAsStateWithLifecycle(),
        hazards = viewModel.hazards.collectAsStateWithLifecycle(),
        pointTransactions = viewModel.pointTransactions.collectAsStateWithLifecycle(),
        adminLogs = viewModel.adminLogs.collectAsStateWithLifecycle(),
        isSosAlarmEnabled = viewModel.isSosAlarmEnabled.collectAsStateWithLifecycle(),
        isCrashGuardEnabled = viewModel.isCrashGuardEnabled.collectAsStateWithLifecycle(),
        crashSensitivity = viewModel.crashSensitivity.collectAsStateWithLifecycle(),
        crashDetectedEvent = viewModel.crashDetectedEvent.collectAsStateWithLifecycle(),
        isPowerSaverMode = viewModel.isPowerSaverMode.collectAsStateWithLifecycle(),
        isDataSaverMode = viewModel.isDataSaverMode.collectAsStateWithLifecycle(),
        mapCachePolicy = viewModel.mapCachePolicy.collectAsStateWithLifecycle(),
        locationSyncProfile = viewModel.locationSyncProfile.collectAsStateWithLifecycle(),
        syncStatus = viewModel.syncEngine.syncStatus.collectAsStateWithLifecycle()
    )
}
