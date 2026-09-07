package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    var taskAssignments by remember { mutableStateOf(initialPref.taskAssignments) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Pengaturan Preferensi Notifikasi & Alarm", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DrgTextPrimary)
                Text("Personalisasi notifikasi sesuai kebutuhan peranan Anda di jalanan.", fontSize = 11.sp, color = DrgTextSecondary)

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("🔊 Sirine SOS Desibel Tinggi (100 dB)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextPrimary)
                        Text("Alarm audio keras saat tombol SOS ditekan", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                    Switch(
                        checked = isSosAlarmEnabled,
                        onCheckedChange = onToggleSosAlarm,
                        colors = SwitchDefaults.colors(checkedThumbColor = DrgRedDanger)
                    )
                }

                HorizontalDivider(color = DrgOutline.copy(alpha = 0.4f))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("🚨 Peringatan Keselamatan Real-time SOS", fontSize = 12.sp)
                    Switch(checked = sosAlerts, onCheckedChange = { sosAlerts = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("📅 Pengingat Kopdar & Kegiatan", fontSize = 12.sp)
                    Switch(checked = kopdarReminders, onCheckedChange = { kopdarReminders = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("💰 Pembaruan Laporan Kas Komunitas", fontSize = 12.sp)
                    Switch(checked = kasUpdates, onCheckedChange = { kasUpdates = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("💬 Aktivitas & Solusi Forum Teknis", fontSize = 12.sp)
                    Switch(checked = forumActivity, onCheckedChange = { forumActivity = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("⭐ Notifikasi Apresiasi Poin Rekan", fontSize = 12.sp)
                    Switch(checked = peerPointsNotif, onCheckedChange = { peerPointsNotif = it })
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Batal") }
                    Button(
                        onClick = {
                            onSave(NotificationPreference(initialPref.memberId, sosAlerts, kopdarReminders, kasUpdates, forumActivity, peerPointsNotif, taskAssignments))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Simpan Preferensi")
                    }
                }
            }
        }
    }
}
