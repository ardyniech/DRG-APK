package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
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
    val theme = LocalAppVisualTheme.current
    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
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
                Icon(Icons.Default.Settings, contentDescription = "Pengaturan", tint = theme.primaryColor)
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(notifications) { n ->
                val accentColor = when (n.severity) {
                    NotificationSeverity.EMERGENCY, NotificationSeverity.DANGER -> DrgRedPanic
                    NotificationSeverity.WARNING -> DrgAmberSecondary
                    NotificationSeverity.SUCCESS -> theme.primaryColor
                    NotificationSeverity.INFO -> DrgIndigoAccent
                }
                DrgLuminousGlassCard(
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = theme.glassBg,
                    borderHighlightColor = accentColor.copy(alpha = 0.6f),
                    contentPadding = 14.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(n.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                            Text(n.timeAgo, fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextMuted)
                        }
                        Text(n.message, fontSize = 11.sp, color = DrgTextDark)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Pengirim: ${n.senderName}", fontSize = 10.sp, color = DrgTextSecondary)
                            Spacer(Modifier.width(6.dp))
                            RoleBadge(role = n.senderRole)
                        }
                    }
                }
            }
        }
    }
}
