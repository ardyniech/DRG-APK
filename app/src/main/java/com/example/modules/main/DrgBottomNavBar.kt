package com.example.modules.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            NavItem(
                label = "Beranda",
                icon = Icons.Default.Home,
                isSelected = selectedTab == MainNavTab.DASHBOARD,
                onClick = { onSelectTab(MainNavTab.DASHBOARD) }
            )
            NavItem(
                label = "Radar Live",
                icon = Icons.Default.GpsFixed,
                isSelected = selectedTab == MainNavTab.RADAR || selectedTab == MainNavTab.EMERGENCY,
                badgeCount = activeEmergencyCount,
                badgeColor = DrgRedDanger,
                onClick = { onSelectTab(MainNavTab.RADAR) }
            )
            NavItem(
                label = "Komunitas",
                icon = Icons.Default.Hub,
                isSelected = selectedTab == MainNavTab.COMMUNITY || selectedTab == MainNavTab.KAS || selectedTab == MainNavTab.FORUM || selectedTab == MainNavTab.MEMBERS || selectedTab == MainNavTab.GAMIFICATION,
                onClick = { onSelectTab(MainNavTab.COMMUNITY) }
            )
            NavItem(
                label = "Profil",
                icon = Icons.Default.Person,
                isSelected = selectedTab == MainNavTab.PROFILE || selectedTab == MainNavTab.ADMIN,
                onClick = { onSelectTab(MainNavTab.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    badgeColor: Color = DrgAmberSecondary,
    onClick: () -> Unit
) {
    val tint = if (isSelected) DrgAmberDark else DrgTextMuted
    val bg = if (isSelected) DrgAmberContainer else Color.Transparent

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bg,
        modifier = Modifier.height(52.dp).padding(horizontal = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-4).dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColor)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "$badgeCount",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = label,
                fontSize = 10.sp,
                color = tint,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}
