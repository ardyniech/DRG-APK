package com.example.modules.main.primitives

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.viewmodel.*
import com.example.modules.about.AboutScreen
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.modules.devtools.DevToolsDashboardScreen
import com.example.modules.emergency.EmergencyScreen
import com.example.modules.radar.RadarScreen
import com.example.modules.workflow.FieldWorkflowScreen
import com.example.shared.models.*
import com.example.shared.utils.WhatsAppLauncher
import kotlinx.coroutines.launch

@Composable
fun DrgTabContentSwitcher(
    currentTab: MainNavTab,
    viewModel: DRGViewModel,
    cashViewModel: CashManagementViewModel,
    currentMember: DriverMember?,
    members: List<DriverMember>,
    activeAlerts: List<EmergencyAlert>,
    cashTransactions: List<KasTransaction>,
    totalIncome: Long, totalExpense: Long, netBalance: Long,
    posts: List<ForumPost>, workshops: List<WorkshopPartner>, poskoList: List<PoskoLocation>,
    notifications: List<CommunityNotification>, badges: List<BadgeItem>, tasks: List<CommunityTask>,
    rewards: List<RewardItem>, hazards: List<HazardArea>, pointTransactions: List<PointTransaction>, adminLogs: List<AdminLog>,
    isSosAlarmEnabled: Boolean, isCrashGuardEnabled: Boolean, crashSensitivity: CrashSensitivity,
    isPowerSaverMode: Boolean, isDataSaverMode: Boolean, mapCachePolicy: MapCachePolicy, locationSyncProfile: LocationSyncPowerProfile,
    snackbarHostState: SnackbarHostState,
    onShowEmergencyTrigger: () -> Unit, onShowAddKasDialog: () -> Unit,
    onShowAddHazardDialog: () -> Unit, onShowSettingsDialog: () -> Unit,
    onShowAwardPointDialog: (DriverMember) -> Unit,
    dashboardTheme: DashboardVisualTheme = DashboardVisualTheme.EMERALD_AURORA,
    onSelectDashboardTheme: (DashboardVisualTheme) -> Unit = {},
    dashboardFocusMode: DashboardFocusMode = DashboardFocusMode.SATGAS_TRC,
    onSelectDashboardFocusMode: (DashboardFocusMode) -> Unit = {},
    scaffoldPadding: PaddingValues = PaddingValues(0.dp)
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fullBleedTabs = remember {
        setOf(
            MainNavTab.DASHBOARD, MainNavTab.PROFILE, MainNavTab.SETTINGS, MainNavTab.EVENTS,
            MainNavTab.DEV_TOOLS, MainNavTab.EMERGENCY, MainNavTab.FIELD_WORKFLOW, MainNavTab.ABOUT, MainNavTab.ROLES
        )
    }
    val events = viewModel.communityEvents.collectAsStateWithLifecycle().value
    val feedback = viewModel.eventFeedback.collectAsStateWithLifecycle().value
    val eventUpdatesCount = computeEventActivityCount(events, feedback)
    val forumActivityCount = computeForumActivityCount(posts)
    val openTasksCount = computeOpenTasksCount(tasks)

    Crossfade(targetState = currentTab, animationSpec = tween(durationMillis = 200), label = "tabTransition") { tab ->
        Box(modifier = if (tab in fullBleedTabs) Modifier else Modifier.padding(scaffoldPadding)) {
            when (tab) {
                MainNavTab.DASHBOARD -> DashboardTabDelegate(
                    viewModel, currentMember, members, activeAlerts, hazards, notifications, cashTransactions,
                    poskoList.size, netBalance, onShowEmergencyTrigger, dashboardTheme, onSelectDashboardTheme,
                    dashboardFocusMode, forumActivityCount, openTasksCount, eventUpdatesCount, scaffoldPadding
                )
                MainNavTab.RADAR -> RadarScreen(
                    members, activeAlerts, poskoList, hazards, currentMember,
                    { viewModel.toggleLocationSharingConsent(it) }, onShowAddHazardDialog, { viewModel.confirmHazard(it) },
                    onShowEmergencyTrigger, { WhatsAppLauncher.openChat(context, it, "Halo rekan DRG, koordinasi tim satgas segera.") },
                    isPowerSaverMode
                )
                MainNavTab.EMERGENCY -> EmergencyScreen(
                    currentMember, activeAlerts,
                    { t, m, l -> viewModel.triggerEmergency(t, m, l); scope.launch { snackbarHostState.showSnackbar("Sinyal SOS Dipancarkan!") } },
                    { viewModel.resolveEmergency(it) }, { viewModel.respondToEmergency(it) },
                    { WhatsAppLauncher.openChat(context, it, "Halo rekan DRG, kami pantau sinyal darurat SOS Anda.") },
                    isSosAlarmEnabled, { viewModel.toggleSosAlarmSound(it) },
                    isCrashGuardEnabled, crashSensitivity, { viewModel.simulateCrashImpact(3.2f) }, onShowSettingsDialog,
                    { viewModel.selectTab(MainNavTab.DASHBOARD) }, Modifier.padding(scaffoldPadding)
                )
                MainNavTab.COMMUNITY, MainNavTab.KAS, MainNavTab.FORUM, MainNavTab.GAMIFICATION, MainNavTab.MEMBERS -> CommunityTabDelegate(
                    tab, viewModel, currentMember, members, cashTransactions, totalIncome, totalExpense, netBalance,
                    posts, workshops, badges, tasks, rewards, poskoList, pointTransactions, adminLogs,
                    snackbarHostState, onShowAddKasDialog, onShowAwardPointDialog, eventUpdatesCount
                )
                MainNavTab.PROFILE -> ProfileTabDelegate(
                    viewModel, currentMember, badges, isSosAlarmEnabled, dashboardTheme, snackbarHostState, scaffoldPadding
                )
                MainNavTab.SETTINGS -> SettingsTabDelegate(
                    viewModel, currentMember, dashboardTheme, onSelectDashboardTheme, dashboardFocusMode, onSelectDashboardFocusMode,
                    isSosAlarmEnabled, isCrashGuardEnabled, crashSensitivity, mapCachePolicy, locationSyncProfile,
                    isDataSaverMode, isPowerSaverMode, snackbarHostState, scaffoldPadding
                )
                MainNavTab.ABOUT -> AboutScreen(onBack = { viewModel.selectTab(MainNavTab.SETTINGS) }, contentPadding = scaffoldPadding)
                MainNavTab.FIELD_WORKFLOW -> FieldWorkflowScreen({ viewModel.selectTab(MainNavTab.DASHBOARD) }, { viewModel.selectTab(MainNavTab.RADAR) }, onShowEmergencyTrigger, Modifier.padding(scaffoldPadding))
                MainNavTab.EVENTS -> EventsTabDelegate(viewModel, currentMember, dashboardTheme, scaffoldPadding)
                MainNavTab.DEV_TOOLS -> DevToolsDashboardScreen(currentMember = currentMember, visualTheme = dashboardTheme, contentPadding = scaffoldPadding, onBack = { viewModel.selectTab(MainNavTab.PROFILE) })
                MainNavTab.ADMIN, MainNavTab.ROLES -> AdminTabDelegate(tab, viewModel, currentMember, members, snackbarHostState, scaffoldPadding)
            }
        }
    }
}
