package com.example.modules.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.MainNavTab
import com.example.modules.auth.AuthContainerScreen
import com.example.modules.emergency.SosFloatingActionButton
import com.example.modules.main.primitives.DrgAppDialogs
import com.example.modules.main.primitives.DrgTabContentSwitcher
import com.example.shared.atoms.DrgHeader
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgBackgroundGradient

@Composable
fun DrgAppContent(
    viewModel: DRGViewModel,
    cashViewModel: CashManagementViewModel
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val currentMember by viewModel.currentMember.collectAsState()
    val members by viewModel.allMembers.collectAsState()
    val activeAlerts by viewModel.activeAlerts.collectAsState()
    val cashTransactions by cashViewModel.transactions.collectAsState()
    val totalIncome by cashViewModel.totalIncome.collectAsState()
    val totalExpense by cashViewModel.totalExpense.collectAsState()
    val netBalance by cashViewModel.netBalance.collectAsState()
    val posts by viewModel.posts.collectAsState()
    val workshops by viewModel.workshops.collectAsState()
    val poskoList by viewModel.poskoList.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val rewards by viewModel.rewards.collectAsState()
    val hazards by viewModel.hazards.collectAsState()
    val pointTransactions by viewModel.pointTransactions.collectAsState()
    val adminLogs by viewModel.adminLogs.collectAsState()
    val isSosAlarmEnabled by viewModel.isSosAlarmSoundEnabled.collectAsState()
    val isCrashGuardEnabled by viewModel.isCrashGuardEnabled.collectAsState()
    val crashSensitivity by viewModel.crashSensitivity.collectAsState()
    val crashDetectedEvent by viewModel.crashDetectedEvent.collectAsState()
    val isPowerSaverMode by viewModel.isPowerSaverMode.collectAsState()
    val isDataSaverMode by viewModel.isDataSaverMode.collectAsState()
    val mapCachePolicy by viewModel.mapCachePolicy.collectAsState()
    val locationSyncProfile by viewModel.locationSyncProfile.collectAsState()
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

    if (!isLoggedIn) {
        AuthContainerScreen(viewModel = viewModel, onLoginSuccess = { viewModel.login(it) })
    } else {
        Scaffold(
            topBar = {
                DrgHeader(
                    currentMember = currentMember,
                    activeEmergencyCount = activeAlerts.size,
                    onRoleSwitchClick = { showRoleSwitcher = true },
                    onEmergencyBadgeClick = { viewModel.selectTab(MainNavTab.EMERGENCY) },
                    onSettingsClick = { showSettingsDialog = true }
                )
            },
            bottomBar = {
                DrgBottomNavBar(
                    selectedTab = currentTab,
                    onSelectTab = { viewModel.selectTab(it) },
                    activeEmergencyCount = activeAlerts.size
                )
            },
            floatingActionButton = {
                if (currentTab != MainNavTab.EMERGENCY) {
                    SosFloatingActionButton(onClick = { showEmergencyTrigger = true })
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DrgBackgroundGradient)
                    .padding(paddingValues)
            ) {
                DrgTabContentSwitcher(
                    currentTab = currentTab, viewModel = viewModel, cashViewModel = cashViewModel,
                    currentMember = currentMember, members = members, activeAlerts = activeAlerts,
                    cashTransactions = cashTransactions, totalIncome = totalIncome, totalExpense = totalExpense,
                    netBalance = netBalance, posts = posts, workshops = workshops, poskoList = poskoList,
                    notifications = notifications, badges = badges, tasks = tasks, rewards = rewards,
                    hazards = hazards, pointTransactions = pointTransactions, adminLogs = adminLogs,
                    isSosAlarmEnabled = isSosAlarmEnabled, isCrashGuardEnabled = isCrashGuardEnabled,
                    crashSensitivity = crashSensitivity, isPowerSaverMode = isPowerSaverMode,
                    snackbarHostState = snackbarHostState,
                    onShowEmergencyTrigger = { showEmergencyTrigger = true },
                    onShowAddKasDialog = { showAddKasDialog = true },
                    onShowAddHazardDialog = { showAddHazardDialog = true },
                    onShowSettingsDialog = { showSettingsDialog = true },
                    onShowAwardPointDialog = { target ->
                        selectedTargetForAward = target
                        showAwardPointDialog = true
                    }
                )
            }
        }

        DrgAppDialogs(
            viewModel = viewModel, cashViewModel = cashViewModel, context = context,
            members = members, currentMember = currentMember,
            showRoleSwitcher = showRoleSwitcher, onDismissRoleSwitcher = { showRoleSwitcher = false },
            showEmergencyTrigger = showEmergencyTrigger, onDismissEmergencyTrigger = { showEmergencyTrigger = false },
            showAddKasDialog = showAddKasDialog, onDismissAddKasDialog = { showAddKasDialog = false },
            showAwardPointDialog = showAwardPointDialog, selectedTargetForAward = selectedTargetForAward,
            onDismissAwardPointDialog = { showAwardPointDialog = false },
            showAddHazardDialog = showAddHazardDialog, onDismissAddHazardDialog = { showAddHazardDialog = false },
            showNotifPrefDialog = showNotifPrefDialog, onDismissNotifPrefDialog = { showNotifPrefDialog = false },
            showSettingsDialog = showSettingsDialog, onDismissSettingsDialog = { showSettingsDialog = false },
            isSosAlarmEnabled = isSosAlarmEnabled, isCrashGuardEnabled = isCrashGuardEnabled,
            crashSensitivity = crashSensitivity, mapCachePolicy = mapCachePolicy,
            locationSyncProfile = locationSyncProfile, isDataSaverMode = isDataSaverMode,
            isPowerSaverMode = isPowerSaverMode, crashDetectedEvent = crashDetectedEvent,
            snackbarHostState = snackbarHostState, scope = scope
        )
    }
}
