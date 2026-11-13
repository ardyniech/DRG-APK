package com.example.modules.dashboard.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.shared.models.KasTransaction

/**
 * 2x2 Frosted Glass Bento Deck Layout Container
 */
@Composable
fun BentoGridDeck(
    activeMemberCount: Int,
    hazardCount: Int,
    kasFormatted: String,
    cashTransactions: List<KasTransaction>,
    poskoCount: Int,
    visualTheme: DashboardVisualTheme,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(142.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BentoRadarCanvasWidget(
                activeMemberCount = activeMemberCount,
                hazardCount = hazardCount,
                visualTheme = visualTheme,
                onClick = { onNavigateTab(MainNavTab.RADAR) },
                modifier = Modifier.weight(1f)
            )
            BentoKasWidget(
                kasFormatted = kasFormatted,
                cashTransactions = cashTransactions,
                visualTheme = visualTheme,
                onClick = { onNavigateTab(MainNavTab.KAS) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().height(126.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BentoPoskoWidget(
                poskoCount = poskoCount,
                visualTheme = visualTheme,
                onClick = { onNavigateTab(MainNavTab.RADAR) },
                modifier = Modifier.weight(1f)
            )
            BentoIntercomWidget(
                visualTheme = visualTheme,
                onClick = { onNavigateTab(MainNavTab.PROFILE) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
