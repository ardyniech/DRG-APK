package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun RecentCommunityActivitiesSection(
    announcements: List<CommunityNotification>,
    cashTransactions: List<KasTransaction>,
    hazards: List<HazardArea>,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Kas & Iuran", "Keselamatan", "Pengumuman")

    val activities = remember(announcements, cashTransactions, hazards, selectedFilter) {
        val list = mutableListOf<ActivityItemModel>()
        if (selectedFilter in listOf("Semua", "Pengumuman")) {
            announcements.take(2).forEach {
                list.add(ActivityItemModel(it.id, it.title, it.message, "Pengumuman", it.timeAgo, Icons.Default.Campaign, DrgGreenPrimary, DrgGreenContainer))
            }
        }
        if (selectedFilter in listOf("Semua", "Kas & Iuran")) {
            cashTransactions.take(2).forEach {
                val isIncome = it.type == TransactionType.INCOME
                val color = if (isIncome) DrgGreenPrimary else DrgAmberSecondary
                val bg = if (isIncome) DrgGreenContainer else DrgAmberContainer
                val prefix = if (isIncome) "+Rp " else "-Rp "
                list.add(ActivityItemModel(it.id, "${it.title} ($prefix${String.format("%,d", it.amount)})", it.category.label, "Kas & Iuran", it.dateString, Icons.Default.AccountBalanceWallet, color, bg) { onNavigateTab(MainNavTab.KAS) })
            }
        }
        if (selectedFilter in listOf("Semua", "Keselamatan")) {
            hazards.take(2).forEach {
                list.add(ActivityItemModel(it.id, "Laporan Bahaya: ${it.title}", it.locationName, "Keselamatan", "Aktif", Icons.Default.Warning, DrgRedDanger, DrgRedContainer) { onNavigateTab(MainNavTab.RADAR) })
            }
        }
        list
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.History, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(18.dp))
                    Text(text = "Aktivitas Komunitas Terkini", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                }
                Text(text = "${activities.size} Catatan", fontSize = 11.sp, color = DrgTextSecondary)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) DrgGreenPrimary else DrgSurface)
                            .border(0.8.dp, if (isSelected) DrgGreenPrimary else DrgOutline.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else DrgTextSecondary
                        )
                    }
                }
            }

            if (activities.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Belum ada aktivitas pada kategori ini.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            } else {
                activities.forEachIndexed { index, item ->
                    CommunityActivityRow(activity = item)
                    if (index < activities.size - 1) {
                        HorizontalDivider(color = DrgOutline.copy(alpha = 0.2f), thickness = 0.8.dp)
                    }
                }
            }
        }
    }
}
