package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
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
fun DevDefectsTab() {
    val defects = remember { ProductionDefectScanner.scanKnownDefects() }
    var selectedFilter by remember { mutableStateOf<DefectType?>(null) }
    val filtered = if (selectedFilter == null) defects else defects.filter { it.type == selectedFilter }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.ReportProblem, contentDescription = null, tint = Color(0xFFC62828))
                        Text("Transparansi Cacat Produksi & Konsep Sistem", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    }
                    Text("Audit terbuka tanpa sembunyikan cacat sekecil apapun: kelemahan arsitektur, limitasi P2P, dan kompromi baterai.", fontSize = 11.sp, color = DrgTextSecondary)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(selected = selectedFilter == null, onClick = { selectedFilter = null }, label = { Text("Semua (${defects.size})", fontSize = 10.sp) })
                        DefectType.values().forEach { type ->
                            FilterChip(
                                selected = selectedFilter == type, onClick = { selectedFilter = type },
                                label = { Text(if (type == DefectType.CONCEPT_FLAW) "Cacat Konsep" else "Cacat Produksi", fontSize = 10.sp) }
                            )
                        }
                    }
                }
            }
        }

        items(filtered) { item ->
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFC62828), modifier = Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFEBEE)) {
                            Text(item.severityLevel, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("Komponen Terkena: ${item.affectedComponent}", fontSize = 10.sp, color = Color(0xFF0369A1), fontWeight = FontWeight.SemiBold)
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFF1F2)) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("🚨 Fakta Cacat: ${item.honestDefectExplanation}", fontSize = 10.sp, color = DrgTextPrimary)
                            HorizontalDivider(color = DrgOutline.copy(alpha = 0.2f))
                            Text("🛠️ Rencana Rekayasa: ${item.engineeringRoadmapFix}", fontSize = 10.sp, color = DrgGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
