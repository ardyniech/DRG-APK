package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.primitives.GlassCard
import com.example.shared.models.CommunityNotification
import com.example.ui.theme.DrgGreenContainer
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun RecentAnnouncementsSection(
    announcements: List<CommunityNotification>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("recent_announcements_section"),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = Color.White.copy(alpha = 0.82f),
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(DrgGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Pengumuman",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "Informasi & Pengumuman Komunitas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
            }

            if (announcements.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada pengumuman hari ini.",
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                }
            } else {
                announcements.take(3).forEachIndexed { index, item ->
                    AnnouncementRow(announcement = item)
                    if (index < announcements.take(3).size - 1) {
                        HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f), thickness = 1.dp)
                    }
                }
            }
        }
    }
}
