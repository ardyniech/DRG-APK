package com.example.modules.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.theme.DrgBackgroundGradient
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.MainNavTab
import com.example.modules.admin.AdminGovernanceScreen
import com.example.modules.community.CommunityHubScreen
import com.example.modules.dashboard.DashboardScreen
import com.example.modules.emergency.EmergencyCountdownDialog
import com.example.modules.emergency.EmergencyScreen
import com.example.modules.emergency.EmergencyTriggerDialog
import com.example.modules.forum_workshop.ForumAndWorkshopScreen
import com.example.modules.gamification.AwardPointDialog
import com.example.modules.gamification.GamificationLeaderboardScreen
import com.example.modules.members.MembersAndPoskoScreen
import com.example.modules.notifications.AppSettingsDialog
import com.example.modules.notifications.NotificationPreferencesDialog
import com.example.modules.profile.ProfileScreen
import com.example.modules.radar.AddHazardDialog
import com.example.modules.radar.RadarScreen
import com.example.modules.treasury.AddTransactionDialog
import com.example.modules.treasury.TreasuryScreen
import com.example.shared.atoms.DrgHeader
import com.example.shared.atoms.RoleSwitcherDialog
import com.example.shared.models.DriverMember
import com.example.shared.models.NotificationPreference
import com.example.modules.auth.AuthContainerScreen
import kotlinx.coroutines.launch

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
    val syncStatus by viewModel.syncStatus.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

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
        AuthContainerScreen(
            viewModel = viewModel,
            onLoginSuccess = { memberId ->
                viewModel.login(memberId)
            }
        )
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
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DrgBackgroundGradient)
                    .padding(paddingValues)
            ) {
                Crossfade(targetState = currentTab, label = "tabTransition") { tab ->
                when (tab) {
                    MainNavTab.DASHBOARD -> DashboardScreen(
                        currentMember = currentMember,
                        members = members,
                        activeAlerts = activeAlerts,
                        transactions = cashTransactions,
                        notifications = notifications,
                        onNavigateTab = { viewModel.selectTab(it) },
                        onTriggerEmergency = { showEmergencyTrigger = true },
                        onApproveSelf = {
                            currentMember?.id?.let { id ->
                                viewModel.approveMemberScreening(id, "Verifikasi mandiri via panel asisten penguji.")
                            }
                        }
                    )
                    MainNavTab.RADAR -> RadarScreen(
                        members = members,
                        alerts = activeAlerts,
                        poskoList = poskoList,
                        hazards = hazards,
                        currentMember = currentMember,
                        onToggleConsent = { viewModel.toggleLocationSharingConsent(it) },
                        onAddHazardClick = { showAddHazardDialog = true },
                        onConfirmHazard = { viewModel.confirmHazard(it) },
                        onTriggerEmergency = { showEmergencyTrigger = true },
                        onCallDriver = { phone ->
                            scope.launch { snackbarHostState.showSnackbar("Menghubungi $phone via telepon...") }
                        },
                        isPowerSaverEnabled = isPowerSaverMode
                    )
                    MainNavTab.EMERGENCY -> EmergencyScreen(
                        currentMember = currentMember,
                        alerts = activeAlerts,
                        onTriggerEmergency = { showEmergencyTrigger = true },
                        onResolveEmergency = { viewModel.resolveEmergency(it) },
                        onRespondEmergency = { viewModel.respondToEmergency(it) },
                        onCallDriver = { phone ->
                            scope.launch { snackbarHostState.showSnackbar("Menghubungi $phone...") }
                        },
                        isSosAlarmEnabled = isSosAlarmEnabled,
                        onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
                        isCrashGuardEnabled = isCrashGuardEnabled,
                        crashSensitivity = crashSensitivity,
                        onSimulateCrash = { viewModel.simulateCrashImpact(3.2f) },
                        onOpenSettings = { showSettingsDialog = true }
                    )
                    MainNavTab.COMMUNITY, MainNavTab.KAS, MainNavTab.FORUM, MainNavTab.GAMIFICATION, MainNavTab.MEMBERS -> {
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
                            onAddTransactionClick = { showAddKasDialog = true },
                            onCreatePost = { title, content, cat, postType ->
                                viewModel.addPost(title, content, cat, postType)
                            },
                            onToggleLike = { id, liked -> viewModel.toggleLikePost(id, liked) },
                            onDeletePost = { id -> viewModel.deletePost(id) },
                            onCallWorkshop = { phone ->
                                scope.launch { snackbarHostState.showSnackbar("Menghubungi Bengkel $phone...") }
                            },
                            onAwardPointsClick = {
                                val other = members.find { it.id != currentMember?.id } ?: members.firstOrNull()
                                selectedTargetForAward = other
                                showAwardPointDialog = true
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
                            onOrderMarketItem = { item ->
                                scope.launch { snackbarHostState.showSnackbar("Pesanan ${item.title} berhasil diajukan! Silakan ambil di ${item.poskoLocation}") }
                            },
                            onClaimInsurance = { claim ->
                                scope.launch { snackbarHostState.showSnackbar("Pengajuan ${claim.title} diterima & diproses tim pengurus!") }
                            }
                        )
                    }
                    MainNavTab.PROFILE -> ProfileScreen(
                        currentMember = currentMember,
                        onUpdateProfile = { phone, area, model, plate, photoUrl ->
                            viewModel.updateProfile(phone, area, model, plate, photoUrl)
                            scope.launch { snackbarHostState.showSnackbar("Data profil berhasil diperbarui!") }
                        },
                        isSosAlarmEnabled = isSosAlarmEnabled,
                        onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
                        onOpenSettings = { showSettingsDialog = true }
                    )
                    MainNavTab.ADMIN -> AdminGovernanceScreen(
                        currentMember = currentMember,
                        members = members,
                        onApproveScreening = { viewModel.approveMemberScreening(it) },
                        onSendNotification = { title, msg, sev ->
                            viewModel.sendNotification(title, msg, sev)
                            scope.launch { snackbarHostState.showSnackbar("Pengumuman berhasil dipancarkan!") }
                        }
                    )
                }
            }
        }
    }

    if (showRoleSwitcher) {
        RoleSwitcherDialog(
            members = members,
            currentMemberId = currentMember?.id ?: "",
            onSelectMember = { viewModel.switchRole(it) },
            onDismiss = { showRoleSwitcher = false }
        )
    }

    if (showEmergencyTrigger) {
        EmergencyTriggerDialog(
            onDismiss = { showEmergencyTrigger = false },
            onConfirm = { type, msg, loc ->
                viewModel.triggerEmergency(type, msg, loc)
                scope.launch { snackbarHostState.showSnackbar("Sinyal SOS Dipancarkan ke Radar Satgas!") }
            },
            isSosAlarmEnabled = isSosAlarmEnabled
        )
    }

    if (showAddKasDialog) {
        AddTransactionDialog(
            onDismiss = { showAddKasDialog = false },
            onConfirm = { title, amount, type, cat, desc ->
                cashViewModel.addTransaction(
                    title = title,
                    amount = amount,
                    type = type,
                    category = cat,
                    description = desc,
                    recordedBy = currentMember?.name ?: "Pengurus"
                )
                scope.launch { snackbarHostState.showSnackbar("Pencatatan kas berhasil disimpan!") }
            }
        )
    }

    if (showAwardPointDialog && selectedTargetForAward != null && currentMember != null) {
        AwardPointDialog(
            targetDriver = selectedTargetForAward!!,
            currentGiverRole = currentMember!!.role,
            onDismiss = { showAwardPointDialog = false },
            onAward = { isBeneficiary, reason ->
                viewModel.awardPeerPoints(
                    selectedTargetForAward!!.id,
                    selectedTargetForAward!!.name,
                    isBeneficiary,
                    reason
                )
                showAwardPointDialog = false
                scope.launch { snackbarHostState.showSnackbar("Apresiasi Poin Berhasil Dikirim!") }
            }
        )
    }

    if (showAddHazardDialog) {
        AddHazardDialog(
            onDismiss = { showAddHazardDialog = false },
            onSubmit = { title, type, location, desc, lat, lng ->
                viewModel.reportHazard(title, type, location, desc, lat, lng)
                showAddHazardDialog = false
                scope.launch { snackbarHostState.showSnackbar("Area Rawan Ditandai di Radar!") }
            }
        )
    }

    if (showNotifPrefDialog) {
        NotificationPreferencesDialog(
            initialPref = NotificationPreference(currentMember?.id ?: "DRG-001"),
            onDismiss = { showNotifPrefDialog = false },
            onSave = {
                viewModel.saveNotificationPref(it)
                showNotifPrefDialog = false
                scope.launch { snackbarHostState.showSnackbar("Preferensi notifikasi disimpan!") }
            },
            isSosAlarmEnabled = isSosAlarmEnabled,
            onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) }
        )
    }

    if (showSettingsDialog) {
        AppSettingsDialog(
            isSosAlarmEnabled = isSosAlarmEnabled,
            onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
            onTestSosAlarm = { viewModel.testSosAlarmSound() },
            onStopSosAlarm = { viewModel.stopSosAlarmSound() },
            isCrashGuardEnabled = isCrashGuardEnabled,
            onToggleCrashGuard = { viewModel.toggleCrashGuard(it) },
            crashSensitivity = crashSensitivity,
            onSelectCrashSensitivity = { viewModel.setCrashSensitivity(it) },
            onSimulateCrash = { viewModel.simulateCrashImpact(3.2f) },
            isLocationSharingConsent = currentMember?.isLocationSharingConsent == true,
            onToggleLocationSharing = { viewModel.toggleLocationSharingConsent(it) },
            cacheSizeDesc = viewModel.getCacheSizeDesc(context),
            selectedCachePolicy = mapCachePolicy,
            onSelectCachePolicy = { viewModel.setMapCachePolicy(it) },
            selectedSyncProfile = locationSyncProfile,
            onSelectSyncProfile = { viewModel.setLocationSyncProfile(it) },
            isDataSaverEnabled = isDataSaverMode,
            onToggleDataSaver = { viewModel.setDataSaverMode(it) },
            isPowerSaverEnabled = isPowerSaverMode,
            onTogglePowerSaver = { viewModel.setPowerSaverMode(it) },
            onPrecacheMap = { viewModel.precacheMap(context) },
            onClearCache = { viewModel.clearMapCache(context) },
            initialPref = NotificationPreference(currentMember?.id ?: "DRG-001"),
            onDismiss = { showSettingsDialog = false },
            onSavePref = {
                viewModel.saveNotificationPref(it)
                showSettingsDialog = false
                scope.launch { snackbarHostState.showSnackbar("Pengaturan & preferensi berhasil disimpan!") }
            }
        )
    }

    crashDetectedEvent?.let { gForce ->
        EmergencyCountdownDialog(
            detectedGForce = gForce,
            isSosAlarmEnabled = isSosAlarmEnabled,
            onCancel = { viewModel.dismissCrashCountdown() },
            onConfirmAutoSos = { viewModel.confirmCrashAutoSos(gForce) }
        )
    }
    }
}
