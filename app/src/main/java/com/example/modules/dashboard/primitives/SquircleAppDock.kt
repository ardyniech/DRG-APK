package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.MemberRole
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun SquircleAppDock(
    visualTheme: DashboardVisualTheme,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier,
    role: MemberRole? = null,
    radarCount: Int = 0,
    emergencyCount: Int = 0,
    forumCount: Int = 0,
    kasCount: Int = 0,
    membersCount: Int = 0,
    communityNotifCount: Int = 0,
    openTasksCount: Int = 0,
    poskoCount: Int = 0,
    pendingAdminCount: Int = 0,
    eventsCount: Int = 0
) {
    val features = buildDashboardLauncherFeatures(
        visualTheme, role, radarCount, emergencyCount, forumCount, kasCount,
        membersCount, communityNotifCount, openTasksCount, poskoCount, pendingAdminCount, eventsCount
    )
    val seenState = rememberLauncherSeenState(features.map { it.id })
    val totalUpdates = features.sumOf { seenState.badgeFor(it.id, it.recordCount, it.alwaysBadge) }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Menu Fitur DRG", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(if (totalUpdates > 0) DrgRedPanic.copy(alpha = 0.14f) else visualTheme.pillBg).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (totalUpdates > 0) "$totalUpdates Baru" else "${features.size} Fitur",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (totalUpdates > 0) DrgRedPanic else visualTheme.primaryColor
                    )
                }
            }
            features.chunked(4).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    rowItems.forEach { item ->
                        val badge = seenState.badgeFor(item.id, item.recordCount, item.alwaysBadge)
                        SquircleLauncherTile(item, badge, onClick = { seenState.markSeen(item.id, item.recordCount); onNavigateTab(item.tab) }, Modifier.weight(1f))
                    }
                    repeat(4 - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun SquircleLauncherTile(item: SquircleLauncherFeature, badgeCount: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val squircle = RoundedCornerShape(16.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier.testTag("launcher_tile_${item.id}").defaultMinSize(minHeight = 48.dp).bouncyClick { onClick() }.padding(vertical = 2.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp, end = 4.dp)
                    .size(48.dp)
                    .shadow(5.dp, squircle, ambientColor = item.color.copy(alpha = 0.24f), spotColor = item.color.copy(alpha = 0.18f))
                    .clip(squircle)
                    .background(Brush.linearGradient(listOf(item.color.copy(alpha = 0.22f), item.color.copy(alpha = 0.10f))))
                    .border(1.3.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.95f), item.color.copy(alpha = 0.35f))), squircle),
                contentAlignment = Alignment.Center
            ) { Icon(item.icon, contentDescription = item.title, tint = item.color, modifier = Modifier.size(22.dp)) }
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier.testTag("launcher_badge_${item.id}").defaultMinSize(minWidth = 18.dp, minHeight = 16.dp).clip(CircleShape).background(DrgRedPanic).border(1.5.dp, Color.White, CircleShape).padding(horizontal = 5.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center
                ) { Text(if (badgeCount > 99) "99+" else "$badgeCount", fontFamily = SpaceGroteskFontFamily, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 11.sp) }
            }
        }
        Text(item.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}
