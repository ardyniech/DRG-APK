package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.CrashSensitivity
import com.example.shared.models.NotificationPreference

@Composable
fun AppSettingsDialog(
    isSosAlarmEnabled: Boolean, onToggleSosAlarm: (Boolean) -> Unit, onTestSosAlarm: () -> Unit, onStopSosAlarm: () -> Unit,
    isCrashGuardEnabled: Boolean, onToggleCrashGuard: (Boolean) -> Unit, crashSensitivity: CrashSensitivity,
    onSelectCrashSensitivity: (CrashSensitivity) -> Unit, onSimulateCrash: () -> Unit,
    isLocationSharingConsent: Boolean, onToggleLocationSharing: (Boolean) -> Unit,
    cacheSizeDesc: String = "0.0 MB", selectedCachePolicy: MapCachePolicy = MapCachePolicy.CACHE_FIRST,
    onSelectCachePolicy: (MapCachePolicy) -> Unit = {},
    selectedSyncProfile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO,
    onSelectSyncProfile: (LocationSyncPowerProfile) -> Unit = {},
    isDataSaverEnabled: Boolean = true, onToggleDataSaver: (Boolean) -> Unit = {},
    isPowerSaverEnabled: Boolean = false, onTogglePowerSaver: (Boolean) -> Unit = {},
    onPrecacheMap: () -> Unit = {}, onClearCache: () -> Unit = {},
    initialPref: NotificationPreference, onOpenAbout: () -> Unit = {},
    onDismiss: () -> Unit, onSavePref: (NotificationPreference) -> Unit
) {
    val theme = LocalAppVisualTheme.current
    var isTestingSound by remember { mutableStateOf(false) }
    var sosAlerts by remember { mutableStateOf(initialPref.sosAlerts) }
    var kopdarReminders by remember { mutableStateOf(initialPref.kopdarReminders) }
    var kasUpdates by remember { mutableStateOf(initialPref.kasUpdates) }
    val closeDialog: () -> Unit = { if (isTestingSound) onStopSosAlarm(); onDismiss() }

    DrgLuminousGlassModal(
        title = "Pengaturan & Keamanan",
        subtitle = "Sirine SOS, Crash Guard, Peta Offline & Notifikasi",
        onDismiss = closeDialog,
        icon = Icons.Default.Settings,
        accentColor = theme.primaryColor,
        footer = {
            DrgGlassPrimaryButton(
                text = "Simpan Pengaturan",
                onClick = {
                    if (isTestingSound) onStopSosAlarm()
                    onSavePref(initialPref.copy(sosAlerts = sosAlerts, kopdarReminders = kopdarReminders, kasUpdates = kasUpdates))
                    onDismiss()
                }
            )
            OutlinedButton(
                onClick = { closeDialog(); onOpenAbout() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = theme.primaryColor)
            ) {
                Text("Tentang Aplikasi & Peta Jalan V", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        SosAlarmSettingSection(
            isSosAlarmEnabled = isSosAlarmEnabled,
            onToggleSosAlarm = { if (isTestingSound) { onStopSosAlarm(); isTestingSound = false }; onToggleSosAlarm(it) },
            isTestingSound = isTestingSound,
            onTestToggle = {
                if (!isTestingSound) { isTestingSound = true; onTestSosAlarm() } else { isTestingSound = false; onStopSosAlarm() }
            }
        )
        CrashGuardSettingSection(isCrashGuardEnabled, onToggleCrashGuard, crashSensitivity, onSelectCrashSensitivity, onSimulateCrash)
        LiveLocationSharingSettingRow(isLocationSharingConsent, onToggleLocationSharing)
        CacheAndBatterySettingSection(
            cacheSizeDesc, selectedCachePolicy, onSelectCachePolicy, selectedSyncProfile, onSelectSyncProfile,
            isDataSaverEnabled, onToggleDataSaver, isPowerSaverEnabled, onTogglePowerSaver, onPrecacheMap, onClearCache
        )
        HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))
        NotificationPushPrefSection(sosAlerts, { sosAlerts = it }, kopdarReminders, { kopdarReminders = it }, kasUpdates, { kasUpdates = it })
    }
}
