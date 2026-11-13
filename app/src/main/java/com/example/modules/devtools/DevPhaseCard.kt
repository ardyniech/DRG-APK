package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
fun DevPhaseCard(phase: DevPhase) {
    var isExpanded by remember { mutableStateOf(phase.hasAnyDefect) }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (phase.hasAnyDefect) DrgRedPanic.copy(alpha = 0.5f) else DrgGreenPrimary.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("FASE ${phase.phaseNumber}: ${phase.title}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    Text(phase.objective, fontSize = 10.sp, color = DrgTextSecondary)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (phase.hasAnyDefect) DrgRoseBorderSoft else DrgGreenLight
                ) {
                    Text(
                        phase.overallStatus.label,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (phase.hasAnyDefect) DrgRedPanic else DrgGreenPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = DrgTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { phase.completionPercentage / 100f },
                modifier = Modifier.fillMaxWidth().height(5.dp),
                color = if (phase.hasAnyDefect) DrgAmberWarning else DrgGreenPrimary,
                trackColor = DrgSlateBorder
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tuntas: ${phase.completedFeatures}/${phase.inScopeFeatures} in-scope (${String.format("%.1f", phase.completionPercentage)}%)", fontSize = 9.sp, color = DrgTextSecondary)
                if (phase.defectFeatures > 0) {
                    Text("⚠️ ${phase.defectFeatures} Cacat Terdeteksi", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgRedPanic)
                }
            }
            if (phase.deferredFeatures > 0) {
                Text("⏸️ ${phase.deferredFeatures} fitur deferred (di luar scope, tidak dihitung)", fontSize = 9.sp, color = DrgTextSecondary)
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                phase.subPhases.forEach { sub ->
                    DevSubPhaseSection(sub = sub)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}
