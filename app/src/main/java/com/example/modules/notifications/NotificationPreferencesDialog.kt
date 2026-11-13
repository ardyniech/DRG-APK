package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.NotificationPreference
import com.example.ui.theme.*

@Composable
fun NotificationPreferencesDialog(
    initialPref: NotificationPreference,
    onDismiss: () -> Unit,
    onSave: (NotificationPreference) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {}
) {
    var sosAlerts by remember { mutableStateOf(initialPref.sosAlerts) }
    var kopdarReminders by remember { mutableStateOf(initialPref.kopdarReminders) }
    var kasUpdates by remember { mutableStateOf(initialPref.kasUpdates) }
    var forumActivity by remember { mutableStateOf(initialPref.forumActivity) }
    var peerPointsNotif by remember { mutableStateOf(initialPref.peerPointsNotif) }
    val taskAssignments by remember { mutableStateOf(initialPref.taskAssignments) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Preferensi Notifikasi & Alarm",
        subtitle = "Personalisasi sesuai kebutuhan operasional jalanan",
        icon = Icons.Default.NotificationsActive,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = { onSave(NotificationPreference(initialPref.memberId, sosAlerts, kopdarReminders, kasUpdates, forumActivity, peerPointsNotif, taskAssignments)) },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Simpan Preferensi", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text("🔊 Sirine SOS Desibel Tinggi (100 dB)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary)
                Text("Alarm audio keras saat tombol SOS ditekan", fontSize = 10.sp, color = DrgTextSecondary)
            }
            Switch(checked = isSosAlarmEnabled, onCheckedChange = onToggleSosAlarm, colors = SwitchDefaults.colors(checkedThumbColor = DrgRedDanger))
        }
        HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))
        PrefToggleRow("🚨 Peringatan Keselamatan Real-time SOS", sosAlerts) { sosAlerts = it }
        PrefToggleRow("📅 Pengingat Kopdar & Kegiatan", kopdarReminders) { kopdarReminders = it }
        PrefToggleRow("💰 Pembaruan Laporan Kas Komunitas", kasUpdates) { kasUpdates = it }
        PrefToggleRow("💬 Aktivitas & Solusi Forum Teknis", forumActivity) { forumActivity = it }
        PrefToggleRow("⭐ Notifikasi Apresiasi Poin Rekan", peerPointsNotif) { peerPointsNotif = it }
    }
}

@Composable
private fun PrefToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val theme = LocalAppVisualTheme.current
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, color = DrgTextPrimary, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = theme.primaryColor, checkedTrackColor = theme.primaryColor.copy(alpha = 0.35f)))
    }
}
