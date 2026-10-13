package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackgroundGradient)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DrgTextPrimary)
            }
            Text("Emergency & SOS Center", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        }

        EmergencyTabSelectorRow(
            selectedTab = selectedTab,
            activeAlertsCount = activeAlertsCount,
            onTabSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                item { SosHeroActionCard(selectedType = selectedType, onSelectType = { selectedType = it }, onTriggerSos = { isCountdownActive = true }) }
                item { SosAlarmQuickCard(isSosAlarmEnabled = isSosAlarmEnabled, onToggleSosAlarm = onToggleSosAlarm, onOpenSettings = onOpenSettings) }
                item { CrashGuardQuickCard(isCrashGuardEnabled = isCrashGuardEnabled, sensitivity = crashSensitivity, onSimulateCrash = onSimulateCrash, onOpenSettings = onOpenSettings) }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Pantauan Sinyal Darurat Aktif", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                        Text("$activeAlertsCount Sinyal Aktif", fontSize = 11.sp, color = DrgRedDanger, fontWeight = FontWeight.Bold)
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
