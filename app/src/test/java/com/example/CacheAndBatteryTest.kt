package com.example

import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.DRGCacheManager
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.sync.BackgroundSyncEngine
import com.example.core.sync.SyncStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CacheAndBatteryTest {

    @Test
    fun testBatteryOptimizationManagerDefaultsAndToggles() {
        val manager = BatteryOptimizationManager(sharedPrefs = null)

        assertFalse("Default power saver should be disabled", manager.isPowerSaverMode.value)
        assertTrue("Default data saver should be enabled", manager.isDataSaverMode.value)

        manager.setPowerSaverMode(true)
        assertTrue(manager.isPowerSaverMode.value)
        assertEquals(5000, manager.getAnimationThrottleMs())
        assertFalse(manager.shouldRenderRadarSweep())

        val initialData = manager.savedDataBytes.value
        manager.recordDataSaved(1024L * 1024L * 5L) // 5 MB
        assertEquals(initialData + (1024L * 1024L * 5L), manager.savedDataBytes.value)
    }

    @Test
    fun testBackgroundSyncEngineOptimisticEnqueueAndReconciliation() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)
        val syncEngine = BackgroundSyncEngine(testScope, testDispatcher)

        assertEquals(SyncStatus.IDLE, syncEngine.syncStatus.value)
        assertEquals(0, syncEngine.getPendingCount())

        syncEngine.enqueueOptimisticAction("HAZARD", "HZD-999", "Begal Area")
        assertEquals(1, syncEngine.getPendingCount())
        assertEquals(SyncStatus.OFFLINE_SAVED, syncEngine.syncStatus.value)

        testScope.advanceUntilIdle()

        assertEquals("Queue should clear after optimistic background sync", 0, syncEngine.getPendingCount())
        assertEquals(SyncStatus.IDLE, syncEngine.syncStatus.value)
    }

    @Test
    fun testMapCachePolicyAndOfflineConfiguration() {
        DRGCacheManager.cachePolicy = MapCachePolicy.CACHE_FIRST
        DRGCacheManager.isOfflineModeForced = false
        assertEquals(MapCachePolicy.CACHE_FIRST, DRGCacheManager.cachePolicy)
        assertFalse(DRGCacheManager.isOfflineModeForced)

        DRGCacheManager.cachePolicy = MapCachePolicy.OFFLINE_ONLY
        DRGCacheManager.isOfflineModeForced = true
        assertEquals(MapCachePolicy.OFFLINE_ONLY, DRGCacheManager.cachePolicy)
        assertTrue(DRGCacheManager.isOfflineModeForced)
    }

    @Test
    fun testBatteryAwareLocationSyncerAdaptiveIntervals() = runTest {
        var syncedCount = 0
        var lastSyncedLat = 0.0
        var lastSyncedLng = 0.0

        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testScope = TestScope(testDispatcher)

        val syncer = BatteryAwareLocationSyncer(
            context = null,
            scope = testScope,
            coroutineDispatcher = testDispatcher,
            onSyncLocation = { lat, lng ->
                syncedCount++
                lastSyncedLat = lat
                lastSyncedLng = lng
            }
        )

        syncer.startSync(LocationSyncPowerProfile.ADAPTIVE_ECO)
        testScheduler.runCurrent() // Initial trigger
        assertEquals(1, syncedCount)
        assertTrue("Synced lat should be within reasonable bounds", lastSyncedLat != 0.0)

        syncer.setProfile(LocationSyncPowerProfile.ULTRA_SAVER)
        testScheduler.runCurrent()
        val metrics = syncer.metrics.value
        assertEquals(LocationSyncPowerProfile.ULTRA_SAVER, metrics.profile)

        syncer.stopSync()
    }
}
