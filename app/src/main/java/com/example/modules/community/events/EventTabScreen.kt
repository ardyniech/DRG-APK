package com.example.modules.community.events

import androidx.compose.runtime.*
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.deleteCommunityEvent
import com.example.core.viewmodel.saveCommunityEvent
import com.example.shared.models.CommunityEvent

@Composable
fun EventTabScreen(
    events: List<CommunityEvent>,
    viewModel: DRGViewModel?
) {
    if (viewModel == null) {
        androidx.compose.material3.Text("Event tidak tersedia (viewModel belum terhubung).")
        return
    }
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var editEvent by remember { mutableStateOf<CommunityEvent?>(null) }

    val selectedId = selectedEventId
    if (selectedId != null) {
        EventDetailScreen(
            eventId = selectedId,
            viewModel = viewModel,
            onBack = { selectedEventId = null },
            onEdit = { editEvent = it },
            onDelete = { viewModel.deleteCommunityEvent(it); selectedEventId = null }
        )
    } else {
        EventListScreen(
            events = events,
            onCreate = { showCreate = true },
            onOpen = { selectedEventId = it.id }
        )
    }

    if (showCreate) {
        EventFormDialog(initial = null, onDismiss = { showCreate = false }, onSave = { e, isNew ->
            viewModel.saveCommunityEvent(e, isNew); showCreate = false
        })
    }
    editEvent?.let { ev ->
        EventFormDialog(initial = ev, onDismiss = { editEvent = null }, onSave = { e, isNew ->
            viewModel.saveCommunityEvent(e, isNew); editEvent = null
        })
    }
}
