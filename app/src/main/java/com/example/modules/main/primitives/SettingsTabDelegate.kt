package com.example.modules.main.primitives

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.viewmodel.*
import com.example.modules.notifications.settings.SettingsScreen
import com.example.modules.profile.CloudBackupCard
import com.example.modules.profile.ProfileAboutEntry
import com.example.modules.profile.ProfileUpdateEntry
import com.example.shared.models.*
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun SettingsTabDelegate(
    viewModel: DRGViewModel,
    currentMember: DriverMember?,
    visualTheme: AppVisualTheme,
    onSelectVisualTheme: (AppVisualTheme) -> Unit,
    focusMode: AppFocusMode,
    onSelectFocusMode: (AppFocusMode) -> Unit,
    isSosAlarmEnabled: Boolean,
    isCrashGuardEnabled: Boolean,
    crashSensitivity: CrashSensitivity,
    mapCachePolicy: MapCachePolicy,
    locationSyncProfile: LocationSyncPowerProfile,
    isDataSaverMode: Boolean,
    isPowerSaverMode: Boolean,
    snackbarHostState: SnackbarHostState,
    scaffoldPadding: PaddingValues
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val memberId = currentMember?.id ?: "DRG-001"
    val syncStatus by viewModel.syncEngine.syncStatus.collectAsStateWithLifecycle()
    val userEmail = rememberFirebaseAuthEmail(context)
    val savedPref by viewModel.getNotificationPrefFlow(memberId)
        .collectAsStateWithLifecycle(initialValue = NotificationPreference(memberId))

    SettingsScreen(
        visualTheme = visualTheme,
        onSelectVisualTheme = onSelectVisualTheme,
        focusMode = focusMode,
        onSelectFocusMode = onSelectFocusMode,
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
        notificationPref = savedPref ?: NotificationPreference(memberId),
        onSaveNotificationPref = { pref ->
            viewModel.saveNotificationPref(pref)
            scope.launch { snackbarHostState.showSnackbar("Preferensi pengaturan disimpan") }
        },
        systemEntriesContent = {
            CloudBackupCard(
                syncStatus = syncStatus,
                userEmail = userEmail,
                onSyncNow = {
                    viewModel.syncOwnProfileToCloud(currentMember)
                    viewModel.syncEngine.triggerBackgroundSync(isPowerSaverMode)
                },
                onUploadBackup = { viewModel.uploadEncryptedCloudBackup() },
                onRestoreBackup = { viewModel.restoreEncryptedCloudBackup() }
            )
            ProfileUpdateEntry()
            ProfileAboutEntry(onOpenAbout = { viewModel.selectTab(MainNavTab.ABOUT) }, visualTheme = visualTheme)
        },
        onBack = { viewModel.selectTab(MainNavTab.PROFILE) },
        contentPadding = scaffoldPadding
    )
}

@Composable
private fun rememberFirebaseAuthEmail(context: android.content.Context): String? {
    return if (FirebaseApp.getApps(context).isNotEmpty()) Firebase.auth.currentUser?.email else null
}
