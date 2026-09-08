package com.example.modules.profile.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LoyaltyRankHeader(rankProgress: RankProgress, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DrgAmberSecondary.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Poin Loyalitas",
                    tint = DrgAmberSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Total Poin Loyalitas",
                    fontSize = 12.sp,
                    color = DrgTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${rankProgress.currentPoints} XP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DrgGreenPrimary
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = DrgGreenContainer,
            modifier = Modifier.border(1.dp, DrgGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = rankProgress.rankBadgeIcon, fontSize = 14.sp)
                Text(
                    text = rankProgress.currentRank,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgGreenPrimary
                )
            }
        }
    }
}
