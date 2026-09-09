package com.example.modules.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.*

@Composable
fun DrgBottomNavBar(
    selectedTab: MainNavTab,
    onSelectTab: (MainNavTab) -> Unit,
    activeEmergencyCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DrgSurface,
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DrgBottomNavItem(
                label = "Beranda",
                icon = Icons.Default.Home,
                isSelected = selectedTab == MainNavTab.DASHBOARD,
                onClick = { onSelectTab(MainNavTab.DASHBOARD) }
            )
            DrgBottomNavItem(
                label = "Radar Live",
                icon = Icons.Default.GpsFixed,
                isSelected = selectedTab == MainNavTab.RADAR,
                badgeCount = activeEmergencyCount,
                badgeColor = DrgRedDanger,
                onClick = { onSelectTab(MainNavTab.RADAR) }
            )
            DrgBottomNavItem(
                label = "Komunitas",
                icon = Icons.Default.Hub,
                isSelected = selectedTab == MainNavTab.COMMUNITY || selectedTab == MainNavTab.KAS || selectedTab == MainNavTab.FORUM || selectedTab == MainNavTab.MEMBERS || selectedTab == MainNavTab.GAMIFICATION,
                onClick = { onSelectTab(MainNavTab.COMMUNITY) }
            )
            DrgBottomNavItem(
                label = "Profil",
                icon = Icons.Default.Person,
                isSelected = selectedTab == MainNavTab.PROFILE || selectedTab == MainNavTab.ADMIN,
                onClick = { onSelectTab(MainNavTab.PROFILE) }
            )
        }
    }
}
