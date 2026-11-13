package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.modules.events.logic.EventReadinessReport
import com.example.modules.events.logic.EventRiskLevel
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EventDetailTransparencyView(
    event: CommunityEventEntity,
    report: EventReadinessReport,
    tasks: List<EventCommitteeTaskEntity>,
    feedbackList: List<EventMemberFeedbackEntity>,
    currentMember: DriverMember?,
    visualTheme: AppVisualTheme,
    contentPadding: PaddingValues,
    onBackToList: () -> Unit,
    onUpdateStage: (EventPhaseStage, String) -> Unit,
    onAddTask: (String, String, String, String, Long) -> Unit,
    onUpdateTask: (String, Int, CommitteeTaskStatus, String) -> Unit,
    onSubmitFeedback: (String, String, String) -> Unit,
    onResolveFeedback: (String, String) -> Unit
) {
    val accent = visualTheme.primaryColor
    val canManage = currentMember != null && (currentMember.role == MemberRole.SUPER_ADMIN || currentMember.role.isLeadership || currentMember.id == event.coordinatorId)
    var subTab by remember { mutableIntStateOf(0) }
    val riskColor = when (report.riskLevel) {
        EventRiskLevel.AMAN -> DrgGreenPrimary; EventRiskLevel.WASPADA -> Color(0xFFD97706); EventRiskLevel.KRITIS_RAWAN_GAGAL -> DrgRedPanic
    }
    val rupiahFmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))

    LazyColumn(
        modifier = Modifier.fillMaxSize().testTag("event_detail_view"),
        contentPadding = PaddingValues(top = contentPadding.calculateTopPadding() + 10.dp, bottom = 104.dp, start = 14.dp, end = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            DrgLuminousGlassCard(shape = RoundedCornerShape(24.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = visualTheme.glassBorderColor, contentPadding = 14.dp, modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = onBackToList, modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(visualTheme.pillBg).testTag("btn_event_detail_back")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = accent)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(event.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                            Text("${event.targetDateText} • ${event.locationName}", fontSize = 10.5.sp, color = DrgTextSecondary)
                        }
                        Text("${report.overallReadinessPct}%", fontFamily = SpaceGroteskFontFamily, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = riskColor)
                    }
                    Text(report.riskHeadline, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = riskColor, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(riskColor.copy(alpha = 0.11f)).padding(8.dp))
                    if (canManage) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(EventPhaseStage.entries) { st ->
                                val active = st == event.stage
                                Box(modifier = Modifier.clip(RoundedCornerShape(9.dp)).background(if (active) accent else Color.White.copy(alpha = 0.72f)).border(1.dp, if (active) accent else Color.White, RoundedCornerShape(9.dp)).bouncyClick { onUpdateStage(st, event.readinessNotes) }.padding(horizontal = 9.dp, vertical = 5.dp)) {
                                    Text(st.label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Progres Panitia (${tasks.size})", "Masukan (${feedbackList.size})", "Info & RAB").forEachIndexed { idx, lbl ->
                    val active = subTab == idx
                    Box(modifier = Modifier.weight(1f).testTag("event_detail_tab_$idx").heightIn(min = 38.dp).clip(RoundedCornerShape(13.dp)).background(if (active) accent else Color.White.copy(alpha = 0.78f)).border(1.dp, if (active) accent else Color.White, RoundedCornerShape(13.dp)).bouncyClick { subTab = idx }.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Text(lbl, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
                    }
                }
            }
        }
        item {
            when (subTab) {
                0 -> EventCommitteeTasksPanel(event.id, tasks, currentMember, event.coordinatorId, visualTheme, onAddTask, onUpdateTask)
                1 -> EventFeedbackPanel(feedbackList, currentMember, event.coordinatorId, visualTheme, onSubmitFeedback, onResolveFeedback)
                else -> DrgLuminousGlassCard(shape = RoundedCornerShape(20.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = visualTheme.glassBorderColor, contentPadding = 14.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Laporan Anggaran & Kesiapan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                        Text("Deskripsi: ${event.description}", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("PJ Panitia: ${event.coordinatorName} • Target Peserta: ${event.targetAttendees} driver", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
                        Text("RAB Dibutuhkan: Rp ${rupiahFmt.format(event.estimatedBudgetRupiah)} • Dana Terkumpul: Rp ${rupiahFmt.format(event.collectedFundsRupiah)} (${report.budgetCoveragePct}%)", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                        if (event.readinessNotes.isNotBlank()) Text("Catatan Mitigasi: ${event.readinessNotes}", fontSize = 11.sp, color = DrgTextDark)
                    }
                }
            }
        }
    }
}
