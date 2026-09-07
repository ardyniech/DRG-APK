package com.example.modules.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun NotificationCenterScreen(
    notifications: List<CommunityNotification>,
    currentMember: DriverMember?,
    onOpenPreferences: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Pusat Notifikasi Komunitas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary)
                Text("Role: ${currentMember?.role?.title ?: "Anggota"}", fontSize = 11.sp, color = DrgTextSecondary)
            }
            IconButton(onClick = onOpenPreferences) {
                Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = DrgGrabGreenPrimary)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(notifications) { n ->
                val cardColor = when (n.severity) {
                    NotificationSeverity.EMERGENCY, NotificationSeverity.DANGER -> DrgRedPanic.copy(alpha = 0.1f)
                    NotificationSeverity.WARNING -> DrgAmberContainer
                    NotificationSeverity.SUCCESS -> DrgEmeraldContainer
                    NotificationSeverity.INFO -> DrgIndigoContainer
                }
                val borderColor = when (n.severity) {
                    NotificationSeverity.EMERGENCY, NotificationSeverity.DANGER -> DrgRedPanic
                    NotificationSeverity.WARNING -> DrgAmberSecondary
                    NotificationSeverity.SUCCESS -> DrgGreenPrimary
                    NotificationSeverity.INFO -> DrgIndigoAccent
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = cardColor,
                    modifier = Modifier.fillMaxWidth().border(1.dp, borderColor, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = n.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                            Text(text = n.timeAgo, fontSize = 10.sp, color = DrgTextMuted)
                        }
                        Text(text = n.message, fontSize = 11.sp, color = DrgTextDark)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Pengirim: ${n.senderName}", fontSize = 10.sp, color = DrgTextSecondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            RoleBadge(role = n.senderRole)
                        }
                    }
                }
            }
        }
    }
}
