package com.example.modules.events

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.events.logic.EventReadinessCalculator
import com.example.modules.events.logic.EventRiskLevel
import com.example.modules.events.primitives.*
import com.example.shared.atoms.DrgLuminousAtmosphere
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.*
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

@Composable
fun CommunityEventsScreen(
    events: List<CommunityEventEntity>,
    tasks: List<EventCommitteeTaskEntity>,
    feedbackList: List<EventMemberFeedbackEntity>,
    currentMember: DriverMember?,
    visualTheme: AppVisualTheme = AppVisualTheme.EMERALD_AURORA,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onBack: () -> Unit,
    onCreateEvent: (String, String, String, String, Long, String, String, String, Long, Long, Int, String, List<EventCommitteeTaskEntity>) -> Unit,
    onUpdateEventStage: (String, EventPhaseStage, String) -> Unit,
    onAddTask: (String, String, String, String, String, Long) -> Unit,
    onUpdateTask: (String, Int, CommitteeTaskStatus, String) -> Unit,
    onSubmitFeedback: (String, String, String, String) -> Unit,
    onResolveFeedback: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    var showCreateModal by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableIntStateOf(0) }

    val reports = remember(events, tasks, feedbackList) {
        events.associate { ev ->
            ev.id to EventReadinessCalculator.analyze(ev, tasks.filter { it.eventId == ev.id }, feedbackList.filter { it.eventId == ev.id })
        }
    }
    val filteredEvents = remember(events, reports, selectedFilter) {
        when (selectedFilter) {
            1 -> events.filter { it.stage != EventPhaseStage.SELESAI }
            2 -> events.filter { reports[it.id]?.riskLevel == EventRiskLevel.KRITIS_RAWAN_GAGAL }
            3 -> events.filter { it.stage == EventPhaseStage.SELESAI }
            else -> events
        }
    }
    val activeDetail = events.find { it.id == selectedEventId }

    BackHandler {
        if (selectedEventId != null) selectedEventId = null else onBack()
    }

    Box(modifier = modifier.fillMaxSize().testTag("community_events_screen")) {
        DrgLuminousAtmosphere(theme = visualTheme)

        if (activeDetail != null) {
            val evTasks = tasks.filter { it.eventId == activeDetail.id }
            val evFeedback = feedbackList.filter { it.eventId == activeDetail.id }
            val evReport = reports[activeDetail.id] ?: EventReadinessCalculator.analyze(activeDetail, evTasks, evFeedback)
            EventDetailTransparencyView(
                event = activeDetail, report = evReport, tasks = evTasks, feedbackList = evFeedback,
                currentMember = currentMember, visualTheme = visualTheme, contentPadding = contentPadding,
                onBackToList = { selectedEventId = null },
                onUpdateStage = { stage, notes -> onUpdateEventStage(activeDetail.id, stage, notes) },
                onAddTask = { div, title, pic, dl, bud -> onAddTask(activeDetail.id, div, title, pic, dl, bud) },
                onUpdateTask = onUpdateTask,
                onSubmitFeedback = { cat, sev, msg -> onSubmitFeedback(activeDetail.id, cat, sev, msg) },
                onResolveFeedback = onResolveFeedback
            )
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(top = contentPadding.calculateTopPadding() + 10.dp, start = 14.dp, end = 14.dp)) {
                EventGlassHeader(
                    visualTheme = visualTheme, totalEvents = events.size,
                    readyCount = reports.values.count { it.riskLevel == EventRiskLevel.AMAN },
                    blockedTasksCount = tasks.count { it.status == CommitteeTaskStatus.TERKENDALA },
                    openFeedbackCount = feedbackList.count { !it.isResolved },
                    selectedFilter = selectedFilter, onSelectFilter = { selectedFilter = it },
                    onBack = onBack, onCreateEventClick = { showCreateModal = true }
                )
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 104.dp)
                ) {
                    if (filteredEvents.isEmpty()) {
                        item {
                            DrgLuminousGlassCard(shape = RoundedCornerShape(22.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = visualTheme.glassBorderColor, contentPadding = 18.dp, modifier = Modifier.fillMaxWidth()) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Belum ada event di kategori ini", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextDark)
                                    Text("Tekan + Buat Event untuk mencatat rencana acara & pembagian tugas panitia secara transparan.", fontSize = 11.sp, color = DrgTextSecondary)
                                }
                            }
                        }
                    }
                    items(filteredEvents, key = { it.id }) { event ->
                        val report = reports[event.id] ?: EventReadinessCalculator.analyze(event, emptyList(), emptyList())
                        EventTransparencyGlassCard(event = event, report = report, visualTheme = visualTheme, onClick = { selectedEventId = event.id })
                    }
                }
            }
        }

        if (showCreateModal) {
            EventCreateRecordModal(
                currentMember = currentMember, visualTheme = visualTheme,
                onDismiss = { showCreateModal = false },
                onSubmit = { t, cat, desc, dt, ts, loc, cid, cname, rab, fnd, att, notes, draft ->
                    onCreateEvent(t, cat, desc, dt, ts, loc, cid, cname, rab, fnd, att, notes, draft)
                    showCreateModal = false
                }
            )
        }
    }
}
