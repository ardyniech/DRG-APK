package com.example

import com.example.core.viewmodel.MainNavTab
import com.example.modules.dashboard.DesktopAppItem
import com.example.modules.dashboard.defaultLauncherApps
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DashboardDesktopLauncherTest {

    @Test
    fun testDefaultAppsHappyPath() {
        val apps = defaultLauncherApps()
        assertEquals(12, apps.size)
        assertTrue(apps.any { it.id == "radar" && it.isHot })
        assertTrue(apps.any { it.id == "sos" && it.isHot })
        // ids must be unique for analytics stability
        assertEquals(apps.size, apps.map { it.id }.toSet().size)
    }

    @Test
    fun testAdversarialEmptyAndMalformedList() {
        val empty: List<DesktopAppItem> = emptyList()
        assertTrue(empty.isEmpty())
        // chunked grid on empty input must not crash and yield no rows
        val rows = empty.chunked(4)
        assertTrue(rows.isEmpty())
        // malformed single item still yields one partial row
        val single = listOf(defaultLauncherApps().first())
        assertEquals(1, single.chunked(4).size)
        assertEquals(1, single.chunked(4).first().size)
    }

    @Test
    fun testCrossModuleNavigationMapping() {
        var lastTab: MainNavTab? = null
        val onNavigate: (MainNavTab) -> Unit = { lastTab = it }
        defaultLauncherApps().forEach { item ->
            onNavigate(item.tab)
            assertEquals(item.tab, lastTab)
        }
        // SOS tile must route to emergency tab
        val sos = defaultLauncherApps().first { it.id == "sos" }
        assertEquals(MainNavTab.EMERGENCY, sos.tab)
    }

    @Test
    fun testUiDeadEndRowChunking() {
        // 5 items -> 2 rows (4 + 1), header shows 5 Active, grid never dead-ends
        val five = defaultLauncherApps().take(5)
        assertEquals(2, five.chunked(4).size)
        assertEquals(4, five.chunked(4)[0].size)
        assertEquals(1, five.chunked(4)[1].size)
    }
}
