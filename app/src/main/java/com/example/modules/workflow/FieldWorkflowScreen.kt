package com.example.modules.workflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PlayArrow
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
fun FieldWorkflowScreen(
    onBack: () -> Unit,
    onNavigateRadar: () -> Unit = {},
    onTriggerEmergency: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeStageIndex by remember { mutableIntStateOf(0) }
    val stages = WorkflowStagesData.stages
    val currentStage = stages[activeStageIndex.coerceIn(0, stages.lastIndex)]

    Column(modifier = modifier.fillMaxSize().background(DrgBackgroundGradient)) {
        // 1. Header
        Surface(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp), color = DrgSurface, shadowElevation = 3.dp) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DrgTextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("Alur Kerja Kondisi Nyata Lapangan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Siklus Kerja Ojol & Proteksi Nyata DRG (Bukan Gimmick)", fontSize = 10.sp, color = DrgGreenPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // Stage Stepper Chips
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    stages.forEachIndexed { idx, stg ->
                        val isSelected = idx == activeStageIndex
                        FilterChip(
                            selected = isSelected,
                            onClick = { activeStageIndex = idx },
                            label = { Text("Fase ${stg.id}", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
                        )
                    }
                }
            }

            // Visual Road Simulation
            item { WorkflowRoadCanvas(stage = currentStage) }

            // Live Telemetry HUD
            item { WorkflowTelemetryHud(stage = currentStage) }

            // Stage Reality Card
            item { WorkflowStageCard(stage = currentStage) }

            // Action Navigation Buttons
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { activeStageIndex = (activeStageIndex + 1) % stages.size },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.NavigateNext, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tahap Berikut", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            if (currentStage.id == 3) onTriggerEmergency() else onNavigateRadar()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (currentStage.id == 3) DrgRedDanger else DrgGreenPrimary),
                        modifier = Modifier.weight(1.3f).height(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentStage.id == 3) "Simulasi Sinyal TRC" else "Buka Radar Siaga", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
