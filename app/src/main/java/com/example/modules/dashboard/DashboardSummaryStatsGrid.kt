package com.example.modules.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.atoms.StatCard
import com.example.ui.theme.*

@Composable
fun SummaryStatsGrid(
    activeMemberCount: Int,
    activeHazardCount: Int,
    kasFormatted: String,
    poskoCount: Int,
    onNavigateToTab: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Driver Aktif",
                value = "$activeMemberCount Anggota",
                subtitle = "Pantau langsung",
                icon = Icons.Default.People,
                iconColor = DrgGreenPrimary,
                onClick = { onNavigateToTab(2) },
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Bahaya Radar",
                value = "$activeHazardCount Titik",
                subtitle = "Selalu waspada",
                icon = Icons.Default.Warning,
                iconColor = DrgAmberSecondary,
                onClick = { onNavigateToTab(1) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                title = "Kas Transparan",
                value = "Rp $kasFormatted",
                subtitle = "Transparansi penuh",
                icon = Icons.Default.AccountBalanceWallet,
                iconColor = DrgGreenPrimary,
                onClick = { onNavigateToTab(4) },
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Posko Rehat",
                value = "$poskoCount Titik",
                subtitle = "Pemberhentian aman",
                icon = Icons.Default.Place,
                iconColor = DrgGreenDark,
                onClick = { onNavigateToTab(2) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
