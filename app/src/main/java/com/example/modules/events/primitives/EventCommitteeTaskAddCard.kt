package com.example.modules.events.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard

@Composable
fun EventCommitteeTaskAddCard(
    accent: Color,
    defaultPic: String,
    onSave: (division: String, title: String, pic: String, deadline: String, budget: Long) -> Unit
) {
    var division by remember { mutableStateOf("Sie Acara") }
    var title by remember { mutableStateOf("") }
    var pic by remember { mutableStateOf(defaultPic) }
    var deadline by remember { mutableStateOf("H-2") }
    var budget by remember { mutableStateOf("200000") }

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0xECFFFFFF),
        borderHighlightColor = accent,
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(value = division, onValueChange = { division = it }, label = { Text("Divisi", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Deadline", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(0.7f))
            }
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("Tugas Konkret Panitia *", fontSize = 10.sp) },
                singleLine = true, modifier = Modifier.fillMaxWidth().testTag("input_committee_task_title")
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(value = pic, onValueChange = { pic = it }, label = { Text("PIC Pengurus", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(value = budget, onValueChange = { budget = it.filter { c -> c.isDigit() } }, label = { Text("Anggaran Rp", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(0.8f))
            }
            Button(
                onClick = { if (title.isNotBlank()) { onSave(division, title, pic, deadline, budget.toLongOrNull() ?: 0L); title = "" } },
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Simpan Tugas Panitia", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
        }
    }
}
