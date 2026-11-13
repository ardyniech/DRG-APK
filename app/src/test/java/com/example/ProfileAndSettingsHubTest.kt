package com.example

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.modules.notifications.settings.SettingsScreen
import com.example.modules.profile.ProfileScreen
import com.example.shared.models.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ProfileAndSettingsHubTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun sampleMember(role: MemberRole) = DriverMember(
        id = "DRG-07",
        driverId = "DRG-07",
        name = "Budi Santoso",
        phone = "08123456789",
        baseArea = "Malang Kota",
        role = role,
        verificationStatus = VerificationStatus.VERIFIED,
        isOnline = true,
        currentStatus = "Aktif Narik",
        motorcyclePlate = "N 4455 DRG",
        motorcycleModel = "NMAX 155",
        loyaltyPoints = 420,
        kopdarAttendanceCount = 12,
        bloodType = "O+"
    )

    @Test
    fun profileSettingsLauncherAndRoleGatedDevToolsWorkCorrectly() {
        var settingsCount = 0
        var devToolsTapped = false

        composeRule.setContent {
            ProfileScreen(
                currentMember = sampleMember(MemberRole.SUPER_ADMIN),
                onUpdateProfile = { _, _, _, _, _, _, _, _, _, _, _ -> },
                onOpenSettings = { settingsCount++ },
                onOpenDevTools = { devToolsTapped = true },
                visualTheme = AppVisualTheme.MINIMAL_PEARL
            )
        }

        composeRule.onNodeWithTag("profile_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("profile_settings_launcher").performClick()
        composeRule.onNodeWithTag("btn_open_settings_launcher").performClick()
        assertEquals(2, settingsCount)

        composeRule.onNodeWithTag("btn_superadmin_dev_tools").performClick()
        assertTrue(devToolsTapped)
    }

    @Test
    fun profileHidesDevToolsForRegularMemberAndHandlesNullSafely() {
        composeRule.setContent {
            ProfileScreen(
                currentMember = null,
                onUpdateProfile = { _, _, _, _, _, _, _, _, _, _, _ -> }
            )
        }

        composeRule.onNodeWithText("Driver DRG").assertIsDisplayed()
        composeRule.onNodeWithTag("btn_superadmin_dev_tools").assertDoesNotExist()
        composeRule.onNodeWithTag("btn_admin_governance").assertDoesNotExist()
    }

    @Test
    fun settingsHubSwitchesAllFourTabsAndUpdatesThemeModeCallbacks() {
        var chosenTheme by mutableStateOf(AppVisualTheme.EMERALD_AURORA)
        var chosenMode by mutableStateOf(AppFocusMode.SATGAS_TRC)
        var backCalled = false

        composeRule.setContent {
            SettingsScreen(
                visualTheme = chosenTheme,
                onSelectVisualTheme = { chosenTheme = it },
                focusMode = chosenMode,
                onSelectFocusMode = { chosenMode = it },
                isSosAlarmEnabled = true, onToggleSosAlarm = {},
                onTestSosAlarm = {}, onStopSosAlarm = {},
                isCrashGuardEnabled = true, onToggleCrashGuard = {},
                crashSensitivity = CrashSensitivity.MEDIUM, onSelectCrashSensitivity = {},
                onSimulateCrash = {},
                isLocationSharingConsent = true, onToggleLocationSharing = {},
                cacheSizeDesc = "2.4 MB",
                selectedCachePolicy = MapCachePolicy.CACHE_FIRST, onSelectCachePolicy = {},
                selectedSyncProfile = LocationSyncPowerProfile.ADAPTIVE_ECO, onSelectSyncProfile = {},
                isDataSaverEnabled = true, onToggleDataSaver = {},
                isPowerSaverEnabled = false, onTogglePowerSaver = {},
                onPrecacheMap = {}, onClearCache = {},
                notificationPref = NotificationPreference("DRG-07"),
                onSaveNotificationPref = {},
                systemEntriesContent = { Text("Sistem Slot Aktif") },
                onBack = { backCalled = true }
            )
        }

        composeRule.onNodeWithTag("settings_hub_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_theme_minimal_pearl").performScrollTo().performClick()
        assertEquals(AppVisualTheme.MINIMAL_PEARL, chosenTheme)

        composeRule.onNodeWithTag("settings_mode_admin").performScrollTo().performClick()
        assertEquals(AppFocusMode.ADMINISTRASI, chosenMode)

        composeRule.onNodeWithTag("settings_tab_security").performScrollTo().performClick()
        composeRule.onNodeWithText("Sirine SOS Desibel Tinggi (100 dB)").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_tab_gps").performScrollTo().performClick()
        composeRule.onNodeWithText("Share Live GPS 12 Jam Komunitas").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_tab_system").performScrollTo().performClick()
        composeRule.onNodeWithText("Sistem Slot Aktif").assertIsDisplayed()

        composeRule.onNodeWithTag("settings_back_button").performClick()
        assertTrue(backCalled)
    }
}
