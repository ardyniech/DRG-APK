package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

private data class PulseStripItem(
    val label: String,
    val metric: String,
    val icon: ImageVector,
    val accent: Color,
    val tab: MainNavTab
)

/**
 * Horizontal frosted squircle telemetry strip inspired by Apple Weather's hourly row,
 * showing live DRG community operational & treasury metrics.
 */
@Composable
fun DashboardPulseStrip(
    visualTheme: DashboardVisualTheme,
    focusMode: DashboardFocusMode,
    activeMemberCount: Int,
    totalMemberCount: Int,
    hazardCount: Int,
    poskoCount: Int,
    transactionCount: Int,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.primaryColor
    val alt = visualTheme.secondaryColor
    val items = if (focusMode == DashboardFocusMode.SATGAS_TRC) {
        listOf(
            PulseStripItem("Siaga", "$activeMemberCount On", Icons.Default.GpsFixed, accent, MainNavTab.RADAR),
            PulseStripItem("Jalur", if (hazardCount > 0) "$hazardCount Awas" else "Aman", Icons.Default.Warning, if (hazardCount > 0) DrgRedPanic else accent, MainNavTab.RADAR),
            PulseStripItem("Posko", "$poskoCount Titik", Icons.Default.Place, alt, MainNavTab.RADAR),
            PulseStripItem("Lapangan", "Check-In", Icons.Default.Route, Color(0xFFD97706), MainNavTab.FIELD_WORKFLOW),
            PulseStripItem("TRC", "Siaga 24j", Icons.Default.Shield, DrgRedPanic, MainNavTab.EMERGENCY)
        )
    } else {
        listOf(
            PulseStripItem("Kas", "Aktif", Icons.Default.AccountBalanceWallet, accent, MainNavTab.KAS),
            PulseStripItem("Mutasi", "$transactionCount Log", Icons.AutoMirrored.Filled.ReceiptLong, alt, MainNavTab.KAS),
            PulseStripItem("Anggota", "$totalMemberCount Org", Icons.Default.Groups, Color(0xFF0284C7), MainNavTab.MEMBERS),
            PulseStripItem("Forum", "Diskusi", Icons.Default.Forum, Color(0xFFD97706), MainNavTab.FORUM),
            PulseStripItem("Poin", "Reward", Icons.Default.Stars, Color(0xFF7C3AED), MainNavTab.GAMIFICATION)
        )
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items, key = { it.label }) { item ->
            val cardShape = RoundedCornerShape(20.dp)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .width(78.dp)
                    .clip(cardShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(visualTheme.glassBg, visualTheme.glassBg.copy(alpha = 0.62f))
                        )
                    )
                    .border(1.2.dp, Color.White.copy(alpha = 0.9f), cardShape)
                    .bouncyClick { onNavigateTab(item.tab) }
                    .padding(vertical = 10.dp, horizontal = 6.dp)
            ) {
                Text(text = item.label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSecondary)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.accent.copy(alpha = 0.16f))
                        .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(item.icon, contentDescription = item.label, tint = item.accent, modifier = Modifier.size(18.dp))
                }
                Text(
                    text = item.metric,
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextDark
                )
            }
        }
    }
}
