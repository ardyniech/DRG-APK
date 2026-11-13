package com.example.modules.notifications.settings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.shared.atoms.DrgLuminousAtmosphere
import com.example.shared.models.AppFocusMode
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.CrashSensitivity
import com.example.shared.models.NotificationPreference

/**
 * Unified Tabbed Settings Hub Screen entered exclusively via Profile's Settings launcher.
 */
@Composable
fun SettingsScreen(
    visualTheme: AppVisualTheme,
    onSelectVisualTheme: (AppVisualTheme) -> Unit,
    focusMode: AppFocusMode,
    onSelectFocusMode: (AppFocusMode) -> Unit,
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
    cacheSizeDesc: String,
    selectedCachePolicy: MapCachePolicy,
    onSelectCachePolicy: (MapCachePolicy) -> Unit,
    selectedSyncProfile: LocationSyncPowerProfile,
    onSelectSyncProfile: (LocationSyncPowerProfile) -> Unit,
    isDataSaverEnabled: Boolean,
    onToggleDataSaver: (Boolean) -> Unit,
    isPowerSaverEnabled: Boolean,
    onTogglePowerSaver: (Boolean) -> Unit,
    onPrecacheMap: () -> Unit,
    onClearCache: () -> Unit,
    notificationPref: NotificationPreference,
    onSaveNotificationPref: (NotificationPreference) -> Unit,
    systemEntriesContent: @Composable ColumnScope.() -> Unit,
    onBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(SettingsHubTab.PERSONALISASI) }
    BackHandler { onBack() }

    Box(modifier = modifier.fillMaxSize().testTag("settings_hub_screen")) {
        DrgLuminousAtmosphere(theme = visualTheme)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            SettingsTabBar(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                onBack = onBack,
                visualTheme = visualTheme
            )

            Crossfade(
                targetState = selectedTab,
                animationSpec = tween(200),
                label = "settingsTabSwitch",
                modifier = Modifier.weight(1f)
            ) { tab ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 12.dp, bottom = contentPadding.calculateBottomPadding() + 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (tab) {
                        SettingsHubTab.PERSONALISASI -> SettingsPersonalizationTab(
                            visualTheme, onSelectVisualTheme, focusMode, onSelectFocusMode, notificationPref, onSaveNotificationPref
                        )
                        SettingsHubTab.KEAMANAN -> SettingsSafetyTab(
                            visualTheme, isSosAlarmEnabled, onToggleSosAlarm, onTestSosAlarm, onStopSosAlarm,
                            isCrashGuardEnabled, onToggleCrashGuard, crashSensitivity, onSelectCrashSensitivity, onSimulateCrash
                        )
                        SettingsHubTab.GPS_PETA -> SettingsGpsCacheTab(
                            visualTheme, isLocationSharingConsent, onToggleLocationSharing, cacheSizeDesc,
                            selectedCachePolicy, onSelectCachePolicy, selectedSyncProfile, onSelectSyncProfile,
                            isDataSaverEnabled, onToggleDataSaver, isPowerSaverEnabled, onTogglePowerSaver,
                            onPrecacheMap, onClearCache
                        )
                        SettingsHubTab.SISTEM -> Column(verticalArrangement = Arrangement.spacedBy(12.dp), content = systemEntriesContent)
                    }
                }
            }
        }
    }
}
