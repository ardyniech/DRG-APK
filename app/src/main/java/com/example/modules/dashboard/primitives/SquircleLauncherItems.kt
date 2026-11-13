package com.example.modules.dashboard.primitives

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.DrgWarningText
import com.example.ui.theme.DrgPurpleAccent
import com.example.ui.theme.DrgBlueAccent
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.MemberRole
import com.example.ui.theme.DrgRedPanic

data class SquircleLauncherFeature(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tab: MainNavTab,
    val recordCount: Int = 0,
    val alwaysBadge: Boolean = false
)

fun buildDashboardLauncherFeatures(
    visualTheme: DashboardVisualTheme,
    role: MemberRole?,
    radarCount: Int,
    emergencyCount: Int,
    forumCount: Int,
    kasCount: Int,
    membersCount: Int,
    communityNotifCount: Int,
    openTasksCount: Int,
    poskoCount: Int,
    pendingAdminCount: Int,
    eventsCount: Int = 0
): List<SquircleLauncherFeature> = buildList {
    add(SquircleLauncherFeature("radar", "Radar", Icons.Default.Radar, visualTheme.primaryColor, MainNavTab.RADAR, radarCount))
    add(SquircleLauncherFeature("sos", "Darurat", Icons.Default.Warning, DrgRedPanic, MainNavTab.EMERGENCY, emergencyCount, alwaysBadge = true))
    add(SquircleLauncherFeature("event", "Event", Icons.Default.EventAvailable, Color(0xFFDB2777), MainNavTab.EVENTS, eventsCount))
    add(SquircleLauncherFeature("forum", "Forum", Icons.Default.Forum, DrgWarningText, MainNavTab.FORUM, forumCount))
    add(SquircleLauncherFeature("kas", "Kas", Icons.Default.AccountBalanceWallet, Color(0xFF059669), MainNavTab.KAS, kasCount))
    add(SquircleLauncherFeature("members", "Anggota", Icons.Default.Groups, DrgBlueAccent, MainNavTab.MEMBERS, membersCount))
    add(SquircleLauncherFeature("community", "Info", Icons.Default.Campaign, Color(0xFF0EA5E9), MainNavTab.COMMUNITY, communityNotifCount))
    add(SquircleLauncherFeature("tasks", "Misi", Icons.Default.EmojiEvents, Color(0xFFEAB308), MainNavTab.GAMIFICATION, openTasksCount))
    add(SquircleLauncherFeature("workflow", "Lapangan", Icons.Default.Route, Color(0xFFEA580C), MainNavTab.FIELD_WORKFLOW, poskoCount))
    add(SquircleLauncherFeature("posko", "Posko", Icons.Default.Place, Color(0xFF0D9488), MainNavTab.RADAR, poskoCount))
    add(SquircleLauncherFeature("profile", "Profil", Icons.Default.Person, DrgPurpleAccent, MainNavTab.PROFILE, 0))
    if (role == MemberRole.SUPER_ADMIN || role?.isLeadership == true) {
        add(SquircleLauncherFeature("admin", "Admin", Icons.Default.AdminPanelSettings, Color(0xFF2563EB), MainNavTab.ADMIN, pendingAdminCount, alwaysBadge = true))
    }
    if (role == MemberRole.SUPER_ADMIN) {
        add(SquircleLauncherFeature("devtools", "DevTools", Icons.Default.Terminal, Color(0xFF4F46E5), MainNavTab.DEV_TOOLS, 0))
    }
}
