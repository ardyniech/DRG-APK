package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.DRGCacheManager
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.core.database.AppDatabase
import com.example.core.location.BatteryAwareLocationSyncer
import com.example.core.sync.BackgroundSyncEngine
import com.example.core.sync.SyncStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CacheAndBatteryTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

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
        val syncEngine = BackgroundSyncEngine(db, this, testDispatcher)

        assertEquals(SyncStatus.IDLE, syncEngine.syncStatus.value)
        assertEquals(0, db.syncQueueDao().getPendingCount())

        syncEngine.enqueueOptimisticAction("HAZARD", "HZD-999", "Begal Area")
        advanceUntilIdle()

        val count = db.syncQueueDao().getPendingCount()
        val status = syncEngine.syncStatus.value
        assertEquals(0, count)
        assertEquals(SyncStatus.IDLE, status)
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
