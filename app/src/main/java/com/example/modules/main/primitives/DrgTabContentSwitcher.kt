package com.example.modules.main.primitives

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.core.viewmodel.CashManagementViewModel
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.MainNavTab
import com.example.modules.admin.AdminGovernanceScreen
import com.example.modules.dashboard.DashboardScreen
import com.example.modules.emergency.EmergencyScreen
import com.example.modules.profile.ProfileScreen
import com.example.modules.radar.RadarScreen
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
    totalIncome: Long,
    totalExpense: Long,
    netBalance: Long,
    posts: List<ForumPost>,
    workshops: List<WorkshopPartner>,
    poskoList: List<PoskoLocation>,
    notifications: List<CommunityNotification>,
    badges: List<BadgeItem>,
    tasks: List<CommunityTask>,
    rewards: List<RewardItem>,
    hazards: List<HazardArea>,
    pointTransactions: List<PointTransaction>,
    adminLogs: List<AdminLog>,
    isSosAlarmEnabled: Boolean,
    isCrashGuardEnabled: Boolean,
    crashSensitivity: CrashSensitivity,
    isPowerSaverMode: Boolean,
    snackbarHostState: SnackbarHostState,
    onShowEmergencyTrigger: () -> Unit,
    onShowAddKasDialog: () -> Unit,
    onShowAddHazardDialog: () -> Unit,
    onShowSettingsDialog: () -> Unit,
    onShowAwardPointDialog: (DriverMember) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Crossfade(
        targetState = currentTab,
        animationSpec = tween(durationMillis = 200),
        label = "tabTransition"
    ) { tab ->
        when (tab) {
            MainNavTab.DASHBOARD -> DashboardScreen(
                currentMember = currentMember,
                members = members,
                activeAlerts = activeAlerts,
                hazards = hazards,
                announcements = notifications,
                poskoCount = poskoList.size,
                totalKasFormatted = String.format("%,d", netBalance),
                onNavigateTab = { viewModel.selectTab(it) },
                onTriggerEmergency = onShowEmergencyTrigger
            )
            MainNavTab.RADAR -> RadarScreen(
                members = members, alerts = activeAlerts, poskoList = poskoList, hazards = hazards,
                currentMember = currentMember, onToggleConsent = { viewModel.toggleLocationSharingConsent(it) },
                onAddHazardClick = onShowAddHazardDialog, onConfirmHazard = { viewModel.confirmHazard(it) },
                onTriggerEmergency = onShowEmergencyTrigger,
                onCallDriver = { phone -> WhatsAppLauncher.openChat(context, phone, "Halo rekan DRG, koordinasi tim satgas segera.") },
                isPowerSaverEnabled = isPowerSaverMode
            )
            MainNavTab.EMERGENCY -> EmergencyScreen(
                currentMember = currentMember, alerts = activeAlerts,
                onTriggerEmergency = { type, msg, loc ->
                    viewModel.triggerEmergency(type, msg, loc)
                    scope.launch { snackbarHostState.showSnackbar("Sinyal SOS Dipancarkan ke Seluruh Satgas!") }
                },
                onResolveEmergency = { viewModel.resolveEmergency(it) },
                onRespondEmergency = { viewModel.respondToEmergency(it) },
                onCallDriver = { phone -> WhatsAppLauncher.openChat(context, phone, "Halo rekan DRG, kami memantau sinyal darurat SOS Anda. Bagaimana kondisi terkini?") },
                isSosAlarmEnabled = isSosAlarmEnabled, onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
                isCrashGuardEnabled = isCrashGuardEnabled, crashSensitivity = crashSensitivity,
                onSimulateCrash = { viewModel.simulateCrashImpact(3.2f) }, onOpenSettings = onShowSettingsDialog,
                onBack = { viewModel.selectTab(MainNavTab.DASHBOARD) }
            )
            MainNavTab.COMMUNITY, MainNavTab.KAS, MainNavTab.FORUM, MainNavTab.GAMIFICATION, MainNavTab.MEMBERS -> {
                CommunityTabDelegate(
                    tab = tab, viewModel = viewModel, currentMember = currentMember, members = members,
                    cashTransactions = cashTransactions, totalIncome = totalIncome, totalExpense = totalExpense,
                    netBalance = netBalance, posts = posts, workshops = workshops, badges = badges, tasks = tasks,
                    rewards = rewards, poskoList = poskoList, pointTransactions = pointTransactions,
                    adminLogs = adminLogs, snackbarHostState = snackbarHostState,
                    onShowAddKasDialog = onShowAddKasDialog, onShowAwardPointDialog = onShowAwardPointDialog
                )
            }
            MainNavTab.PROFILE -> ProfileScreen(
                currentMember = currentMember,
                onUpdateProfile = { phone, area, model, plate, photoUrl ->
                    viewModel.updateProfile(phone, area, model, plate, photoUrl)
                    scope.launch { snackbarHostState.showSnackbar("Data profil berhasil diperbarui!") }
                },
                isSosAlarmEnabled = isSosAlarmEnabled, onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
                onOpenSettings = onShowSettingsDialog
            )
            MainNavTab.ADMIN -> AdminGovernanceScreen(
                currentMember = currentMember, members = members,
                onApproveScreening = { viewModel.approveMemberScreening(it) },
                onSendNotification = { title, msg, sev ->
                    viewModel.sendNotification(title, msg, sev)
                    scope.launch { snackbarHostState.showSnackbar("Pengumuman berhasil dipancarkan!") }
                }
            )
        }
    }
}
