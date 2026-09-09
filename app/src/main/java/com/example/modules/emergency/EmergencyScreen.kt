package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun EmergencyScreen(
    currentMember: DriverMember?,
    alerts: List<EmergencyAlert>,
    onTriggerEmergency: (EmergencyType, String, String) -> Unit,
    onResolveEmergency: (String) -> Unit,
    onRespondEmergency: (String) -> Unit,
    onCallDriver: (String) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {},
    isCrashGuardEnabled: Boolean = true,
    crashSensitivity: CrashSensitivity = CrashSensitivity.MEDIUM,
    onSimulateCrash: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedType by remember { mutableStateOf(EmergencyType.MOGOK) }
    var isCountdownActive by remember { mutableStateOf(false) }
    val activeAlertsCount = alerts.count { it.isActive }

    Column(
        modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Back Header Navigation
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DrgTextPrimary)
            }
            Text("Emergency & SOS Center", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        }

        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DrgSurface).padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { selectedTab = 0 },
                colors = ButtonDefaults.buttonColors(containerColor = if (selectedTab == 0) DrgRedDanger else DrgSurface, contentColor = if (selectedTab == 0) Color.White else DrgTextSecondary),
                shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f).height(42.dp), contentPadding = PaddingValues(0.dp)
            ) { Text("🚨 Kirim SOS", fontWeight = FontWeight.Bold, fontSize = 13.sp) }

            Button(
                onClick = { selectedTab = 1 },
                colors = ButtonDefaults.buttonColors(containerColor = if (selectedTab == 1) DrgGreenPrimary else DrgSurface, contentColor = if (selectedTab == 1) Color.White else DrgTextSecondary),
                shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f).height(42.dp), contentPadding = PaddingValues(0.dp)
            ) { Text("📢 Respon SOS ($activeAlertsCount)", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                item { SosHeroActionCard(selectedType = selectedType, onSelectType = { selectedType = it }, onTriggerSos = { isCountdownActive = true }) }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pantauan Sinyal Darurat Aktif", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        Text("$activeAlertsCount Sinyal Aktif", fontSize = 12.sp, color = DrgRedDanger, fontWeight = FontWeight.Bold)
                    }
                }
                if (alerts.isEmpty()) {
                    item { SosActiveAlertsEmptyCard() }
                } else {
                    items(alerts) { alert ->
                        EmergencyAlertCard(
                            alert = alert, currentMember = currentMember,
                            onResolve = { onResolveEmergency(alert.id) }, onRespond = { onRespondEmergency(alert.id) }, onCall = { onCallDriver(alert.driverPhone) }
                        )
                    }
                }
            }
        }
    }

    if (isCountdownActive) {
        SosCountdownOverlay(
            emergencyType = selectedType, initialSeconds = 5, isAlarmSoundEnabled = isSosAlarmEnabled,
            onCancel = { isCountdownActive = false },
            onTriggerNow = {
                isCountdownActive = false
                onTriggerEmergency(selectedType, "Memerlukan bantuan darurat segera (${selectedType.label})", "Lokasi Terkini GPS Driver")
            }
        )
    }
}
