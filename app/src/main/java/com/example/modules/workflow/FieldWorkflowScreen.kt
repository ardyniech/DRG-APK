package com.example.modules.workflow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.utils.bouncyClick
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
    val theme = LocalAppVisualTheme.current

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.glassBg)
                    .border(1.2.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                    .bouncyClick { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DrgTextPrimary, modifier = Modifier.size(20.dp)) }
            Column(modifier = Modifier.weight(1f)) {
                Text("Alur Kerja Kondisi Lapangan", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextPrimary)
                Text("Siklus Kerja Ojol & Proteksi Nyata DRG", fontSize = 11.sp, color = theme.primaryColor, fontWeight = FontWeight.SemiBold)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    stages.forEachIndexed { idx, stg ->
                        val selected = idx == activeStageIndex
                        val pillShape = RoundedCornerShape(14.dp)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(pillShape)
                                .background(if (selected) theme.primaryColor else theme.glassBg)
                                .border(1.dp, if (selected) Color.White.copy(alpha = 0.7f) else theme.glassBorderColor.copy(alpha = 0.45f), pillShape)
                                .bouncyClick { activeStageIndex = idx }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Fase ${stg.id}", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selected) Color.White else DrgTextSecondary)
                        }
                    }
                }
            }
            item { WorkflowRoadCanvas(stage = currentStage) }
            item { WorkflowTelemetryHud(stage = currentStage) }
            item { WorkflowStageCard(stage = currentStage) }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { activeStageIndex = (activeStageIndex + 1) % stages.size },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.NavigateNext, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Tahap Berikut", fontSize = 11.sp)
                    }
                    Button(
                        onClick = { if (currentStage.id == 3) onTriggerEmergency() else onNavigateRadar() },
                        colors = ButtonDefaults.buttonColors(containerColor = if (currentStage.id == 3) DrgRedDanger else theme.primaryColor),
                        modifier = Modifier.weight(1.3f).height(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text(if (currentStage.id == 3) "Simulasi Sinyal TRC" else "Buka Radar Siaga", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}
