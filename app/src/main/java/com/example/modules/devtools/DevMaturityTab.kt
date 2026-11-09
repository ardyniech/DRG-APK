package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
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
fun DevMaturityTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    var selectedFilter by remember { mutableStateOf<FeatureStage?>(null) }
    val features = remember { DeadButtonShallowScanner.getAuditedFeatures() }
    val filtered = if (selectedFilter == null) features else features.filter { it.stage == selectedFilter }

    DevTabShell(
        headerContent = headerContent,
        headerIcon = null,
        headerTitle = "🌱 Matriks Kematangan Modul (Auto-Detect)",
        headerDesc = "Klasifikasi ketat: Benih (Konsep), Anak (Incubating), Mature (Stabil), Lulus (Production-grade).",
        headerExtra = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(selected = selectedFilter == null, onClick = { selectedFilter = null }, label = { Text("Semua (${features.size})", fontSize = 10.sp) })
                        FeatureStage.values().forEach { stage ->
                            FilterChip(
                                selected = selectedFilter == stage,
                                onClick = { selectedFilter = stage },
                                label = { Text(stage.label.split(" ").first(), fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(stage.badgeColorHex), selectedLabelColor = Color(stage.textColorHex))
                            )
                        }
                    }
        }
    ) {

        items(filtered) { item ->
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                            Text("Modul: ${item.module} • ${item.codeLineCount} baris", fontSize = 10.sp, color = DrgTextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(item.stage.badgeColorHex)) {
                            Text(item.stage.label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(item.stage.textColorHex), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    if (item.hasDeadButtons && item.deadButtonDetails != null) {
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFEBEE)) {
                            Row(modifier = Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = DrgRedDanger, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Dead Button: ${item.deadButtonDetails}", fontSize = 10.sp, color = Color(0xFFC62828))
                            }
                        }
                    }
                    if (item.isShallow && item.shallowReason != null) {
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFF8E1)) {
                            Row(modifier = Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = DrgAmberWarning, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Fitur Dangkal: ${item.shallowReason}", fontSize = 10.sp, color = Color(0xFFF57F17))
                            }
                        }
                    }
                }
            }
        }
    }
}
