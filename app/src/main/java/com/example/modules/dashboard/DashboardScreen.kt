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
import com.example.shared.models.CommunityNotification
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
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
    poskoCount: Int,
    totalKasFormatted: String,
    onNavigateTab: (MainNavTab) -> Unit,
    onTriggerEmergency: () -> Unit,
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
                delay(1200) // Simulate data fetch reconciliation
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
            item { ShiftStatusBanner(member = currentMember) }

            val activeSos = activeAlerts.firstOrNull { it.isActive }
            if (activeSos != null) {
                item {
                    ActiveAlertBanner(alert = activeSos, onViewDetail = { onNavigateTab(MainNavTab.EMERGENCY) })
                }
            }

            item {
                Text(
                    text = "Pintas Tindakan Cepat (Quick Actions)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextPrimary
                )
            }

            item {
                QuickActionGrid(onNavigate = onNavigateTab, onQuickEmergency = onTriggerEmergency)
            }

            item {
                RecentAnnouncementsSection(announcements = announcements)
            }

            item {
                Text(
                    text = "Ringkasan Ekosistem Komunitas DRG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextPrimary
                )
            }

            item {
                SummaryStatsGrid(
                    activeMemberCount = activeMemberCount,
                    activeHazardCount = hazards.size,
                    kasFormatted = totalKasFormatted,
                    poskoCount = poskoCount,
                    onNavigateToTab = { onNavigateTab(it) }
                )
            }
        }
    }
}
