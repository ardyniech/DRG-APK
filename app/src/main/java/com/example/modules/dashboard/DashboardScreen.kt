package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.*
import com.example.ui.theme.*

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
    modifier: Modifier = Modifier
) {
    val activeMemberCount = members.count { it.isOnline }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Active Emergency Alert Glass Banners
        if (activeAlerts.isNotEmpty()) {
            activeAlerts.take(2).forEach { alert ->
                ActiveAlertBanner(
                    alert = alert,
                    onViewDetail = { onNavigateTab(MainNavTab.EMERGENCY) }
                )
            }
        }

        // 2. Pending Verification Glass Banner
        if (currentMember?.verificationStatus == VerificationStatus.PENDING_SCREENING) {
            ScreeningPendingCard(member = currentMember)
        }

        // 3. Weather Atmosphere & Driver Patrol Hero Glass Card
        WeatherHeroCard(
            member = currentMember,
            onUpdateDutyStatus = onUpdateDutyStatus,
            modifier = Modifier.testTag("weather_hero_card")
        )

        // 4. Floating Emergency TRC Glass Bar
        DashboardSosGlassBar(
            onTriggerEmergency = onTriggerEmergency,
            modifier = Modifier.testTag("dashboard_sos_bar")
        )

        // 5. Live Radar & Community Safety Glass Card
        CommunityLiveStatusCard(
            activeMemberCount = activeMemberCount,
            activeHazardCount = hazards.size,
            kasFormatted = totalKasFormatted,
            poskoCount = poskoCount,
            onNavigateTab = onNavigateTab,
            modifier = Modifier.testTag("community_live_status_card")
        )

        // 6. Quick Action Floating Glass Tiles
        QuickActionGrid(
            onNavigate = onNavigateTab,
            modifier = Modifier.testTag("quick_action_grid")
        )

        // 7. Rider Audio & Local-First Hardware Glass Card
        RiderHardwareRouteCard(
            modifier = Modifier.testTag("rider_hardware_card")
        )

        // 8. Community Announcements Floating Glass Card
        RecentAnnouncementsSection(
            announcements = announcements,
            modifier = Modifier.testTag("announcements_section")
        )

        // 9. Recent Community Activity Glass Card
        RecentCommunityActivitiesSection(
            announcements = announcements,
            cashTransactions = cashTransactions,
            hazards = hazards,
            onNavigateTab = onNavigateTab,
            modifier = Modifier.testTag("community_activities_section")
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
