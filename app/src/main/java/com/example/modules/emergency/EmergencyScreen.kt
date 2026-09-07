package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.ui.theme.*

@Composable
fun EmergencyScreen(
    currentMember: DriverMember?,
    alerts: List<EmergencyAlert>,
    onTriggerEmergency: () -> Unit,
    onResolveEmergency: (String) -> Unit,
    onRespondEmergency: (String) -> Unit,
    onCallDriver: (String) -> Unit,
    isSosAlarmEnabled: Boolean = true,
    onToggleSosAlarm: (Boolean) -> Unit = {},
    isCrashGuardEnabled: Boolean = true,
    crashSensitivity: com.example.shared.models.CrashSensitivity = com.example.shared.models.CrashSensitivity.MEDIUM,
    onSimulateCrash: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isEscortModeActive by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Quick-Access SOS Alarm Toggle Card in Emergency feature
        item {
            SosAlarmQuickCard(
                isSosAlarmEnabled = isSosAlarmEnabled,
                onToggleSosAlarm = onToggleSosAlarm,
                onOpenSettings = onOpenSettings
            )
        }

        // Crash Guard Sensor Card
        item {
            CrashGuardQuickCard(
                isCrashGuardEnabled = isCrashGuardEnabled,
                sensitivity = crashSensitivity,
                onSimulateCrash = onSimulateCrash,
                onOpenSettings = onOpenSettings
            )
        }

        // Escort Mode Toggle Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isEscortModeActive) DrgGreenContainer else DrgSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isEscortModeActive) DrgGreenPrimary else DrgOutline.copy(alpha = 0.6f),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Mode Pantauan",
                                tint = if (isEscortModeActive) DrgGreenPrimary else DrgTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mode Pantau Jalur Rawan (Escort)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DrgTextPrimary
                            )
                        }
                        Text(
                            text = if (isEscortModeActive) "Perjalanan Anda sedang dipantau live oleh Satgas DRG." else "Aktifkan saat melintasi jalur gelap/sepi malam hari.",
                            fontSize = 11.sp,
                            color = DrgTextSecondary
                        )
                    }
                    Switch(
                        checked = isEscortModeActive,
                        onCheckedChange = { isEscortModeActive = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = DrgGreenPrimary)
                    )
                }
            }
        }

        // Trigger New Emergency Action
        item {
            Button(
                onClick = onTriggerEmergency,
                colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Pancarkan SOS",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PANCARKAN SINYAL DARURAT (SOS)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Satgas Shift Rotational Duty Schedule
        item {
            SatgasShiftScheduleCard(currentMember = currentMember)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Laporan Sinyal Darurat Aktif",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextPrimary
                )
                Text(
                    text = "${alerts.count { it.isActive }} Kasus Aktif",
                    fontSize = 11.sp,
                    color = DrgRedDanger,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (alerts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DrgSurface)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Aman",
                            tint = DrgGreenPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Kondisi Jalanan Aman",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DrgTextPrimary
                        )
                        Text(
                            text = "Tidak ada laporan sinyal darurat aktif saat ini.",
                            fontSize = 11.sp,
                            color = DrgTextSecondary
                        )
                    }
                }
            }
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
}
