package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class DevTaskItem(val title: String, val module: String, val status: String, val isCompleted: Boolean)

@Composable
fun DevProgressTab() {
    val tasks = listOf(
        DevTaskItem("Clean Architecture MVI & State Flow", "Core Architecture", "Selesai", true),
        DevTaskItem("Room v13 Database & 23 Entities", "Storage Layer", "Selesai", true),
        DevTaskItem("Live Driver Map & Radar Posko", "Radar & Posko", "Selesai", true),
        DevTaskItem("SOS Emergency & Haptic / Alarm Engine", "Emergency TRC", "Selesai", true),
        DevTaskItem("Transparent Kas Komunitas & Export Report", "Treasury", "Selesai", true),
        DevTaskItem("Forum Diskusi & Bengkel Rekanan Rating", "Community Hub", "Selesai", true),
        DevTaskItem("Gamification Badges ('Anggota Teraktif', dll)", "Gamification", "Selesai", true),
        DevTaskItem("Dedicated Field Workflow Simulation (Malang)", "Workflow", "Selesai", true),
        DevTaskItem("Background Sync Cloud Worker Queue", "Sync Worker", "Pending (-2% Penalti)", false)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DrgAmberContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("📊 Status Progress & Penalti Progres (-2%)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgOnAmberContainer)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Ada 1 task background sync queue yang belum tuntas, memicu penalti progres -2% sesuai aturan ketat proyek.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }

        items(tasks) { task ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (task.isCompleted) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(20.dp))
                    } else {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = DrgAmberWarning, modifier = Modifier.size(20.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(task.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                        Text("Modul: ${task.module}", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (task.isCompleted) DrgGreenLight else Color(0xFFFFECB3)
                    ) {
                        Text(task.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (task.isCompleted) DrgGreenPrimary else Color(0xFFF57C00), modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                }
            }
        }
    }
}
