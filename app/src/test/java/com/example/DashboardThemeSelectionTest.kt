package com.example

import com.example.modules.dashboard.theme.DashboardFocusMode
import com.example.modules.dashboard.theme.DashboardVisualTheme
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DashboardThemeSelectionTest {

    @Test
    fun testThreeVisualPresetsAvailabilityAndUniqueness() {
        val themes = DashboardVisualTheme.values()
        assertEquals(3, themes.size)

        val emerald = DashboardVisualTheme.EMERALD_AURORA
        assertEquals("Emerald Aurora", emerald.title)
        assertNotNull(emerald.backgroundGradient)

        val pearl = DashboardVisualTheme.MINIMAL_PEARL
        assertEquals("Minimal Pearl", pearl.title)
        assertNotNull(pearl.backgroundGradient)

        val teal = DashboardVisualTheme.DEEP_SUNSET_TEAL
        assertEquals("Sunset Teal", teal.title)
        assertNotNull(teal.backgroundGradient)

        // Ensure all 3 themes have genuinely distinct palettes and atmospheric orbs
        assertEquals(3, themes.map { it.primaryColor }.toSet().size)
        assertEquals(3, themes.map { it.skyTop }.toSet().size)
        assertEquals(3, themes.map { it.orbPrimary }.toSet().size)
        assertEquals(3, themes.map { it.pillBg }.toSet().size)
    }

    @Test
    fun testTwoFocusModesOperation() {
        val modes = DashboardFocusMode.values()
        assertEquals(2, modes.size)

        val satgasMode = DashboardFocusMode.SATGAS_TRC
        val adminMode = DashboardFocusMode.ADMINISTRASI

        assertEquals("SATGAS", satgasMode.tag)
        assertEquals("ADMIN", adminMode.tag)
        assertTrue(satgasMode.label.contains("Satgas"))
        assertTrue(adminMode.label.contains("Admin"))
    }
}
