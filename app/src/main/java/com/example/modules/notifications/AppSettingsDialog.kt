package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.shared.models.CrashSensitivity
import com.example.shared.models.NotificationPreference
import com.example.ui.theme.*

@Composable
fun AppSettingsDialog(
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    onTestSosAlarm: () -> Unit,
    onStopSosAlarm: () -> Unit,
    isCrashGuardEnabled: Boolean,
    onToggleCrashGuard: (Boolean) -> Unit,
    crashSensitivity: CrashSensitivity,
    onSelectCrashSensitivity: (CrashSensitivity) -> Unit,
    onSimulateCrash: () -> Unit,
    isLocationSharingConsent: Boolean,
    onToggleLocationSharing: (Boolean) -> Unit,
    cacheSizeDesc: String = "0.0 MB",
    selectedCachePolicy: MapCachePolicy = MapCachePolicy.CACHE_FIRST,
    onSelectCachePolicy: (MapCachePolicy) -> Unit = {},
    selectedSyncProfile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO,
    onSelectSyncProfile: (LocationSyncPowerProfile) -> Unit = {},
    isDataSaverEnabled: Boolean = true,
    onToggleDataSaver: (Boolean) -> Unit = {},
    isPowerSaverEnabled: Boolean = false,
    onTogglePowerSaver: (Boolean) -> Unit = {},
    onPrecacheMap: () -> Unit = {},
    onClearCache: () -> Unit = {},
    initialPref: NotificationPreference,
    onDismiss: () -> Unit,
    onSavePref: (NotificationPreference) -> Unit
) {
    var isTestingSound by remember { mutableStateOf(false) }
    var sosAlerts by remember { mutableStateOf(initialPref.sosAlerts) }
    var kopdarReminders by remember { mutableStateOf(initialPref.kopdarReminders) }
    var kasUpdates by remember { mutableStateOf(initialPref.kasUpdates) }

    Dialog(onDismissRequest = {
        if (isTestingSound) onStopSosAlarm()
        onDismiss()
    }) {
        Surface(shape = RoundedCornerShape(20.dp), color = DrgSurface, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(modifier = Modifier.padding(18.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AppSettingsHeaderRow(onClose = {
                    if (isTestingSound) onStopSosAlarm()
                    onDismiss()
                })

                SosAlarmSettingSection(
                    isSosAlarmEnabled = isSosAlarmEnabled,
                    onToggleSosAlarm = {
                        if (isTestingSound) { onStopSosAlarm(); isTestingSound = false }
                        onToggleSosAlarm(it)
                    },
                    isTestingSound = isTestingSound,
                    onTestToggle = {
                        if (!isTestingSound) { isTestingSound = true; onTestSosAlarm() }
                        else { isTestingSound = false; onStopSosAlarm() }
                    }
                )

                CrashGuardSettingSection(isCrashGuardEnabled = isCrashGuardEnabled, onToggleCrashGuard = onToggleCrashGuard, sensitivity = crashSensitivity, onSelectSensitivity = onSelectCrashSensitivity, onSimulateCrash = onSimulateCrash)
                LiveLocationSharingSettingRow(isLocationSharingConsent = isLocationSharingConsent, onToggleLocationSharing = onToggleLocationSharing)
                CacheAndBatterySettingSection(cacheSizeDesc = cacheSizeDesc, selectedPolicy = selectedCachePolicy, onSelectPolicy = onSelectCachePolicy, selectedSyncProfile = selectedSyncProfile, onSelectSyncProfile = onSelectSyncProfile, isDataSaverEnabled = isDataSaverEnabled, onToggleDataSaver = onToggleDataSaver, isPowerSaverEnabled = isPowerSaverEnabled, onTogglePowerSaver = onTogglePowerSaver, onPrecacheMap = onPrecacheMap, onClearCache = onClearCache)
                HorizontalDivider(color = DrgOutline.copy(alpha = 0.4f))
                NotificationPushPrefSection(sosAlerts = sosAlerts, onSosAlertsChange = { sosAlerts = it }, kopdarReminders = kopdarReminders, onKopdarRemindersChange = { kopdarReminders = it }, kasUpdates = kasUpdates, onKasUpdatesChange = { kasUpdates = it })

                Button(
                    onClick = {
                        if (isTestingSound) onStopSosAlarm()
                        onSavePref(initialPref.copy(sosAlerts = sosAlerts, kopdarReminders = kopdarReminders, kasUpdates = kasUpdates))
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan Pengaturan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
