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
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.HazardArea
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    activeAlerts: List<EmergencyAlert>,
    hazards: List<HazardArea>,
    poskoCount: Int,
    totalKasFormatted: String,
    onNavigateTab: (MainNavTab) -> Unit,
    onTriggerEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeMemberCount = members.count { it.isOnline }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
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
