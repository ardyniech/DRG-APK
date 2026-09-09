package com.example.modules.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.*

@Composable
fun QuickActionGrid(
    onNavigate: (MainNavTab) -> Unit,
    onQuickEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RoadEmergencySosBar(onQuickEmergency = onQuickEmergency)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickTile(
                title = "Live Radar",
                desc = "Posisi Driver & Posko",
                icon = Icons.Default.GpsFixed,
                color = DrgGreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.RADAR) }
            )
            QuickTile(
                title = "Kas Transparan",
                desc = "Laporan Keuangan",
                icon = Icons.Default.AccountBalanceWallet,
                color = DrgAmberSecondary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.KAS) }
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickTile(
                title = "Bengkel Mitra",
                desc = "Diskon Khusus DRG",
                icon = Icons.Default.Build,
                color = DrgGrabGreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.FORUM) }
            )
            QuickTile(
                title = "Anggota & Posko",
                desc = "Screening & Absensi",
                icon = Icons.Default.People,
                color = DrgGrabGreenDark,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.MEMBERS) }
            )
        }
    }
}
