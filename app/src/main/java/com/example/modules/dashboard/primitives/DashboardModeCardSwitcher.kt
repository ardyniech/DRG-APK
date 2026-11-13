package com.example.modules.dashboard.primitives

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.HazardArea
import com.example.shared.models.KasTransaction
import com.example.shared.models.VerificationStatus

/**
 * Animated switcher between Mode Satgas (A) Bento Deck and Mode Administrasi (B) Focus Card.
 */
@Composable
fun DashboardModeCardSwitcher(
    mode: DashboardFocusMode,
    visualTheme: DashboardVisualTheme,
    activeMemberCount: Int,
    members: List<DriverMember>,
    hazards: List<HazardArea>,
    cashTransactions: List<KasTransaction>,
    poskoCount: Int,
    totalKasFormatted: String,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val verifiedCount = members.count { it.verificationStatus == VerificationStatus.VERIFIED }
    AnimatedContent(
        targetState = mode,
        transitionSpec = { fadeIn(tween(260)) togetherWith fadeOut(tween(180)) },
        label = "modeSwitch",
        modifier = modifier
    ) { activeMode ->
        if (activeMode == DashboardFocusMode.SATGAS_TRC) {
            BentoGridDeck(
                activeMemberCount = activeMemberCount,
                hazardCount = hazards.size,
                kasFormatted = totalKasFormatted,
                cashTransactions = cashTransactions,
                poskoCount = poskoCount,
                visualTheme = visualTheme,
                onNavigateTab = onNavigateTab,
                modifier = Modifier.testTag("community_live_status_card")
            )
        } else {
            DashboardAdminFocusCard(
                kasFormatted = totalKasFormatted,
                memberCount = members.size,
                verifiedCount = verifiedCount,
                transactionCount = cashTransactions.size,
                visualTheme = visualTheme,
                onNavigateTab = onNavigateTab,
                modifier = Modifier.testTag("dashboard_admin_focus_card")
            )
        }
    }
}
