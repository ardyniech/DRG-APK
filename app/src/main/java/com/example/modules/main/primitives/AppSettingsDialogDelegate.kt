package com.example.modules.main.primitives

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.*
import com.example.modules.notifications.AppSettingsDialog
import com.example.shared.models.CrashSensitivity
import com.example.shared.models.DriverMember
import com.example.shared.models.NotificationPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppSettingsDialogDelegate(
    viewModel: DRGViewModel,
    context: Context,
    currentMember: DriverMember?,
    isSosAlarmEnabled: Boolean,
    isCrashGuardEnabled: Boolean,
    crashSensitivity: CrashSensitivity,
    mapCachePolicy: MapCachePolicy,
    locationSyncProfile: LocationSyncPowerProfile,
    isDataSaverMode: Boolean,
    isPowerSaverMode: Boolean,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onDismiss: () -> Unit
) {
    AppSettingsDialog(
        isSosAlarmEnabled = isSosAlarmEnabled,
        onToggleSosAlarm = { viewModel.toggleSosAlarmSound(it) },
        onTestSosAlarm = { viewModel.testSosAlarmSound() },
        onStopSosAlarm = { viewModel.stopSosAlarmSound() },
        isCrashGuardEnabled = isCrashGuardEnabled,
        onToggleCrashGuard = { viewModel.toggleCrashGuard(it) },
        crashSensitivity = crashSensitivity,
        onSelectCrashSensitivity = { viewModel.setCrashSensitivity(it) },
        onSimulateCrash = { viewModel.simulateCrashImpact(3.2f) },
        isLocationSharingConsent = currentMember?.isLocationSharingConsent == true,
        onToggleLocationSharing = { viewModel.toggleLocationSharingConsent(it) },
        cacheSizeDesc = viewModel.getCacheSizeDesc(context),
        selectedCachePolicy = mapCachePolicy,
        onSelectCachePolicy = { viewModel.setMapCachePolicy(it) },
        selectedSyncProfile = locationSyncProfile,
        onSelectSyncProfile = { viewModel.setLocationSyncProfile(it) },
        isDataSaverEnabled = isDataSaverMode,
        onToggleDataSaver = { viewModel.setDataSaverMode(it) },
        isPowerSaverEnabled = isPowerSaverMode,
        onTogglePowerSaver = { viewModel.setPowerSaverMode(it) },
        onPrecacheMap = { viewModel.precacheMap(context) },
        onClearCache = { viewModel.clearMapCache(context) },
        initialPref = NotificationPreference(currentMember?.id ?: "DRG-001"),
        onDismiss = onDismiss,
        onSavePref = {
            viewModel.saveNotificationPref(it)
            onDismiss()
            scope.launch { snackbarHostState.showSnackbar("Pengaturan & preferensi berhasil disimpan!") }
        }
    )
}
