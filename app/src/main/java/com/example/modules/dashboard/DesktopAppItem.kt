package com.example.modules.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgGoldReward
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRedPanic

/**
 * Immutable model for a single launcher shortcut.
 *
 * @param id stable identifier used for testing and analytics.
 * @param title short label shown under the icon.
 * @param icon vector icon for the shortcut.
 * @param color accent color for icon tint and background wash.
 * @param tab destination tab navigated to on click.
 * @param isHot true for high-priority shortcuts (shows hot dot).
 */
data class DesktopAppItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tab: MainNavTab,
    val isHot: Boolean = false
)

/**
 * Provides the default set of launcher applications.
 *
 * @return 12-item immutable list in display order (4 per row).
 */
fun defaultLauncherApps(): List<DesktopAppItem> = listOf(
    DesktopAppItem("radar", "Live Radar", Icons.Default.GpsFixed, DrgGreenPrimary, MainNavTab.RADAR, true),
    DesktopAppItem("sos", "SOS Panic", Icons.Default.Warning, DrgRedPanic, MainNavTab.EMERGENCY, true),
    DesktopAppItem("posko", "Anggota", Icons.Default.Groups, DrgGrabGreenPrimary, MainNavTab.MEMBERS),
    DesktopAppItem("kas", "Kas Wallet", Icons.Default.AccountBalanceWallet, DrgAmberWarning, MainNavTab.KAS),
    DesktopAppItem("bengkel", "Forum Mitra", Icons.Default.Build, Color(0xFF0284C7), MainNavTab.FORUM),
    DesktopAppItem("intercom", "PTT Radio", Icons.Default.CellTower, Color(0xFF7C3AED), MainNavTab.COMMUNITY),
    DesktopAppItem("ai", "AI Assistant", Icons.Default.SmartToy, Color(0xFF2563EB), MainNavTab.COMMUNITY),
    DesktopAppItem("workflow", "Alur Lapangan", Icons.Default.ElectricBike, Color(0xFFD97706), MainNavTab.FIELD_WORKFLOW),
    DesktopAppItem("devtools", "Dev Tools", Icons.Default.Terminal, Color(0xFF059669), MainNavTab.DEV_TOOLS),
    DesktopAppItem("loyalty", "Rewards", Icons.Default.EmojiEvents, DrgGoldReward, MainNavTab.GAMIFICATION),
    DesktopAppItem("profile", "Profil KTA", Icons.Default.Badge, Color(0xFF0D9488), MainNavTab.PROFILE),
    DesktopAppItem("settings", "Tentang", Icons.Default.Info, Color(0xFF475569), MainNavTab.ABOUT)
)
