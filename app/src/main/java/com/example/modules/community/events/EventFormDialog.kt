package com.example.modules.community.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.models.CommunityEvent
import com.example.shared.models.CommunityEventStatus
import com.example.shared.models.CommunityEventType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormDialog(
    initial: CommunityEvent?,
    onDismiss: () -> Unit,
    onSave: (CommunityEvent, Boolean) -> Unit
) {
    val isNew = initial == null
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var type by remember { mutableStateOf(initial?.type ?: CommunityEventType.KOPDAR_RUTIN) }
    var status by remember { mutableStateOf(initial?.status ?: CommunityEventStatus.DRAFT) }
    var schedule by remember { mutableStateOf(initial?.scheduleNote ?: "Tiap 3 bulan") }
    var date by remember { mutableStateOf(initial?.dateText ?: "") }
    var time by remember { mutableStateOf(initial?.timeText ?: "") }
    var location by remember { mutableStateOf(initial?.location ?: "") }
    var desc by remember { mutableStateOf(initial?.description ?: "") }
    var showStatus by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Buat Event" else "Edit Event") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Nama event*") }, singleLine = true)
                Text("Tipe: ${type.label}", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(type == CommunityEventType.KOPDAR_RUTIN, { type = CommunityEventType.KOPDAR_RUTIN }, label = { Text("Kopdar") })
                    FilterChip(type == CommunityEventType.ANNIVERSARY, { type = CommunityEventType.ANNIVERSARY }, label = { Text("Anniv") })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(type == CommunityEventType.BAKTI_SOSIAL, { type = CommunityEventType.BAKTI_SOSIAL }, label = { Text("Baksos") })
                    FilterChip(type == CommunityEventType.LAINNYA, { type = CommunityEventType.LAINNYA }, label = { Text("Lainnya") })
                }
                OutlinedTextField(schedule, { schedule = it }, label = { Text("Jadwal (mis. tiap 3 bulan)") }, singleLine = true)
                OutlinedTextField(date, { date = it }, label = { Text("Tanggal") }, singleLine = true)
                OutlinedTextField(time, { time = it }, label = { Text("Waktu") }, singleLine = true)
                OutlinedTextField(location, { location = it }, label = { Text("Lokasi") }, singleLine = true)
                OutlinedTextField(desc, { desc = it }, label = { Text("Deskripsi") }, minLines = 2)
                TextButton(onClick = { showStatus = !showStatus }) { Text("Status: ${status.label}") }
                if (showStatus) {
                    Column {
                        CommunityEventStatus.entries.forEach { s ->
                            TextButton(onClick = { status = s; showStatus = false }) { Text(s.label) }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isBlank()) return@TextButton
                val e = (initial ?: CommunityEvent(id = "EVT-${UUID.randomUUID().toString().take(8).uppercase()}", title = title)).copy(
                    title = title.trim(), type = type, status = status, scheduleNote = schedule.trim(),
                    dateText = date.trim(), timeText = time.trim(), location = location.trim(), description = desc.trim()
                )
                onSave(e, isNew)
            }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
