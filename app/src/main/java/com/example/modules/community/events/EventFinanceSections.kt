package com.example.modules.community.events

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
fun EventFundingSection(eventId: String, funding: List<EventFunding>, vm: DRGViewModel) {
    var showType by remember { mutableStateOf<EventFundingType?>(null) }
    val sumber = funding.filter { it.type == EventFundingType.SUMBER }
    val keluar = funding.filter { it.type == EventFundingType.KELUAR }
    val totalIn = sumber.sumOf { it.amount }
    val totalOut = keluar.sumOf { it.amount }

    SectionCard("Dana Sumber (Masuk)", onAdd = { showType = EventFundingType.SUMBER }) {
        Text("Total: Rp %,d".format(totalIn), fontWeight = FontWeight.SemiBold, color = DrgGrabGreenDark, fontSize = 12.sp)
        if (sumber.isEmpty()) Text("Belum ada dana sumber.", fontSize = 12.sp, color = DrgTextSecondary)
        sumber.forEach { FundingRow(it) { vm.deleteEventFunding(it) } }
    }
    Spacer(Modifier.height(4.dp))
    SectionCard("Dana Keluar", onAdd = { showType = EventFundingType.KELUAR }) {
        Text("Total: Rp %,d".format(totalOut), fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        if (keluar.isEmpty()) Text("Belum ada dana keluar.", fontSize = 12.sp, color = DrgTextSecondary)
        keluar.forEach { FundingRow(it) { vm.deleteEventFunding(it) } }
    }
    Text("Saldo event (sumber - keluar): Rp %,d".format(totalIn - totalOut), fontWeight = FontWeight.Bold, fontSize = 12.sp)
    showType?.let { t -> FundingDialog(eventId, t, onDismiss = { showType = null }) { a,c,s,d,dt,p -> vm.saveEventFunding(eventId,t,a,c,s,d,dt,p); showType = null } }
}

@Composable
private fun FundingRow(f: EventFunding, onDelete: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("${f.category.ifBlank { f.type.label }} • Rp %,d".format(f.amount), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            val info = listOf(f.sourceOrRecipient, f.dateText, f.description).filter { it.isNotBlank() }.joinToString(" • ")
            if (info.isNotBlank()) Text(info, fontSize = 11.sp, color = DrgTextSecondary)
            if (f.proofNote.isNotBlank()) Text("Bukti/catatan: ${f.proofNote}", fontSize = 11.sp, color = DrgTextSecondary)
        }
        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Hapus", tint = MaterialTheme.colorScheme.error) }
    }
}

@Composable
private fun FundingDialog(eventId: String, type: EventFundingType, onDismiss: () -> Unit, onSave: (Long,String,String,String,String,String)->Unit) {
    var amount by remember { mutableStateOf("") }; var cat by remember { mutableStateOf("") }
    var src by remember { mutableStateOf("") }; var desc by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }; var proof by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (type==EventFundingType.SUMBER) "Tambah Dana Sumber" else "Tambah Dana Keluar") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedTextField(amount, { amount = it.filter(Char::isDigit) }, label = { Text("Nominal (Rp)*") }, singleLine = true)
            OutlinedTextField(cat, { cat = it }, label = { Text("Kategori (iuran/donasi/sponsor/konsumsi/dll)") })
            OutlinedTextField(src, { src = it }, label = { Text(if (type==EventFundingType.SUMBER) "Sumber/Dari" else "Penerima/Ke") })
            OutlinedTextField(date, { date = it }, label = { Text("Tanggal") }, singleLine = true)
            OutlinedTextField(desc, { desc = it }, label = { Text("Keterangan") })
            OutlinedTextField(proof, { proof = it }, label = { Text("Catatan bukti/nota") })
        }
    }, confirmButton = { TextButton(onClick = { val a = amount.toLongOrNull() ?: 0L; if (a>0) onSave(a,cat.trim(),src.trim(),desc.trim(),date.trim(),proof.trim()) }) { Text("Simpan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } })
}

@Composable
fun EventLpjSection(eventId: String, funding: List<EventFunding>, lpj: EventLpjReport?, vm: DRGViewModel) {
    var show by remember { mutableStateOf(false) }
    val totalIn = funding.filter { it.type == EventFundingType.SUMBER }.sumOf { it.amount }
    val totalOut = funding.filter { it.type == EventFundingType.KELUAR }.sumOf { it.amount }
    SectionCard("LPJ (Laporan Pertanggungjawaban)", onAdd = { show = true }) {
        Text("Ringkasan dari dana tercatat: Masuk Rp %,d • Keluar Rp %,d • Saldo Rp %,d".format(totalIn,totalOut,totalIn-totalOut), fontSize = 12.sp, color = DrgTextSecondary)
        if (lpj == null) Text("Belum ada LPJ. Tambahkan ringkasan & status LPJ.", fontSize = 12.sp, color = DrgTextSecondary)
        else {
            Text("Status: ${lpj.status.label}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            if (lpj.summary.isNotBlank()) Text(lpj.summary, fontSize = 12.sp)
            Text("LPJ tercatat: Masuk Rp %,d • Keluar Rp %,d • Saldo Rp %,d".format(lpj.incomeTotal, lpj.expenseTotal, lpj.balance), fontSize = 12.sp)
            if (lpj.notes.isNotBlank()) Text("Catatan: ${lpj.notes}", fontSize = 11.sp, color = DrgTextSecondary)
            if (lpj.submittedBy.isNotBlank()) Text("Oleh: ${lpj.submittedBy}", fontSize = 11.sp, color = DrgTextSecondary)
        }
    }
    if (show) LpjDialog(eventId, lpj, totalIn, totalOut, onDismiss = { show = false }) { status, summary, notes ->
        vm.saveEventLpj(eventId, status, summary, totalIn, totalOut, notes, lpj?.id); show = false
    }
}

@Composable
private fun LpjDialog(eventId: String, current: EventLpjReport?, income: Long, expense: Long, onDismiss: () -> Unit, onSave: (EventLpjStatus,String,String)->Unit) {
    var status by remember { mutableStateOf(current?.status ?: EventLpjStatus.DRAFT) }
    var summary by remember { mutableStateOf(current?.summary ?: "") }
    var notes by remember { mutableStateOf(current?.notes ?: "") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Simpan LPJ") }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Masuk Rp %,d • Keluar Rp %,d • Saldo Rp %,d".format(income,expense,income-expense), fontSize = 12.sp)
            Text("Status: ${status.label}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { FilterChip(status==EventLpjStatus.DRAFT,{status=EventLpjStatus.DRAFT},{Text("Draft")}); FilterChip(status==EventLpjStatus.DIAJUKAN,{status=EventLpjStatus.DIAJUKAN},{Text("Diajukan")}) }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { FilterChip(status==EventLpjStatus.DISETUJUI,{status=EventLpjStatus.DISETUJUI},{Text("Disetujui")}); FilterChip(status==EventLpjStatus.REVISI,{status=EventLpjStatus.REVISI},{Text("Revisi")}) }
            OutlinedTextField(summary, { summary = it }, label = { Text("Ringkasan LPJ") }, minLines = 2)
            OutlinedTextField(notes, { notes = it }, label = { Text("Catatan") })
        }
    }, confirmButton = { TextButton(onClick = { onSave(status, summary.trim(), notes.trim()) }) { Text("Simpan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } })
}
