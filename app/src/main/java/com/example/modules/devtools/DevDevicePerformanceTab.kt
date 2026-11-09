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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevDevicePerformanceTab() {
    val context = LocalContext.current
    val perf = remember { DevicePerformanceEngine.inspectPerformance(context) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (perf.isOverheating) Color(0xFFFFFBEB) else DrgGreenLight,
                modifier = Modifier.fillMaxWidth().border(1.dp, if (perf.isOverheating) DrgAmberWarning.copy(alpha = 0.5f) else DrgGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⚡ Real-Time Device Performance Node", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (perf.isOverheating) Color(0xFFB45309) else DrgGreenPrimary)
                        Icon(Icons.Default.Memory, contentDescription = null, tint = if (perf.isOverheating) DrgAmberWarning else DrgGreenPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Saran Aksi: ${perf.optimizationSuggestion}", fontSize = 11.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("📈 Telemetri Hardware & Termal OS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.3f))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Estimasi Utilisasi CPU:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${String.format("%.1f", perf.cpuUtilizationPercent)}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Suhu Baterai / Perangkat:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${perf.batteryTemperatureCelsius}°C", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (perf.batteryTemperatureCelsius > 39.0) DrgRedPanic else DrgGreenPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Status Termal OS:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text(perf.thermalStatusString, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Active Threads:", fontSize = 11.sp, color = DrgTextSecondary)
                        Text("${perf.cpuThreadCount} Threads", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    }
                }
            }
        }
    }
}
