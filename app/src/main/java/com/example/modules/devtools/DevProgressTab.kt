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
    val metrics = remember { PhaseTreeScanner.getMetrics() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (headerContent != null) {
            item { headerContent() }
        }
        item {
            com.example.shared.atoms.DrgLuminousGlassCard(
                shape = RoundedCornerShape(22.dp),
                backgroundColor = if (metrics.defectFeatures > 0) Color(0xDDFEE2E2) else Color(0xDCFFFFFF),
                borderHighlightColor = if (metrics.defectFeatures > 0) DrgRedPanic else DrgGreenPrimary,
                contentPadding = 14.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Hierarki Fase Dev & Sub-Fitur (Progres Kurasi)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (metrics.defectFeatures > 0) DrgRedPanic else DrgGreenPrimary
                    )
                    Text(
                        "Total: ${metrics.totalFeatures} Sub-Fitur | In-scope: ${metrics.inScopeFeatures} | Lulus: ${metrics.completedFeatures} | Cacat: ${metrics.defectFeatures}",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextDark
                    )
                    Text(
                        "Dikerjakan: ${metrics.inProgressFeatures} | Deferred: ${metrics.deferredFeatures} | Progres in-scope: ${String.format("%.1f", metrics.progressPercentage)}%",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextDark
                    )
                    Text(
                        "Progres = Lulus ÷ fitur in-scope (deferred dikecualikan). Ini metrik kurasi status PhaseTree — bukan runtime/build/produksi, dan bukan skor kesehatan di banner.",
                        fontSize = 10.sp,
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
