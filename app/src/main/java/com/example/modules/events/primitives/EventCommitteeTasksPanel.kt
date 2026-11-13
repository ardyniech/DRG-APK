package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
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
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

@Composable
fun EventCommitteeTasksPanel(
    eventId: String,
    tasks: List<EventCommitteeTaskEntity>,
    currentMember: DriverMember?,
    coordinatorId: String,
    visualTheme: AppVisualTheme,
    onAddTask: (String, String, String, String, Long) -> Unit,
    onUpdateTask: (String, Int, CommitteeTaskStatus, String) -> Unit
) {
    val accent = visualTheme.primaryColor
    val canManage = currentMember != null && (currentMember.role == MemberRole.SUPER_ADMIN || currentMember.role.isLeadership || currentMember.id == coordinatorId)
    var showAddForm by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (canManage) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.testTag("btn_toggle_add_task").fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(accent.copy(alpha = 0.14f)).border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(14.dp)).bouncyClick { showAddForm = !showAddForm }.padding(vertical = 9.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (showAddForm) "Tutup Form Tugas Panitia" else "Tambah Tugas Divisi Panitia", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
            }
        }
        if (showAddForm && canManage) {
            EventCommitteeTaskAddCard(accent, currentMember?.name ?: "Panitia") { div, t, p, dl, bud ->
                onAddTask(div, t, p, dl, bud)
                showAddForm = false
            }
        }

        if (tasks.isEmpty()) {
            DrgLuminousGlassCard(shape = RoundedCornerShape(18.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = DrgRedPanic.copy(alpha = 0.45f), contentPadding = 16.dp) {
                Text("Belum ada pembagian tugas panitia pada event $eventId. Pengurus wajib merinci tugas tiap divisi agar anggota bisa memantau.", fontSize = 11.sp, color = DrgRedPanic, fontWeight = FontWeight.SemiBold)
            }
        }
        tasks.forEach { task ->
            val canEditTask = canManage || currentMember?.id == task.picMemberId
            val statusColor = when (task.status) {
                CommitteeTaskStatus.SELESAI -> DrgGreenPrimary
                CommitteeTaskStatus.DIKERJAKAN -> Color(0xFF0284C7)
                CommitteeTaskStatus.TERKENDALA -> DrgRedPanic
                CommitteeTaskStatus.BELUM_MULAI -> DrgTextSecondary
            }
            var noteDraft by remember(task.id, task.blockerNote) { mutableStateOf(task.blockerNote) }
            DrgLuminousGlassCard(shape = RoundedCornerShape(20.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = if (task.status == CommitteeTaskStatus.TERKENDALA) DrgRedPanic.copy(alpha = 0.6f) else visualTheme.glassBorderColor, contentPadding = 12.dp, modifier = Modifier.fillMaxWidth().testTag("task_card_${task.id}")) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("${task.divisionName} • ${task.deadlineText}", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
                        Text(task.status.label, fontFamily = SpaceGroteskFontFamily, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = statusColor, modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(statusColor.copy(alpha = 0.14f)).padding(horizontal = 7.dp, vertical = 2.dp))
                    }
                    Text(task.taskTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    Text("PIC: ${task.picMemberName} • Pos Anggaran: Rp ${task.budgetAllocatedRupiah}", fontSize = 10.sp, color = DrgTextSecondary)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LinearProgressIndicator(progress = { task.progressPercent / 100f }, modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(4.dp)), color = statusColor, trackColor = statusColor.copy(alpha = 0.15f))
                        Text("${task.progressPercent}%", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = statusColor)
                    }
                    if (task.blockerNote.isNotBlank()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(statusColor.copy(alpha = 0.09f)).padding(7.dp)) {
                            if (task.status == CommitteeTaskStatus.TERKENDALA) Icon(Icons.Default.Warning, contentDescription = null, tint = DrgRedPanic, modifier = Modifier.size(13.dp))
                            Text(task.blockerNote, fontSize = 10.sp, color = DrgTextDark)
                        }
                    }
                    if (canEditTask) {
                        OutlinedTextField(value = noteDraft, onValueChange = { noteDraft = it }, label = { Text("Update Catatan / Hambatan Nyata", fontSize = 9.5.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TaskQuickActionChip("50% Jalan", Color(0xFF0284C7), Modifier.weight(1f)) { onUpdateTask(task.id, 50, CommitteeTaskStatus.DIKERJAKAN, noteDraft) }
                            TaskQuickActionChip("Terkendala", DrgRedPanic, Modifier.weight(1f)) { onUpdateTask(task.id, task.progressPercent, CommitteeTaskStatus.TERKENDALA, noteDraft.ifBlank { "Ada hambatan, perlu dukungan!" }) }
                            TaskQuickActionChip("100% Selesai", DrgGreenPrimary, Modifier.weight(1f).testTag("btn_complete_task_${task.id}")) { onUpdateTask(task.id, 100, CommitteeTaskStatus.SELESAI, noteDraft) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskQuickActionChip(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Box(modifier = modifier.heightIn(min = 36.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.14f)).border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(10.dp)).bouncyClick { onClick() }.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
