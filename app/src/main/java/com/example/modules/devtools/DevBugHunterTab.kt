package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevBugHunterTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val score = remember { ProjectHealthScore() }
    val features = remember { DeadButtonShallowScanner.getAuditedFeatures() }
    val dead = features.filter { it.hasDeadButtons }
    val shallow = features.filter { it.isShallow }
    val defects = remember { ProductionDefectScanner.scanKnownDefects() }
    val totalIssues = dead.size + shallow.size + defects.size + score.progressPenaltiesCount

    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.BugReport,
        headerTitle = "Bug, Dead Buttons & Shallow Features Hunter",
        headerDesc = "Deteksi jujur dari scanner: Dead Button (-4%), Dangkal (-3%), Cacat (-2%), Gap progres fitur DALAM_PENGERJAAN non-deferred (-2%)."
    ) {
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = DrgErrorBgSoft, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = DrgRedDanger, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "$totalIssues temuan (Penalti: -${String.format("%.1f", score.totalPenalty)}%)",
                            fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgErrorText
                        )
                        Text(
                            "Skor kesehatan ${String.format("%.1f", score.baseScore)}% → ${String.format("%.1f", score.finalScore)}% (${score.grade}) — heuristik, bukan progres fitur",
                            fontSize = 10.sp, color = DrgErrorTextDark
                        )
                    }
                }
            }
        }
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("⚠️ Rincian Temuan (dari scanner nyata)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    dead.forEach { Text("• [DEAD-BUTTON] ${it.name}: ${it.deadButtonDetails ?: "-"}", fontSize = 10.sp, color = DrgRedDanger) }
                    shallow.filter { !it.hasDeadButtons }.forEach { Text("• [SHALLOW] ${it.name}: ${it.shallowReason ?: "-"}", fontSize = 10.sp, color = DrgOrangeText) }
                    defects.forEach { Text("• [${it.type.label}] ${it.defectId} ${it.title}", fontSize = 10.sp, color = DrgRedDanger) }
                    if (score.progressPenaltiesCount > 0) Text("• [PROGRES] ${score.progressPenaltiesCount} fitur DALAM_PENGERJAAN non-deferred", fontSize = 10.sp, color = DrgWarningTextDark)
                    Text("Catatan: fitur DEFERRED (Asuransi, Intercom Helm) dikecualikan dari gap progres & tidak dianggap cacat in-scope.", fontSize = 9.sp, color = DrgTextSecondary)
                }
            }
        }
    }
}
