package com.example.modules.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.viewmodel.MainNavTab

internal data class NavDockEntry(
    val label: String,
    val targetTab: MainNavTab,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val testTag: String
)

internal val NAV_DOCK_ENTRIES = listOf(
    NavDockEntry("Beranda", MainNavTab.DASHBOARD, Icons.Filled.Home, Icons.Outlined.Home, "nav_tab_dashboard"),
    NavDockEntry("Radar", MainNavTab.RADAR, Icons.Filled.Explore, Icons.Outlined.Explore, "nav_tab_radar"),
    NavDockEntry("Warga", MainNavTab.COMMUNITY, Icons.Filled.Groups, Icons.Outlined.Groups, "nav_tab_community"),
    NavDockEntry("Profil", MainNavTab.PROFILE, Icons.Filled.Person, Icons.Outlined.PersonOutline, "nav_tab_profile")
)

internal fun resolveDockSelectedIndex(selectedTab: MainNavTab): Int = when (selectedTab) {
    MainNavTab.DASHBOARD, MainNavTab.FIELD_WORKFLOW -> 0
    MainNavTab.RADAR -> 1
    MainNavTab.COMMUNITY, MainNavTab.KAS, MainNavTab.FORUM, MainNavTab.MEMBERS, MainNavTab.GAMIFICATION, MainNavTab.EVENTS -> 2
    MainNavTab.PROFILE, MainNavTab.SETTINGS, MainNavTab.ADMIN, MainNavTab.ROLES, MainNavTab.ABOUT, MainNavTab.DEV_TOOLS -> 3
    MainNavTab.EMERGENCY -> -1
}
