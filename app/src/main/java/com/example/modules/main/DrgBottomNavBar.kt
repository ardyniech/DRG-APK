package com.example.modules.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.DrgGrabGreenContainer
import com.example.ui.theme.DrgGrabGreenPrimary

/**
 * Split Floating Liquid Glass Dock with Radar live-alert dot & Warga record-change count badge.
 */
@Composable
fun DrgBottomNavBar(
    selectedTab: MainNavTab,
    onSelectTab: (MainNavTab) -> Unit,
    activeEmergencyCount: Int,
    modifier: Modifier = Modifier,
    accentColor: Color = DrgGrabGreenPrimary,
    pillBgColor: Color = DrgGrabGreenContainer,
    glassBgColor: Color = Color.White.copy(alpha = 0.82f),
    communityUpdateCount: Int = 0,
    onEmergencyClick: () -> Unit = { onSelectTab(MainNavTab.EMERGENCY) }
) {
    val selectedIndex = resolveDockSelectedIndex(selectedTab)
    val animatedAccent by animateColorAsState(accentColor, tween(320), label = "dockAccent")
    val animatedPill by animateColorAsState(pillBgColor, tween(320), label = "dockPill")
    val animatedGlass by animateColorAsState(glassBgColor, tween(320), label = "dockGlass")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("bottom_nav_bar"),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .shadow(14.dp, CircleShape, ambientColor = animatedAccent, spotColor = animatedAccent)
                .clip(CircleShape)
                .background(Brush.verticalGradient(listOf(animatedGlass, Color.White.copy(alpha = 0.90f))))
                .border(1.3.dp, Brush.verticalGradient(listOf(Color.White, animatedAccent.copy(alpha = 0.28f))), CircleShape)
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            val slotWidth = maxWidth / NAV_DOCK_ENTRIES.size
            if (selectedIndex >= 0) {
                val indicatorOffset by animateDpAsState(
                    targetValue = slotWidth * selectedIndex,
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow),
                    label = "liquidPillOffset"
                )
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(slotWidth)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(26.dp))
                        .background(Brush.verticalGradient(listOf(animatedPill, Color.White.copy(alpha = 0.92f))))
                        .border(1.1.dp, Brush.verticalGradient(listOf(Color.White, animatedAccent.copy(alpha = 0.42f))), RoundedCornerShape(26.dp))
                )
            }
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                NAV_DOCK_ENTRIES.forEachIndexed { idx, item ->
                    DrgBottomNavItem(
                        label = item.label,
                        activeIcon = item.activeIcon,
                        inactiveIcon = item.inactiveIcon,
                        isSelected = idx == selectedIndex,
                        testTagKey = item.testTag,
                        accentColor = animatedAccent,
                        hasLiveDot = item.targetTab == MainNavTab.RADAR && activeEmergencyCount > 0,
                        badgeCount = if (item.targetTab == MainNavTab.COMMUNITY) communityUpdateCount else 0,
                        onClick = { onSelectTab(item.targetTab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        DrgBottomNavSatellite(
            activeEmergencyCount = activeEmergencyCount,
            isEmergencyActiveTab = selectedTab == MainNavTab.EMERGENCY,
            accentColor = animatedAccent,
            onClick = onEmergencyClick
        )
    }
}
