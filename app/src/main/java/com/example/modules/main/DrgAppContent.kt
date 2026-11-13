package com.example.modules.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.core.sync.SyncStatus
import com.example.core.viewmodel.*
import com.example.modules.auth.AuthContainerScreen
import com.example.modules.main.primitives.*
import com.example.shared.atoms.DrgHeader
import com.example.shared.atoms.DrgLuminousAtmosphere
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.utils.AppThemePreferences
import com.example.shared.utils.rememberDoubleBackExitHandler

@Composable
fun DrgAppContent(viewModel: DRGViewModel, cashViewModel: CashManagementViewModel) {
    val state = rememberDrgAppState(viewModel = viewModel, cashViewModel = cashViewModel)
    val context = LocalContext.current
    var dashboardTheme by remember { mutableStateOf(AppThemePreferences.loadVisualTheme(context)) }
    var dashboardFocusMode by remember { mutableStateOf(AppThemePreferences.loadFocusMode(context)) }
    var showEmergencyTrigger by remember { mutableStateOf(false) }
    var showAddKasDialog by remember { mutableStateOf(false) }
    var showAwardPointDialog by remember { mutableStateOf(false) }
    var showAddHazardDialog by remember { mutableStateOf(false) }
    var showNotifPrefDialog by remember { mutableStateOf(false) }
    var selectedTargetForAward by remember { mutableStateOf<DriverMember?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val doubleBackHandler = rememberDoubleBackExitHandler()

    val forumCount = remember(state.posts.value) { computeForumActivityCount(state.posts.value) }
    val openTasksCount = remember(state.tasks.value) { computeOpenTasksCount(state.tasks.value) }
    val eventsCount = remember(state.communityEvents.value, state.eventFeedback.value) {
        computeEventActivityCount(state.communityEvents.value, state.eventFeedback.value)
    }
    val liveCounts = rememberCommunityLiveCounts(
        state.cashTransactions.value.size, forumCount, openTasksCount, state.members.value.size, eventsCount
    )

    BackHandler(enabled = state.isLoggedIn.value) {
        when {
            showEmergencyTrigger -> showEmergencyTrigger = false
            showAddKasDialog -> showAddKasDialog = false
            showAwardPointDialog -> showAwardPointDialog = false
            showAddHazardDialog -> showAddHazardDialog = false
            showNotifPrefDialog -> showNotifPrefDialog = false
            state.currentTab.value == MainNavTab.ABOUT -> viewModel.selectTab(MainNavTab.SETTINGS)
            state.currentTab.value == MainNavTab.SETTINGS -> viewModel.selectTab(MainNavTab.PROFILE)
            state.currentTab.value == MainNavTab.DEV_TOOLS -> viewModel.selectTab(MainNavTab.PROFILE)
            state.currentTab.value != MainNavTab.DASHBOARD -> { doubleBackHandler.reset(); viewModel.selectTab(MainNavTab.DASHBOARD) }
            else -> doubleBackHandler.handleBackPress()
        }
    }

    CompositionLocalProvider(LocalAppVisualTheme provides dashboardTheme) {
        if (!state.isLoggedIn.value) {
            AuthContainerScreen(viewModel = viewModel, onLoginSuccess = { viewModel.login(it) })
        } else {
            val headerLessTabs = remember { setOf(MainNavTab.DASHBOARD, MainNavTab.PROFILE, MainNavTab.SETTINGS, MainNavTab.EVENTS, MainNavTab.DEV_TOOLS, MainNavTab.EMERGENCY, MainNavTab.FIELD_WORKFLOW, MainNavTab.ABOUT, MainNavTab.ROLES) }
            Box(modifier = Modifier.fillMaxSize()) {
                DrgLuminousAtmosphere(theme = dashboardTheme)
                Scaffold(
                    modifier = Modifier.fillMaxSize().imePadding(), containerColor = Color.Transparent, contentWindowInsets = WindowInsets.systemBars,
                    topBar = {
                        if (state.currentTab.value !in headerLessTabs) {
                            Column {
                                DrgHeader(
                                    currentMember = state.currentMember.value,
                                    activeEmergencyCount = state.activeAlerts.value.size,
                                    onEmergencyBadgeClick = { viewModel.selectTab(MainNavTab.EMERGENCY) }
                                )
                                if (state.syncStatus.value == SyncStatus.SYNCING) LinearProgressIndicator(Modifier.fillMaxWidth().height(3.dp), color = dashboardTheme.primaryColor)
                            }
                        }
                    },
                    bottomBar = {
                        DrgBottomNavBar(
                            selectedTab = state.currentTab.value, onSelectTab = { viewModel.selectTab(it) },
                            activeEmergencyCount = state.activeAlerts.value.size, accentColor = dashboardTheme.primaryColor,
                            pillBgColor = dashboardTheme.pillBg, glassBgColor = dashboardTheme.glassBg,
                            communityUpdateCount = liveCounts.totalUnreadCommunity, onEmergencyClick = { viewModel.selectTab(MainNavTab.EMERGENCY) }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->
                    DrgTabContentSwitcher(
                        state.currentTab.value, viewModel, cashViewModel, state.currentMember.value, state.members.value,
                        state.activeAlerts.value, state.cashTransactions.value, state.totalIncome.value, state.totalExpense.value,
                        state.netBalance.value, state.posts.value, state.workshops.value, state.poskoList.value,
                        state.notifications.value, state.badges.value, state.tasks.value, state.rewards.value,
                        state.hazards.value, state.pointTransactions.value, state.adminLogs.value,
                        state.isSosAlarmEnabled.value, state.isCrashGuardEnabled.value, state.crashSensitivity.value,
                        state.isPowerSaverMode.value, state.isDataSaverMode.value, state.mapCachePolicy.value,
                        state.locationSyncProfile.value, snackbarHostState,
                        { showEmergencyTrigger = true }, { showAddKasDialog = true }, { showAddHazardDialog = true },
                        { viewModel.selectTab(MainNavTab.SETTINGS) }, { target -> selectedTargetForAward = target; showAwardPointDialog = true },
                        dashboardTheme, { dashboardTheme = it; AppThemePreferences.saveVisualTheme(context, it) },
                        dashboardFocusMode, { dashboardFocusMode = it; AppThemePreferences.saveFocusMode(context, it) },
                        paddingValues
                    )
                }
            }
            DrgAppDialogs(
                viewModel, cashViewModel, context, state.members.value, state.currentMember.value,
                showEmergencyTrigger, { showEmergencyTrigger = false }, showAddKasDialog, { showAddKasDialog = false },
                showAwardPointDialog, selectedTargetForAward, { showAwardPointDialog = false },
                showAddHazardDialog, { showAddHazardDialog = false }, showNotifPrefDialog, { showNotifPrefDialog = false },
                false, {}, state.isSosAlarmEnabled.value, state.isCrashGuardEnabled.value, state.crashSensitivity.value,
                state.mapCachePolicy.value, state.locationSyncProfile.value, state.isDataSaverMode.value,
                state.isPowerSaverMode.value, state.crashDetectedEvent.value, snackbarHostState, scope
            ) { viewModel.selectTab(MainNavTab.SETTINGS) }
        }
    }
}
