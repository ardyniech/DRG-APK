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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LoyaltyProgressIndicator(
    rankProgress: RankProgress,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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

        // Progress Bar & Rank Thresholds
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progres Rank",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextPrimary
                )
                Text(
                    text = "${(rankProgress.progressFraction * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = DrgGreenPrimary
                )
            }

            LinearProgressIndicator(
                progress = { rankProgress.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = DrgGreenPrimary,
                trackColor = DrgGreenContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${rankProgress.minPointsForCurrent} XP (${rankProgress.currentRank})",
                    fontSize = 9.5.sp,
                    color = DrgTextMuted
                )
                if (rankProgress.nextRank != null) {
                    Text(
                        text = "${rankProgress.pointsForNext} XP (${rankProgress.nextRank})",
                        fontSize = 9.5.sp,
                        color = DrgGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Rank Maksimal (Puncak)",
                        fontSize = 9.5.sp,
                        color = DrgGoldReward,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (rankProgress.nextRank != null) {
                Surface(
                    color = DrgGreenContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🎯 Kumpulkan ${rankProgress.pointsNeeded} XP lagi untuk naik ke Rank ${rankProgress.nextRank}!",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DrgGreenDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}
