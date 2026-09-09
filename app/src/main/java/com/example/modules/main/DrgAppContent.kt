package com.example.modules.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.core.sync.SyncStatus
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.*
import com.example.core.viewmodel.MainNavTab
import com.example.modules.auth.AuthContainerScreen
import com.example.modules.main.primitives.DrgAppDialogs
import com.example.modules.main.primitives.DrgTabContentSwitcher
import com.example.shared.atoms.DrgHeader
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgBackgroundGradient
import com.example.ui.theme.DrgGrabGreenPrimary

@Composable
fun DrgAppContent(viewModel: DRGViewModel, cashViewModel: CashManagementViewModel) {
    val state = rememberDrgAppState(viewModel = viewModel, cashViewModel = cashViewModel)
    val context = LocalContext.current
    var showRoleSwitcher by remember { mutableStateOf(false) }
    var showEmergencyTrigger by remember { mutableStateOf(false) }
    var showAddKasDialog by remember { mutableStateOf(false) }
    var showAwardPointDialog by remember { mutableStateOf(false) }
    var showAddHazardDialog by remember { mutableStateOf(false) }
    var showNotifPrefDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var selectedTargetForAward by remember { mutableStateOf<DriverMember?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Android Back Button Handler
    BackHandler {
        when {
            showRoleSwitcher -> showRoleSwitcher = false
            showEmergencyTrigger -> showEmergencyTrigger = false
            showAddKasDialog -> showAddKasDialog = false
            showAwardPointDialog -> showAwardPointDialog = false
            showAddHazardDialog -> showAddHazardDialog = false
            showNotifPrefDialog -> showNotifPrefDialog = false
            showSettingsDialog -> showSettingsDialog = false
            state.currentTab.value != MainNavTab.DASHBOARD -> viewModel.selectTab(MainNavTab.DASHBOARD)
            else -> (context as? android.app.Activity)?.finish()
        }
    }

    if (!state.isLoggedIn.value) {
        AuthContainerScreen(viewModel = viewModel, onLoginSuccess = { viewModel.login(it) })
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize().imePadding(),
            contentWindowInsets = WindowInsets.systemBars,
            topBar = {
                Column {
                    DrgHeader(
                        currentMember = state.currentMember.value,
                        activeEmergencyCount = state.activeAlerts.value.size,
                        onRoleSwitchClick = { showRoleSwitcher = true },
                        onEmergencyBadgeClick = { viewModel.selectTab(MainNavTab.EMERGENCY) },
                        onSettingsClick = { showSettingsDialog = true }
                    )
                    if (state.syncStatus.value == SyncStatus.SYNCING) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().height(3.dp),
                            color = DrgGrabGreenPrimary
                        )
                    }
                }
            },
            bottomBar = {
                DrgBottomNavBar(selectedTab = state.currentTab.value, onSelectTab = { viewModel.selectTab(it) }, activeEmergencyCount = state.activeAlerts.value.size)
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().background(DrgBackgroundGradient).padding(paddingValues)) {
                DrgTabContentSwitcher(
                    currentTab = state.currentTab.value, viewModel = viewModel, cashViewModel = cashViewModel,
                    currentMember = state.currentMember.value, members = state.members.value, activeAlerts = state.activeAlerts.value,
                    cashTransactions = state.cashTransactions.value, totalIncome = state.totalIncome.value, totalExpense = state.totalExpense.value,
                    netBalance = state.netBalance.value, posts = state.posts.value, workshops = state.workshops.value, poskoList = state.poskoList.value,
                    notifications = state.notifications.value, badges = state.badges.value, tasks = state.tasks.value, rewards = state.rewards.value,
                    hazards = state.hazards.value, pointTransactions = state.pointTransactions.value, adminLogs = state.adminLogs.value,
                    isSosAlarmEnabled = state.isSosAlarmEnabled.value, isCrashGuardEnabled = state.isCrashGuardEnabled.value,
                    crashSensitivity = state.crashSensitivity.value, isPowerSaverMode = state.isPowerSaverMode.value,
                    snackbarHostState = snackbarHostState,
                    onShowEmergencyTrigger = { showEmergencyTrigger = true }, onShowAddKasDialog = { showAddKasDialog = true },
                    onShowAddHazardDialog = { showAddHazardDialog = true }, onShowSettingsDialog = { showSettingsDialog = true },
                    onShowAwardPointDialog = { target -> selectedTargetForAward = target; showAwardPointDialog = true }
                )
            }
        }

        DrgAppDialogs(
            viewModel = viewModel, cashViewModel = cashViewModel, context = context,
            members = state.members.value, currentMember = state.currentMember.value,
            showRoleSwitcher = showRoleSwitcher, onDismissRoleSwitcher = { showRoleSwitcher = false },
            showEmergencyTrigger = showEmergencyTrigger, onDismissEmergencyTrigger = { showEmergencyTrigger = false },
            showAddKasDialog = showAddKasDialog, onDismissAddKasDialog = { showAddKasDialog = false },
            showAwardPointDialog = showAwardPointDialog, selectedTargetForAward = selectedTargetForAward,
            onDismissAwardPointDialog = { showAwardPointDialog = false },
            showAddHazardDialog = showAddHazardDialog, onDismissAddHazardDialog = { showAddHazardDialog = false },
            showNotifPrefDialog = showNotifPrefDialog, onDismissNotifPrefDialog = { showNotifPrefDialog = false },
            showSettingsDialog = showSettingsDialog, onDismissSettingsDialog = { showSettingsDialog = false },
            isSosAlarmEnabled = state.isSosAlarmEnabled.value, isCrashGuardEnabled = state.isCrashGuardEnabled.value,
            crashSensitivity = state.crashSensitivity.value, mapCachePolicy = state.mapCachePolicy.value,
            locationSyncProfile = state.locationSyncProfile.value, isDataSaverMode = state.isDataSaverMode.value,
            isPowerSaverMode = state.isPowerSaverMode.value, crashDetectedEvent = state.crashDetectedEvent.value,
            snackbarHostState = snackbarHostState, scope = scope
        )
    }
}
