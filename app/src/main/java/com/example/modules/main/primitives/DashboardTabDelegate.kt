package com.example.modules.main.primitives

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.updateDutyStatus
import com.example.modules.dashboard.DashboardScreen
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.*

@Composable
fun DashboardTabDelegate(
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    members: List<DriverMember>,
    activeAlerts: List<EmergencyAlert>,
    hazards: List<HazardArea>,
    announcements: List<CommunityNotification>,
    cashTransactions: List<KasTransaction>,
    poskoCount: Int,
    netBalance: Long,
    onShowEmergencyTrigger: () -> Unit,
    dashboardTheme: DashboardVisualTheme,
    onSelectDashboardTheme: (DashboardVisualTheme) -> Unit,
    dashboardFocusMode: DashboardFocusMode,
    forumActivityCount: Int,
    openTasksCount: Int,
    eventUpdatesCount: Int,
    scaffoldPadding: PaddingValues
) {
    DashboardScreen(
        currentMember = currentMember,
        members = members,
        activeAlerts = activeAlerts,
        hazards = hazards,
        announcements = announcements,
        cashTransactions = cashTransactions,
        poskoCount = poskoCount,
        totalKasFormatted = String.format("%,d", netBalance),
        onNavigateTab = { viewModel.selectTab(it) },
        onTriggerEmergency = onShowEmergencyTrigger,
        onUpdateDutyStatus = { status, online -> viewModel.updateDutyStatus(status, online) },
        visualTheme = dashboardTheme,
        onSelectVisualTheme = onSelectDashboardTheme,
        focusMode = dashboardFocusMode,
        forumPostsCount = forumActivityCount,
        openTasksCount = openTasksCount,
        eventsCount = eventUpdatesCount,
        contentPadding = scaffoldPadding
    )
}
