package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.shared.models.AppFocusMode
import com.example.shared.models.AppVisualTheme
import com.example.shared.utils.AppThemePreferences
import com.example.shared.utils.RecordBadgeState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ThemeAndBadgeDeepSyncTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `test AppThemePreferences save and load roundtrip`() {
        AppThemePreferences.saveVisualTheme(context, AppVisualTheme.MINIMAL_PEARL)
        val loadedTheme = AppThemePreferences.loadVisualTheme(context)
        assertEquals(AppVisualTheme.MINIMAL_PEARL, loadedTheme)

        AppThemePreferences.saveFocusMode(context, AppFocusMode.ADMINISTRASI)
        val loadedFocus = AppThemePreferences.loadFocusMode(context)
        assertEquals(AppFocusMode.ADMINISTRASI, loadedFocus)

        // Reset to default
        AppThemePreferences.saveVisualTheme(context, AppVisualTheme.EMERALD_AURORA)
        AppThemePreferences.saveFocusMode(context, AppFocusMode.SATGAS_TRC)
    }

    @Test
    fun `test RecordBadgeState watermarks and delta calculation`() {
        val state = RecordBadgeState(context, mapOf("forum" to 12))
        assertEquals(0, state.badgeFor("forum", 12))

        val delta = state.badgeFor("forum", 15)
        assertEquals(3, delta)

        state.markSeen("forum", 15)
        assertEquals(0, state.badgeFor("forum", 15))
    }
}
