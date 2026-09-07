package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.CrashSensitivity
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.EmergencyType
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
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(EmergencyType.MOGOK) }
    var isCountdownActive by remember { mutableStateOf(false) }
    var isEscortModeActive by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            SosHeroActionCard(
                selectedType = selectedType,
                onSelectType = { selectedType = it },
                onTriggerSos = { isCountdownActive = true }
            )
        }
        item {
            SosAlarmQuickCard(
                isSosAlarmEnabled = isSosAlarmEnabled,
                onToggleSosAlarm = onToggleSosAlarm,
                onOpenSettings = onOpenSettings
            )
        }
        item {
            CrashGuardQuickCard(
                isCrashGuardEnabled = isCrashGuardEnabled,
                sensitivity = crashSensitivity,
                onSimulateCrash = onSimulateCrash,
                onOpenSettings = onOpenSettings
            )
        }
        item {
            SosEscortModeCard(
                isEscortActive = isEscortModeActive,
                onToggleEscort = { isEscortModeActive = it }
            )
        }
        item {
            SatgasShiftScheduleCard(currentMember = currentMember)
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Laporan Sinyal Darurat Aktif", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text("${alerts.count { it.isActive }} Kasus Aktif", fontSize = 11.sp, color = DrgRedDanger, fontWeight = FontWeight.Bold)
            }
        }
        if (alerts.isEmpty()) {
            item { SosActiveAlertsEmptyCard() }
        } else {
            items(alerts) { alert ->
                EmergencyAlertCard(
                    alert = alert,
                    currentMember = currentMember,
                    onResolve = { onResolveEmergency(alert.id) },
                    onRespond = { onRespondEmergency(alert.id) },
                    onCall = { onCallDriver(alert.driverPhone) }
                )
            }
        }
    }

    if (isCountdownActive) {
        SosCountdownOverlay(
            emergencyType = selectedType,
            initialSeconds = 5,
            isAlarmSoundEnabled = isSosAlarmEnabled,
            onCancel = { isCountdownActive = false },
            onTriggerNow = {
                isCountdownActive = false
                onTriggerEmergency(
                    selectedType,
                    "Memerlukan bantuan darurat segera (${selectedType.label})",
                    "Lokasi Terkini GPS Driver"
                )
            }
        )
    }
}
