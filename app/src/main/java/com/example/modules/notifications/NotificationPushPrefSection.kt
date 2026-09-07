package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun NotificationPushPrefSection(
    sosAlerts: Boolean,
    onSosAlertsChange: (Boolean) -> Unit,
    kopdarReminders: Boolean,
    onKopdarRemindersChange: (Boolean) -> Unit,
    kasUpdates: Boolean,
    onKasUpdatesChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Preferensi Notifikasi Push:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Peringatan Darurat SOS", fontSize = 11.sp, color = DrgTextPrimary)
            Switch(checked = sosAlerts, onCheckedChange = onSosAlertsChange)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pengingat Kopdar & Misi", fontSize = 11.sp, color = DrgTextPrimary)
            Switch(checked = kopdarReminders, onCheckedChange = onKopdarRemindersChange)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Update Kas Komunitas", fontSize = 11.sp, color = DrgTextPrimary)
            Switch(checked = kasUpdates, onCheckedChange = onKasUpdatesChange)
        }
    }
}
