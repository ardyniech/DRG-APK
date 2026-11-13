package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.primitives.LauncherSeenState
import com.example.modules.dashboard.primitives.SquircleAppDock
import com.example.modules.dashboard.primitives.calculateLauncherBadgeDelta
import com.example.modules.dashboard.primitives.reconcileSeenWatermark
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.modules.devtools.DevToolsDashboardScreen
import com.example.shared.models.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LauncherBadgesAndDevToolsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun driver(role: MemberRole) = DriverMember(
        id = "DRG-99",
        driverId = "DRG-99",
        name = "Andi Wijaya",
        phone = "08111222333",
        baseArea = "Surabaya Timur",
        role = role,
        verificationStatus = VerificationStatus.VERIFIED,
        isOnline = true,
        motorcyclePlate = "L 1234 DRG",
        motorcycleModel = "PCX 160"
    )

    @Test
    fun badgeDeltaHandlesNewRecordsSeenClearAndDeletedRecordsSafely() {
        assertEquals(4, calculateLauncherBadgeDelta(currentCount = 4, seenCount = 0))
        assertEquals(0, calculateLauncherBadgeDelta(currentCount = 4, seenCount = 4))
        assertEquals(2, calculateLauncherBadgeDelta(currentCount = 6, seenCount = 4))
        // Bad/Shrunk record counts must never produce negative badge counts
        assertEquals(0, calculateLauncherBadgeDelta(currentCount = 2, seenCount = 5))
        assertEquals(0, calculateLauncherBadgeDelta(currentCount = -3, seenCount = 0))
        assertEquals(2, reconcileSeenWatermark(currentCount = 2, seenCount = 5))

        val state = LauncherSeenState(context = null, initialCounts = mapOf("forum" to 2))
        assertEquals(3, state.badgeFor("forum", currentCount = 5))
        state.markSeen("forum", currentCount = 5)
        assertEquals(0, state.badgeFor("forum", currentCount = 5))
        assertEquals(1, state.badgeFor("forum", currentCount = 6))
        // Deleted record shrink followed by new post still increments cleanly
        assertEquals(0, state.badgeFor("forum", currentCount = 2))
    }

    @Test
    fun squircleAppDockEnforcesRolesShowsRecordBadgesAndClearsOnClick() {
        var navigatedTab: MainNavTab? = null

        composeRule.setContent {
            SquircleAppDock(
                visualTheme = DashboardVisualTheme.EMERALD_AURORA,
                onNavigateTab = { navigatedTab = it },
                role = MemberRole.SUPER_ADMIN,
                radarCount = 3,
                emergencyCount = 1,
                forumCount = 5,
                kasCount = 120,
                membersCount = 8,
                communityNotifCount = 2,
                openTasksCount = 4,
                poskoCount = 2,
                pendingAdminCount = 1
            )
        }

        composeRule.waitForIdle()
        composeRule.onNodeWithTag("launcher_tile_radar").assertIsDisplayed()
        composeRule.onNodeWithTag("launcher_tile_workflow").assertIsDisplayed()
        composeRule.onNodeWithTag("launcher_tile_posko").assertIsDisplayed()
        composeRule.onNodeWithTag("launcher_tile_admin").assertIsDisplayed()
        composeRule.onNodeWithTag("launcher_tile_devtools").assertIsDisplayed()

        // Badge shows 5 on Forum and 99+ cap on Kas (120)
        composeRule.onNodeWithTag("launcher_badge_forum", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("5", useUnmergedTree = true).assertExists()
        composeRule.onNodeWithTag("launcher_badge_kas", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("99+", useUnmergedTree = true).assertExists()

        // Clicking Forum clears its badge and routes to FORUM
        composeRule.onNodeWithTag("launcher_tile_forum").performClick()
        composeRule.waitForIdle()
        assertEquals(MainNavTab.FORUM, navigatedTab)
        composeRule.onNodeWithTag("launcher_badge_forum", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun squircleAppDockHidesAdminAndDevToolsForRegularMember() {
        composeRule.setContent {
            SquircleAppDock(
                visualTheme = DashboardVisualTheme.MINIMAL_PEARL,
                onNavigateTab = {},
                role = MemberRole.ANGGOTA
            )
        }

        composeRule.waitForIdle()
        composeRule.onNodeWithTag("launcher_tile_radar").assertIsDisplayed()
        composeRule.onNodeWithTag("launcher_tile_profile").assertIsDisplayed()
        composeRule.onNodeWithTag("launcher_tile_admin").assertDoesNotExist()
        composeRule.onNodeWithTag("launcher_tile_devtools").assertDoesNotExist()
    }

    @Test
    fun devToolsScreenLocksOutNonSuperAdminAndRecoversCleanly() {
        var backPressed = false

        composeRule.setContent {
            DevToolsDashboardScreen(
                currentMember = driver(MemberRole.KETUA),
                onBack = { backPressed = true },
                visualTheme = AppVisualTheme.MINIMAL_PEARL
            )
        }

        composeRule.waitForIdle()
        composeRule.onNodeWithTag("devtools_access_denied").assertIsDisplayed()
        composeRule.onNodeWithTag("devtools_tab_0").assertDoesNotExist()
        composeRule.onNodeWithText("Kembali ke Profil").performClick()
        assertTrue(backPressed)
    }

    @Test
    fun devToolsScreenDisplaysSuperAdminGlassConsoleAndSwitchesTabs() {
        composeRule.setContent {
            DevToolsDashboardScreen(
                currentMember = driver(MemberRole.SUPER_ADMIN),
                onBack = {},
                visualTheme = AppVisualTheme.EMERALD_AURORA
            )
        }

        composeRule.waitForIdle()
        composeRule.onNodeWithTag("dev_tools_dashboard_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("devtools_access_denied").assertDoesNotExist()
        composeRule.onNodeWithText("SUPER ADMIN").assertIsDisplayed()
        composeRule.onNodeWithTag("devtools_tab_0").assertIsDisplayed()
        composeRule.onNodeWithTag("devtools_tab_1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("devtools_tab_1").assertIsDisplayed()
    }
}
