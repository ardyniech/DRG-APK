package com.example.modules.community.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.CommunityEvent
import com.example.ui.theme.*

@Composable
fun EventListScreen(
    events: List<CommunityEvent>,
    onCreate: () -> Unit,
    onOpen: (CommunityEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Event Komunitas", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DrgTextPrimary)
                Text("Kopdar 3 bulanan, anniversary, dll. Kelola panitia, dana, LPJ, dan pihak terkait.", fontSize = 11.sp, color = DrgTextSecondary)
            }
            FilledTonalButton(onClick = onCreate) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(4.dp)); Text("Buat") }
        }
        if (events.isEmpty()) {
            Card(colors = CardDefaults.cardColors(containerColor = DrgSurface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Event, null, tint = DrgGrabGreenPrimary)
                    Text("Belum ada event.", fontWeight = FontWeight.SemiBold)
                    Text("Buat event untuk kelola panitia, dana sumber/keluar, LPJ, dan pihak terkait secara jelas.", fontSize = 12.sp, color = DrgTextSecondary)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(events, key = { it.id }) { e ->
                    Card(onClick = { onOpen(e) }, colors = CardDefaults.cardColors(containerColor = DrgSurface)) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(e.title, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                            Text("${e.type.label} • ${e.status.label}", fontSize = 11.sp, color = DrgGrabGreenDark)
                            val info = listOf(e.scheduleNote, e.dateText, e.location).filter { it.isNotBlank() }.joinToString(" • ")
                            if (info.isNotBlank()) Text(info, fontSize = 11.sp, color = DrgTextSecondary)
                            if (e.description.isNotBlank()) Text(e.description, fontSize = 12.sp, color = DrgTextSecondary, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}
