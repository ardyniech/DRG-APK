package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.modules.main.DrgBottomNavBar
import com.example.modules.main.resolveDockSelectedIndex
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BottomNavBarRedesignTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun splitDockAndSatelliteOrbNavigationCallbacksWork() {
        var selectedTab: MainNavTab = MainNavTab.DASHBOARD
        var emergencyTapped = false

        composeRule.setContent {
            DrgBottomNavBar(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                activeEmergencyCount = 0,
                accentColor = DashboardVisualTheme.DEEP_SUNSET_TEAL.primaryColor,
                pillBgColor = DashboardVisualTheme.DEEP_SUNSET_TEAL.pillBg,
                glassBgColor = DashboardVisualTheme.DEEP_SUNSET_TEAL.glassBg,
                onEmergencyClick = { emergencyTapped = true }
            )
        }

        composeRule.onNodeWithTag("bottom_nav_bar").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_tab_radar").performClick()
        assertEquals(MainNavTab.RADAR, selectedTab)

        composeRule.onNodeWithTag("nav_tab_community").performClick()
        assertEquals(MainNavTab.COMMUNITY, selectedTab)

        composeRule.onNodeWithTag("nav_tab_profile").performClick()
        assertEquals(MainNavTab.PROFILE, selectedTab)

        composeRule.onNodeWithTag("nav_tab_dashboard").performClick()
        assertEquals(MainNavTab.DASHBOARD, selectedTab)

        composeRule.onNodeWithTag("bottom_nav_sos_orb").performClick()
        assertTrue(emergencyTapped)
    }

    @Test
    fun adversarialHighAlertCountCapsCleanlyAndTabGroupingResolvesSafely() {
        composeRule.setContent {
            DrgBottomNavBar(
                selectedTab = MainNavTab.KAS,
                onSelectTab = {},
                activeEmergencyCount = 150
            )
        }

        composeRule.onNodeWithTag("bottom_nav_sos_orb").assertIsDisplayed()
        composeRule.onNodeWithText("99").assertIsDisplayed()

        assertEquals(0, resolveDockSelectedIndex(MainNavTab.DASHBOARD))
        assertEquals(1, resolveDockSelectedIndex(MainNavTab.RADAR))
        assertEquals(2, resolveDockSelectedIndex(MainNavTab.KAS))
        assertEquals(2, resolveDockSelectedIndex(MainNavTab.FORUM))
        assertEquals(3, resolveDockSelectedIndex(MainNavTab.ADMIN))
        assertEquals(-1, resolveDockSelectedIndex(MainNavTab.EMERGENCY))
    }
}
