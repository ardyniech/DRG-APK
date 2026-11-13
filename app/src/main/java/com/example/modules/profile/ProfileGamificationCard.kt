package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.repository.CommunitySeedData
import com.example.modules.profile.primitives.CommunityActivityPointsList
import com.example.modules.profile.primitives.LoyaltyProgressIndicator
import com.example.modules.profile.primitives.LoyaltyRankUtils
import com.example.modules.profile.primitives.ProfileBadgesShowcase
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.BadgeItem
import com.example.shared.models.DriverMember

@Composable
fun GamificationCard(
    member: DriverMember?,
    badges: List<BadgeItem> = emptyList(),
    visualTheme: AppVisualTheme = AppVisualTheme.EMERALD_AURORA
) {
    val points = member?.loyaltyPoints ?: 150
    val kopdarCount = member?.kopdarAttendanceCount ?: 8
    val rankProgress = LoyaltyRankUtils.calculateRankProgress(points)
    val activitySources = LoyaltyRankUtils.getCommunityActivitySources(kopdarCount)
    val activeBadges = if (badges.isNotEmpty()) badges else CommunitySeedData.defaultBadges()

    DrgLuminousGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            LoyaltyProgressIndicator(rankProgress = rankProgress)
            ProfileBadgesShowcase(badges = activeBadges)
            CommunityActivityPointsList(activities = activitySources)
        }
    }
}
