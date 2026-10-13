package com.example.modules.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.atoms.DrgGlassCard
import com.example.ui.theme.*

@Composable
fun DrgBottomNavBar(
    selectedTab: MainNavTab,
    onSelectTab: (MainNavTab) -> Unit,
    activeEmergencyCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        DrgGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            backgroundColor = Color.White.copy(alpha = 0.90f),
            borderBrush = DrgGlassBorderBrush,
            elevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
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
                    isSelected = selectedTab in listOf(MainNavTab.COMMUNITY, MainNavTab.KAS, MainNavTab.FORUM, MainNavTab.MEMBERS, MainNavTab.GAMIFICATION),
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
}
