package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevMemoryTab(
    headerContent: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current
    val probe = RealtimeMemoryProbe.inspect(context)
    val heapRatio = if (probe.heapMaxMb > 0) probe.heapUsedMb.toFloat() / probe.heapMaxMb.toFloat() else 0.2f

    DevTabShell(
        headerContent = headerContent,
        headerIcon = Icons.Default.Memory,
        headerTitle = "Monitor RAM & Gejala Kejenuhan Memori",
        headerDesc = "Auto-deteksi alokasi heap JVM, memory OS Android, dan thread saturation tanpa mocking."
    ) {

        probe.earlySymptomWarning?.let { warning ->
            item {
                Surface(shape = RoundedCornerShape(10.dp), color = DrgWarningBgAmberSoft, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = DrgAmberWarning, modifier = Modifier.size(16.dp))
                        Text(warning, fontSize = 10.sp, color = DrgWarningTextAmber, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = DrgGreenLight, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("JVM Heap: ${probe.heapUsedMb} MB / ${probe.heapMaxMb} MB", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                        Text("${(heapRatio * 100).toInt()}% Terpakai", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgGreenPrimary)
                    }
                    LinearProgressIndicator(progress = { heapRatio.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth().height(8.dp), color = if (heapRatio < 0.65f) DrgGreenPrimary else Color.Red, trackColor = Color.White)
                    Text("Free Heap: ${probe.heapFreeMb} MB • Active Threads: ${probe.activeThreadCount}", fontSize = 10.sp, color = DrgTextSecondary)
                }
            }
        }

        item {
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("📱 Telemetri RAM Fisik Perangkat (ActivityManager)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("RAM Sistem Tersedia:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${probe.deviceAvailRamMb} MB / ${probe.deviceTotalRamMb} MB", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Low RAM Device Mode:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(if (probe.isLowRamDevice) "Aktif (Entry-Level Tuning)" else "Non-Aktif (Standar)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("OS Low Memory Trim Signal:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(if (probe.isSystemLowMemory) "PERINGATAN (TRIM)" else "Aman (Normal)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (probe.isSystemLowMemory) Color.Red else DrgGreenPrimary)
                    }
                }
            }
        }
    }
}
