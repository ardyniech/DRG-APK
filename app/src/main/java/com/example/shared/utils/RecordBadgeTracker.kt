package com.example.shared.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

private const val BADGE_PREF_NAME = "drg_launcher_seen_records"

fun calculateRecordBadgeDelta(currentCount: Int, seenCount: Int): Int {
    if (currentCount <= 0) return 0
    return (currentCount - seenCount.coerceAtLeast(0)).coerceAtLeast(0)
}

fun reconcileRecordSeenWatermark(currentCount: Int, seenCount: Int): Int =
    seenCount.coerceIn(0, currentCount.coerceAtLeast(0))

/**
 * Shared record-change tracker synchronized via SharedPreferences across Dashboard launcher tiles,
 * Community sub-tabs, and Bottom Nav badges.
 */
class RecordBadgeState(
    private val context: Context?,
    initialCounts: Map<String, Int>
) {
    var seenMap by mutableStateOf(initialCounts)
        private set

    fun badgeFor(tileId: String, currentCount: Int, alwaysShowTotal: Boolean = false): Int {
        if (alwaysShowTotal) return currentCount.coerceAtLeast(0)
        val seen = reconcileRecordSeenWatermark(currentCount, seenMap[tileId] ?: 0)
        return calculateRecordBadgeDelta(currentCount, seen)
    }

    fun markSeen(tileId: String, currentCount: Int) {
        val normalized = currentCount.coerceAtLeast(0)
        if (seenMap[tileId] == normalized) return
        seenMap = seenMap + (tileId to normalized)
        context?.getSharedPreferences(BADGE_PREF_NAME, Context.MODE_PRIVATE)
            ?.edit()
            ?.putInt(tileId, normalized)
            ?.apply()
    }

    fun refreshFromPrefs(keys: List<String>) {
        val prefs = context?.getSharedPreferences(BADGE_PREF_NAME, Context.MODE_PRIVATE) ?: return
        seenMap = keys.associateWith { prefs.getInt(it, 0) }
    }
}

@Composable
fun rememberSharedRecordBadgeState(keys: List<String>): RecordBadgeState {
    val context = LocalContext.current
    val state = remember(context, keys) {
        val prefs = context.getSharedPreferences(BADGE_PREF_NAME, Context.MODE_PRIVATE)
        RecordBadgeState(context, keys.associateWith { prefs.getInt(it, 0) })
    }
    DisposableEffect(context, keys) {
        val prefs = context.getSharedPreferences(BADGE_PREF_NAME, Context.MODE_PRIVATE)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            state.refreshFromPrefs(keys)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
    return state
}
