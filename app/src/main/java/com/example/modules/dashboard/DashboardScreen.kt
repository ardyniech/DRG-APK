package com.example.modules.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.primitives.*
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.*

@Composable
fun DashboardScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    activeAlerts: List<EmergencyAlert>,
    hazards: List<HazardArea>,
    announcements: List<CommunityNotification>,
    cashTransactions: List<KasTransaction> = emptyList(),
    poskoCount: Int,
    totalKasFormatted: String,
    onNavigateTab: (MainNavTab) -> Unit,
    onTriggerEmergency: () -> Unit,
    onUpdateDutyStatus: (String, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    visualTheme: DashboardVisualTheme? = null,
    onSelectVisualTheme: ((DashboardVisualTheme) -> Unit)? = null,
    focusMode: DashboardFocusMode = DashboardFocusMode.SATGAS_TRC,
    forumPostsCount: Int = 0,
    openTasksCount: Int = 0,
    eventsCount: Int = 0,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val activeMemberCount = members.count { it.isOnline }
    val activeTheme = visualTheme ?: DashboardVisualTheme.EMERALD_AURORA
    val pendingScreening = members.count { it.verificationStatus == VerificationStatus.PENDING_SCREENING }

    Box(modifier = modifier.fillMaxSize().testTag("dashboard_screen")) {
        DashboardAtmosphereCanvas(theme = activeTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (activeAlerts.isNotEmpty()) {
                activeAlerts.take(2).forEach { ActiveAlertBanner(alert = it, onViewDetail = { onNavigateTab(MainNavTab.EMERGENCY) }) }
            }
            if (currentMember?.verificationStatus == VerificationStatus.PENDING_SCREENING) {
                ScreeningPendingCard(member = currentMember)
            }

            CinematicCockpitHeader(
                member = currentMember, activeMemberCount = activeMemberCount, totalMembersCount = members.size,
                kasFormatted = totalKasFormatted, visualTheme = activeTheme, focusMode = focusMode,
                onTriggerEmergency = onTriggerEmergency, onUpdateDutyStatus = onUpdateDutyStatus,
                modifier = Modifier.testTag("weather_hero_card")
            )
            DashboardPulseStrip(
                visualTheme = activeTheme, focusMode = focusMode, activeMemberCount = activeMemberCount,
                totalMemberCount = members.size, hazardCount = hazards.size, poskoCount = poskoCount,
                transactionCount = cashTransactions.size, onNavigateTab = onNavigateTab,
                modifier = Modifier.testTag("dashboard_pulse_strip")
            )
            DashboardModeCardSwitcher(
                mode = focusMode, visualTheme = activeTheme, activeMemberCount = activeMemberCount,
                members = members, hazards = hazards, cashTransactions = cashTransactions,
                poskoCount = poskoCount, totalKasFormatted = totalKasFormatted, onNavigateTab = onNavigateTab
            )
            LiveActivityTickerCapsule(
                announcements = announcements, visualTheme = activeTheme,
                onNavigateTab = onNavigateTab, modifier = Modifier.testTag("announcements_section")
            )
            SquircleAppDock(
                visualTheme = activeTheme, onNavigateTab = onNavigateTab, role = currentMember?.role,
                radarCount = hazards.size, emergencyCount = activeAlerts.size, forumCount = forumPostsCount,
                kasCount = cashTransactions.size, membersCount = members.size, communityNotifCount = announcements.size,
                openTasksCount = openTasksCount, poskoCount = poskoCount, pendingAdminCount = pendingScreening,
                eventsCount = eventsCount,
                modifier = Modifier.testTag("dashboard_desktop_launcher")
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
