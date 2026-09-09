package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.CommunityNotification
import com.example.shared.models.NotificationSeverity
import com.example.ui.theme.*

@Composable
fun RecentAnnouncementsSection(
    announcements: List<CommunityNotification>,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("recent_announcements_section")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
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

@Composable
private fun AnnouncementRow(
    announcement: CommunityNotification,
    modifier: Modifier = Modifier
) {
    val (icon, color, bg) = when (announcement.severity) {
        NotificationSeverity.EMERGENCY, NotificationSeverity.DANGER -> Triple(Icons.Default.Warning, DrgRedDanger, DrgRedContainer)
        NotificationSeverity.WARNING -> Triple(Icons.Default.Warning, DrgAmberSecondary, DrgAmberContainer)
        NotificationSeverity.SUCCESS -> Triple(Icons.Default.Info, DrgGreenPrimary, DrgGreenContainer)
        else -> Triple(Icons.Default.Notifications, DrgGreenPrimary, DrgGreenContainer)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = announcement.severity.name,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DrgTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = announcement.timeAgo,
                    fontSize = 10.sp,
                    color = DrgTextSecondary,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = announcement.message,
                fontSize = 11.sp,
                color = DrgTextSecondary,
                lineHeight = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Oleh: ${announcement.senderName}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DrgTextPrimary
                )
                RoleBadge(role = announcement.senderRole)
            }
        }
    }
}
