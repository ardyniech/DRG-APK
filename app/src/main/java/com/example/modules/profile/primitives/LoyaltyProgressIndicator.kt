package com.example.modules.profile.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoyaltyProgressIndicator(
    rankProgress: RankProgress,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LoyaltyRankHeader(rankProgress = rankProgress)
        LoyaltyProgressBar(rankProgress = rankProgress)
    }
}
