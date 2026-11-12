package com.example.modules.community.events

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.*
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun EventCommitteeSection(eventId: String, items: List<EventCommitteeMember>, vm: DRGViewModel) {
    var show by remember { mutableStateOf(false) }
    SectionCard("Susunan Panitia", onAdd = { show = true }) {
        if (items.isEmpty()) Text("Belum ada panitia.", fontSize = 12.sp, color = DrgTextSecondary)
        items.forEach { m ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("${m.name} — ${m.role}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    val info = listOf(m.phone, m.responsibility).filter { it.isNotBlank() }.joinToString(" • ")
                    if (info.isNotBlank()) Text(info, fontSize = 11.sp, color = DrgTextSecondary)
                }
                IconButton(onClick = { vm.deleteEventCommittee(m) }) { Icon(Icons.Default.Delete, "Hapus", tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
    if (show) SimpleTwoFieldDialog("Tambah Panitia", "Nama", "Jabatan/Peran", "Kontak (opsional)", "Tanggung jawab", onDismiss = { show = false }) { a, b, c, d ->
        vm.saveEventCommittee(eventId, a, b, c, d); show = false
    }
}

@Composable
fun EventStakeholderSection(eventId: String, items: List<EventStakeholder>, vm: DRGViewModel) {
    var show by remember { mutableStateOf(false) }
    SectionCard("Pihak Terkait / Terlibat", onAdd = { show = true }) {
        if (items.isEmpty()) Text("Belum ada pihak terkait.", fontSize = 12.sp, color = DrgTextSecondary)
        items.forEach { s ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("${s.name} — ${s.role}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    val info = listOf(s.organization, s.contact, s.contribution).filter { it.isNotBlank() }.joinToString(" • ")
                    if (info.isNotBlank()) Text(info, fontSize = 11.sp, color = DrgTextSecondary)
                }
                IconButton(onClick = { vm.deleteEventStakeholder(s) }) { Icon(Icons.Default.Delete, "Hapus", tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
    if (show) SimpleTwoFieldDialog("Tambah Pihak Terkait", "Nama", "Peran (Sponsor/Vendor/dll)", "Organisasi/Kontak", "Kontribusi/Keterangan", onDismiss = { show = false }) { a, b, c, d ->
        vm.saveEventStakeholder(eventId, a, b, c, "", d); show = false
    }
}

@Composable
fun SectionCard(title: String, onAdd: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = DrgSurface)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                IconButton(onClick = onAdd) { Icon(Icons.Default.Add, "Tambah") }
            }
            content()
        }
    }
}

@Composable
fun SimpleTwoFieldDialog(title: String, l1: String, l2: String, l3: String, l4: String, onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var a by remember { mutableStateOf("") }; var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }; var d by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(title) }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(a, { a = it }, label = { Text(l1) }, singleLine = true)
            OutlinedTextField(b, { b = it }, label = { Text(l2) }, singleLine = true)
            OutlinedTextField(c, { c = it }, label = { Text(l3) }, singleLine = true)
            OutlinedTextField(d, { d = it }, label = { Text(l4) })
        }
    }, confirmButton = { TextButton(onClick = { if (a.isNotBlank()) onSave(a.trim(), b.trim(), c.trim(), d.trim()) }) { Text("Simpan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } })
}
