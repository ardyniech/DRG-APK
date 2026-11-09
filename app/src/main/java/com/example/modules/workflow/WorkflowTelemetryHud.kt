package com.example.modules.workflow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun WorkflowTelemetryHud(
    stage: WorkflowStage,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0F172A),
        modifier = modifier.fillMaxWidth().border(1.2.dp, DrgGreenPrimary, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("TELEMETRI LAPANGAN RIIL (OFF-APP)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Surface(shape = RoundedCornerShape(6.dp), color = if (stage.speedKmh > 0) DrgGreenDark else DrgAmberDark) {
                    Text(
                        text = if (stage.speedKmh > 0) "MELAJU" else "STANDBY / REHAT",
                        fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("KECEPATAN", fontSize = 9.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)
                    Text("${stage.speedKmh} KM/H", fontSize = 22.sp, fontWeight = FontWeight.Black, color = DrgSegmentOn)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SENSOR G-FORCE", fontSize = 9.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)
                    Text(stage.gForceText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (stage.id == 3) DrgRedDanger else Color.White)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("CUACA JALAN", fontSize = 9.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)
                    Text(stage.weather, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgAmberWarning)
                }
            }

            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF1E293B), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Status Sistem DRG:", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text(stage.statusText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                }
            }
        }
    }
}
