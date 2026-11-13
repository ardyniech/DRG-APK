package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.EventCommitteeTaskEntity
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun EventCreateTaskDraftSection(
    draftTasks: List<EventCommitteeTaskEntity>,
    accentColor: Color,
    defaultPicId: String,
    defaultPicName: String,
    onAddTask: (EventCommitteeTaskEntity) -> Unit,
    onRemoveTask: (String) -> Unit
) {
    var division by remember { mutableStateOf("Sie Acara") }
    var title by remember { mutableStateOf("") }
    var picName by remember { mutableStateOf(defaultPicName) }
    var deadline by remember { mutableStateOf("H-3") }
    var budgetText by remember { mutableStateOf("300000") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Pembagian Tugas Panitia (${draftTasks.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        draftTasks.forEach { t ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.78f)).border(1.dp, Color.White, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${t.divisionName} • ${t.deadlineText}", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    Text(t.taskTitle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
                    Text("PIC: ${t.picMemberName} • Pos: Rp ${t.budgetAllocatedRupiah}", fontSize = 10.sp, color = DrgTextSecondary)
                }
                IconButton(onClick = { onRemoveTask(t.id) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Hapus Tugas", tint = DrgRedPanic, modifier = Modifier.size(16.dp))
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(accentColor.copy(alpha = 0.08f)).padding(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(value = division, onValueChange = { division = it }, label = { Text("Divisi", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Target H-", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(0.8f))
            }
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Nama Tugas Panitia", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth().testTag("input_draft_task_title"))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedTextField(value = picName, onValueChange = { picName = it }, label = { Text("PIC Panitia", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(value = budgetText, onValueChange = { budgetText = it.filter { ch -> ch.isDigit() } }, label = { Text("Biaya Rp", fontSize = 10.sp) }, singleLine = true, modifier = Modifier.weight(0.9f))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.testTag("btn_add_draft_task").fillMaxWidth().heightIn(min = 40.dp).clip(RoundedCornerShape(11.dp)).background(accentColor.copy(alpha = 0.16f)).bouncyClick {
                    if (title.isNotBlank()) {
                        onAddTask(
                            EventCommitteeTaskEntity(
                                id = "EVTK-${UUID.randomUUID().toString().take(8)}", eventId = "",
                                divisionName = division.ifBlank { "Sie Umum" }, taskTitle = title.trim(),
                                picMemberId = defaultPicId, picMemberName = picName.ifBlank { defaultPicName },
                                deadlineText = deadline.ifBlank { "H-2" }, budgetAllocatedRupiah = budgetText.toLongOrNull() ?: 0L
                            )
                        )
                        title = ""
                    }
                }.padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.AddTask, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Tambah Tugas Panitia ke Daftar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor)
            }
        }
    }
}
