package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevRootCauseTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current
    val findings = remember { EarlySymptomDetector.diagnose(context) }
    var selectedSeverity by remember { mutableStateOf<SymptomSeverity?>(null) }
    val filtered = if (selectedSeverity == null) findings else findings.filter { it.severity == selectedSeverity }

    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.Build,
        headerTitle = "Root Cause Analysis & Gejala Awal",
        headerDesc = "Auto-deteksi teknis: Mengenali gejala sebelum user/dev menemukan crash/stuck di lapangan.",
        headerExtra = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(selected = selectedSeverity == null, onClick = { selectedSeverity = null }, label = { Text("Semua (${findings.size})", fontSize = 10.sp) })
                        SymptomSeverity.values().forEach { sev ->
                            FilterChip(
                                selected = selectedSeverity == sev, onClick = { selectedSeverity = sev },
                                label = { Text(sev.label.split(" ").first(), fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(sev.badgeColorHex), selectedLabelColor = Color(sev.textColorHex))
                            )
                        }
                    }
        }
    ) {

        items(filtered) { item ->
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary, modifier = Modifier.weight(1f))
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(item.severity.badgeColorHex)) {
                            Text(item.severity.label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(item.severity.textColorHex), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF1F5F9)) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("🚨 Gejala: ${item.preFailureSymptom}", fontSize = 10.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("📍 Sumber File: ${item.sourceFile} (${item.sourceFunction})", fontSize = 10.sp, color = Color(0xFF0369A1), fontWeight = FontWeight.Bold)
                            Text("⚙️ Akar Masalah: ${item.rootCauseTechnical}", fontSize = 10.sp, color = DrgTextSecondary)
                            HorizontalDivider(color = DrgOutline.copy(alpha = 0.2f))
                            Text("🛠️ Solusi Perbaikan: ${item.actionableFix}", fontSize = 10.sp, color = DrgGreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
