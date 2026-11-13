package com.example.shared.utils

import android.content.Context
import com.example.shared.models.AppFocusMode
import com.example.shared.models.AppVisualTheme

private const val THEME_PREF_FILE = "drg_prefs"
private const val KEY_VISUAL_THEME = "app_visual_theme"
private const val KEY_FOCUS_MODE = "app_focus_mode"

/**
 * Persists user-selected Luminous Glass theme & Focus Mode cleanly in SharedPreferences
 * with safe fallback when stored values are missing or malformed.
 */
object AppThemePreferences {

    fun loadVisualTheme(context: Context?): AppVisualTheme {
        val raw = context?.getSharedPreferences(THEME_PREF_FILE, Context.MODE_PRIVATE)
            ?.getString(KEY_VISUAL_THEME, null)
            .orEmpty()
        return AppVisualTheme.entries.firstOrNull { it.name == raw } ?: AppVisualTheme.EMERALD_AURORA
    }

    fun saveVisualTheme(context: Context?, theme: AppVisualTheme) {
        context?.getSharedPreferences(THEME_PREF_FILE, Context.MODE_PRIVATE)
            ?.edit()
            ?.putString(KEY_VISUAL_THEME, theme.name)
            ?.apply()
    }

    fun loadFocusMode(context: Context?): AppFocusMode {
        val raw = context?.getSharedPreferences(THEME_PREF_FILE, Context.MODE_PRIVATE)
            ?.getString(KEY_FOCUS_MODE, null)
            .orEmpty()
        return AppFocusMode.entries.firstOrNull { it.name == raw } ?: AppFocusMode.SATGAS_TRC
    }

    fun saveFocusMode(context: Context?, mode: AppFocusMode) {
        context?.getSharedPreferences(THEME_PREF_FILE, Context.MODE_PRIVATE)
            ?.edit()
            ?.putString(KEY_FOCUS_MODE, mode.name)
            ?.apply()
    }
}
