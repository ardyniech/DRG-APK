package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.*
import com.example.ui.theme.*
import java.text.NumberFormat

@Composable
fun DashboardScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    activeAlerts: List<EmergencyAlert>,
    transactions: List<KasTransaction>,
    notifications: List<CommunityNotification>,
    onNavigateTab: (MainNavTab) -> Unit,
    onTriggerEmergency: () -> Unit,
    onApproveSelf: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val activeMemberCount = members.count { it.isOnline }
    val totalBalance = transactions.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
    val totalKasFormatted = NumberFormat.getInstance().format(totalBalance)
    val poskoCount = 4

    LazyColumn(
        modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
    ) {
        if (currentMember?.verificationStatus == VerificationStatus.PENDING_SCREENING) {
            item {
                ScreeningPendingCard(member = currentMember, onApproveSelf = onApproveSelf)
            }
        }

        item { ShiftStatusBanner(member = currentMember) }
        item { RecommendedTasksCard(member = currentMember) }
        item { CommunityHeroBanner() }

        val activeSos = activeAlerts.firstOrNull { it.isActive }
        if (activeSos != null) {
            item {
                ActiveAlertBanner(alert = activeSos, onViewDetail = { onNavigateTab(MainNavTab.EMERGENCY) })
            }
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
                activeHazardCount = 0,
                kasFormatted = totalKasFormatted,
                poskoCount = poskoCount,
                onNavigateToTab = { index ->
                    when (index) {
                        1 -> onNavigateTab(MainNavTab.RADAR)
                        2 -> onNavigateTab(MainNavTab.MEMBERS)
                        4 -> onNavigateTab(MainNavTab.KAS)
                        else -> onNavigateTab(MainNavTab.DASHBOARD)
                    }
                }
            )
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
    }
}
