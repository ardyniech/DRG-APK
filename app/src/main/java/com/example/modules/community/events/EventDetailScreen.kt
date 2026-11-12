package com.example.modules.community.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.observeCommunityEvent
import com.example.core.viewmodel.eventCommittee
import com.example.core.viewmodel.eventFunding
import com.example.core.viewmodel.eventLpj
import com.example.core.viewmodel.eventStakeholders
import com.example.shared.models.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: DRGViewModel,
    onBack: () -> Unit,
    onEdit: (CommunityEvent) -> Unit,
    onDelete: (CommunityEvent) -> Unit
) {
    val event by viewModel.observeCommunityEvent(eventId).collectAsState(initial = null)
    val committee by viewModel.eventCommittee(eventId).collectAsState(initial = emptyList())
    val funding by viewModel.eventFunding(eventId).collectAsState(initial = emptyList())
    val lpj by viewModel.eventLpj(eventId).collectAsState(initial = null)
    val stakeholders by viewModel.eventStakeholders(eventId).collectAsState(initial = emptyList())

    val e = event ?: run {
        Scaffold(topBar = { TopAppBar(title = { Text("Event") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }) }) { p ->
            Box(Modifier.padding(p).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { Text("Event tidak ditemukan / dimuat...") }
        }
        return
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(e.title, maxLines = 1) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Kembali") } },
            actions = {
                IconButton(onClick = { onEdit(e) }) { Icon(Icons.Default.Edit, "Edit") }
                IconButton(onClick = { onDelete(e); onBack() }) { Icon(Icons.Default.Delete, "Hapus") }
            }
        )
    }) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = DrgSurface)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${e.type.label} • ${e.status.label}", fontWeight = FontWeight.SemiBold, color = DrgGrabGreenDark)
                    if (e.scheduleNote.isNotBlank()) Text("Jadwal: ${e.scheduleNote}", fontSize = 12.sp)
                    if (e.dateText.isNotBlank() || e.timeText.isNotBlank()) Text("Waktu: ${e.dateText} ${e.timeText}".trim(), fontSize = 12.sp)
                    if (e.location.isNotBlank()) Text("Lokasi: ${e.location}", fontSize = 12.sp)
                    if (e.description.isNotBlank()) Text(e.description, fontSize = 12.sp, color = DrgTextSecondary)
                }
            }
            EventCommitteeSection(eventId, committee, viewModel)
            EventFundingSection(eventId, funding, viewModel)
            EventLpjSection(eventId, funding, lpj, viewModel)
            EventStakeholderSection(eventId, stakeholders, viewModel)
        }
    }
}
