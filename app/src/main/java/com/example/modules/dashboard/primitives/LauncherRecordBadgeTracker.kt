package com.example.modules.dashboard.primitives

import android.content.Context
import androidx.compose.runtime.Composable
import com.example.shared.utils.RecordBadgeState
import com.example.shared.utils.calculateRecordBadgeDelta
import com.example.shared.utils.reconcileRecordSeenWatermark
import com.example.shared.utils.rememberSharedRecordBadgeState

/**
 * Forwarding wrapper delegating to canonical shared RecordBadgeTracker so Dashboard dock,
 * Community sub-tabs, and Bottom Navigation stay strictly in sync.
 */
fun calculateLauncherBadgeDelta(currentCount: Int, seenCount: Int): Int =
    calculateRecordBadgeDelta(currentCount, seenCount)

fun reconcileSeenWatermark(currentCount: Int, seenCount: Int): Int =
    reconcileRecordSeenWatermark(currentCount, seenCount)

typealias LauncherSeenState = RecordBadgeState

@Composable
fun rememberLauncherSeenState(keys: List<String>): LauncherSeenState =
    rememberSharedRecordBadgeState(keys)
