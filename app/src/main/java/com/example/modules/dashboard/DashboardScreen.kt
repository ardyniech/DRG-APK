package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(800)
                isRefreshing = false
            }
        },
        modifier = modifier.fillMaxSize().background(DrgBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Driver Profile Details
            item {
                DriverProfileOverviewCard(
                    member = currentMember,
                    onStatusChange = onUpdateDutyStatus
                )
            }

            // Active SOS Emergency Alert if any
            val activeSos = activeAlerts.firstOrNull { it.isActive }
            if (activeSos != null) {
                item {
                    ActiveAlertBanner(alert = activeSos, onViewDetail = { onNavigateTab(MainNavTab.EMERGENCY) })
                }
            }

            // 2. Community Live Status
            item {
                Text(
                    text = "Status Ekosistem Komunitas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
            }
            item {
                CommunityLiveStatusCard(
                    activeMemberCount = activeMemberCount,
                    activeHazardCount = hazards.size,
                    kasFormatted = totalKasFormatted,
                    poskoCount = poskoCount,
                    onNavigateTab = onNavigateTab
                )
            }

            // Quick Actions
            item {
                Text(
                    text = "Akses Cepat (Quick Actions)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
            }
            item {
                QuickActionGrid(onNavigate = onNavigateTab, onQuickEmergency = onTriggerEmergency)
            }

            // 3. Summary of Recent Community Activities
            item {
                RecentCommunityActivitiesSection(
                    announcements = announcements,
                    cashTransactions = cashTransactions,
                    hazards = hazards,
                    onNavigateTab = onNavigateTab
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
