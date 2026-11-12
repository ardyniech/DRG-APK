package com.example.modules.devtools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevProgressTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val phases = remember { PhaseTreeScanner.scanFullTree() }
    val (total, completed, defect) = remember { PhaseTreeScanner.getTotalMetrics() }
    val progressPct = if (total > 0) (completed.toFloat() / total) * 100f else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (headerContent != null) {
            item { headerContent() }
        }
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (defect > 0) DrgRoseBgSoft else DrgGreenLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🌳 Hierarki Fase Dev & Sub-Fitur (Progres Jujur)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (defect > 0) DrgRedPanic else DrgGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Total: $total Sub-Fitur | Lulus: $completed | Cacat: $defect | ${String.format("%.1f", progressPct)}% (dari fitur lulus). PENDING/DEFERRED tidak dihitung cacat in-scope.",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Catatan: status selaras roadmap/scope; fitur deferred (Asuransi/Intercom) ditandai PENDING, bukan cacat menyesatkan.",
                        fontSize = 9.sp,
                        color = DrgTextSecondary
                    )
                }
            }
        }

        items(phases) { phase ->
            DevPhaseCard(phase)
        }
    }
}
