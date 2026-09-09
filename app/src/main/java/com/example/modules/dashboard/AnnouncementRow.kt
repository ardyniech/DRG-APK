package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.CommunityNotification
import com.example.shared.models.NotificationSeverity
import com.example.ui.theme.DrgAmberContainer
import com.example.ui.theme.DrgAmberSecondary
import com.example.ui.theme.DrgGreenContainer
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgRedContainer
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

@Composable
fun AnnouncementRow(
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
