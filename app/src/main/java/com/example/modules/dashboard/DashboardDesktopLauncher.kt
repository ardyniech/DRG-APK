package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

data class DesktopAppItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tab: MainNavTab,
    val isHot: Boolean = false
)

@Composable
fun DashboardDesktopLauncher(
    onNavigate: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcherApps = listOf(
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

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.92f),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp), ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, Brush.linearGradient(listOf(Color.White, Color.White.copy(alpha = 0.6f), DrgGreenPrimary.copy(alpha = 0.15f))), RoundedCornerShape(22.dp))
            .testTag("dashboard_desktop_launcher")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DrgGreenPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.GridView, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text("Launcher Aplikasi DRG", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                        Text("Pintasan Akses Cepat Ekosistem", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                }
                Surface(shape = RoundedCornerShape(12.dp), color = DrgGreenLight) {
                    Text("12 Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                launcherApps.chunked(4).forEach { rowApps ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        rowApps.forEach { app ->
                            LauncherTile(app = app, onClick = { onNavigate(app.tab) }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LauncherTile(app: DesktopAppItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .bouncyClick { onClick() }
            .padding(4.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = app.color.copy(alpha = 0.3f))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.verticalGradient(listOf(Color.White, app.color.copy(alpha = 0.12f))))
                    .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(app.icon, contentDescription = app.title, tint = app.color, modifier = Modifier.size(24.dp))
            }
            if (app.isHot) {
                Box(
                    modifier = Modifier
                        .offset(x = 2.dp, y = (-2).dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(app.color)
                        .border(1.5.dp, Color.White, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = app.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = DrgTextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
