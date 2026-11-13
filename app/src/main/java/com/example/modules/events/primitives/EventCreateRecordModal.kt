package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.modules.events.logic.EventReadinessCalculator
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.EventCommitteeTaskEntity
import com.example.ui.theme.*

@Composable
fun EventCreateRecordModal(
    currentMember: DriverMember?,
    visualTheme: AppVisualTheme,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, Long, String, String, String, Long, Long, Int, String, List<EventCommitteeTaskEntity>) -> Unit
) {
    val accent = visualTheme.primaryColor
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Kopdar") }
    var dateText by remember { mutableStateOf("Sabtu, 29 Nov 2026 • 19:00 WIB") }
    var daysAhead by remember { mutableIntStateOf(7) }
    var location by remember { mutableStateOf("") }
    var coordinatorName by remember { mutableStateOf(currentMember?.name ?: "Ketua Panitia DRG") }
    var budgetText by remember { mutableStateOf("2500000") }
    var collectedText by remember { mutableStateOf("1000000") }
    var targetAttendeesText by remember { mutableStateOf("50") }
    var description by remember { mutableStateOf("") }
    var readinessNotes by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var draftTasks by remember {
        mutableStateOf(EventReadinessCalculator.defaultTaskDrafts(currentMember?.id ?: "DRG-001", currentMember?.name ?: "Ketua Panitia"))
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().background(DrgScrimOverlay).padding(14.dp), contentAlignment = Alignment.Center) {
            DrgLuminousGlassCard(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.92f).testTag("event_create_modal"),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = DrgGlassSurfaceBright,
                borderHighlightColor = visualTheme.glassBorderColor,
                contentPadding = 16.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Form Laporan Transparansi Event", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                            Text("Isi detail RAB, jadwal & tugas panitia secara terbuka", fontSize = 10.sp, color = DrgTextSecondary)
                        }
                        IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Tutup", tint = DrgTextDark) }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = visualTheme.glassBorderColor.copy(alpha = 0.5f))

                    Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        validationError?.let {
                            Text(it, fontSize = 11.sp, color = DrgRedPanic, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(DrgRedPanic.copy(alpha = 0.12f)).padding(8.dp))
                        }
                        EventCreateFieldsSection(
                            accent, title, { title = it }, category, { category = it }, location, { location = it },
                            dateText, { dateText = it }, daysAhead, { daysAhead = it }, coordinatorName, { coordinatorName = it },
                            targetAttendeesText, { targetAttendeesText = it }, budgetText, { budgetText = it },
                            collectedText, { collectedText = it }, description, { description = it }, readinessNotes, { readinessNotes = it }
                        )
                        EventCreateTaskDraftSection(draftTasks, accent, currentMember?.id ?: "DRG-001", coordinatorName, { draftTasks = draftTasks + it }, { id -> draftTasks = draftTasks.filterNot { it.id == id } })
                    }

                    Button(
                        onClick = {
                            val rab = budgetText.toLongOrNull() ?: 0L
                            val funds = collectedText.toLongOrNull() ?: 0L
                            val target = targetAttendeesText.toIntOrNull() ?: 0
                            when {
                                title.isBlank() || location.isBlank() -> validationError = "Nama Event dan Lokasi wajib diisi."
                                rab <= 0L -> validationError = "Estimasi RAB harus lebih besar dari Rp 0."
                                target <= 0 -> validationError = "Target peserta minimal 1 anggota."
                                else -> onSubmit(title, category, description, dateText, System.currentTimeMillis() + daysAhead * 86_400_000L, location, currentMember?.id ?: "DRG-001", coordinatorName, rab, funds, target, readinessNotes, draftTasks)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp).testTag("btn_submit_event_record")
                    ) { Text("Publikasikan Event & Laporan Panitia", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
