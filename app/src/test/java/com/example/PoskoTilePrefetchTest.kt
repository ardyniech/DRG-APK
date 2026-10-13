package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.battery.BatteryOptimizationManager
import com.example.core.cache.PoskoTilePrefetchManager
import com.example.core.cache.TileMetadataCacheEngine
import com.example.core.database.AppDatabase
import com.example.core.location.PoskoTileCoordinateCalculator
import com.example.shared.models.MapTileMetadata
import com.example.shared.models.PoskoLocation
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PoskoTilePrefetchTest {
    private lateinit var db: AppDatabase
    private lateinit var engine: TileMetadataCacheEngine
    private lateinit var batteryManager: BatteryOptimizationManager
    private lateinit var prefetchManager: PoskoTilePrefetchManager

    private val samplePosko = PoskoLocation(
        id = "POSKO-SINGOSARI",
        name = "Posko Singosari TRC",
        address = "Jl. Raya Singosari",
        area = "Malang Utara",
        lat = -7.8921,
        lng = 112.6644,
        phone = "08123456789",
        facilities = "Shelter, Wi-Fi",
        coordinatorName = "Pak Slamet"
    )

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        engine = TileMetadataCacheEngine(db.mapTileDao(), db.appStateDao())
        batteryManager = BatteryOptimizationManager()
        prefetchManager = PoskoTilePrefetchManager(engine, batteryManager)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testBoundingTileCalculationForPosko() {
        val tilesZ14 = PoskoTileCoordinateCalculator.calculateBoundingTiles(
            samplePosko.lat, samplePosko.lng, zoom = 14, radiusMeters = 1000.0
        )
        assertTrue("Tiles at zoom 14 should not be empty", tilesZ14.isNotEmpty())
        assertEquals(14, tilesZ14.first().zoom)
    }

    @Test
    fun testPrefetchTilesForPoskoStoresOfflinePersisted() = runTest {
        batteryManager.setPowerSaverMode(false)
        val result = prefetchManager.prefetchTilesForPosko(samplePosko)
        assertTrue(result.isSuccess)
        val res = result.getOrNull()
        assertNotNull(res)
        assertTrue((res?.totalTilesCached ?: 0) > 0)
        assertEquals(listOf(14, 15, 16), res?.zoomLevels)
        assertFalse(res?.isThrottledByPowerSaver == true)

        val isCached = prefetchManager.isPoskoCachedOffline(samplePosko.id)
        assertTrue("Posko should be marked as cached offline", isCached)
        val persistedCount = db.mapTileDao().getPersistedOfflineTileCountSync()
        assertTrue("Persisted offline tiles count should be > 0", persistedCount > 0)
    }

    @Test
    fun testPowerSaverModeThrottlesTilePrefetch() = runTest {
        batteryManager.setPowerSaverMode(true)
        val result = prefetchManager.prefetchTilesForPosko(samplePosko)
        assertTrue(result.isSuccess)
        val res = result.getOrNull()
        assertTrue(res?.isThrottledByPowerSaver == true)
        assertEquals(listOf(14), res?.zoomLevels)
    }

    @Test
    fun testPersistedOfflineTilesAreProtectedFromEviction() = runTest {
        db.mapTileDao().insertOrUpdateTile(
            MapTileMetadata(tileKey = "temp_1", zoom = 14, tileX = 1, tileY = 1, isPersistedOffline = false)
        )
        db.mapTileDao().insertOrUpdateTile(
            MapTileMetadata(tileKey = "posko_offline_1", zoom = 14, tileX = 2, tileY = 2, isPersistedOffline = true)
        )

        db.mapTileDao().evictOldestTiles(1)
        assertNull("Transient tile should be evicted", db.mapTileDao().getTileMetadata("temp_1"))
        assertNotNull("Persisted offline posko tile must be protected from eviction", db.mapTileDao().getTileMetadata("posko_offline_1"))
    }

    @Test
    fun testAdversarialBadInputCoordinatesHandledGracefully() = runTest {
        val badPosko = samplePosko.copy(lat = Double.NaN, lng = Double.NaN)
        val result = prefetchManager.prefetchTilesForPosko(badPosko)
        assertTrue("NaN coordinates must return failure", result.isFailure)

        val badCoords = PoskoTileCoordinateCalculator.calculateBoundingTiles(Double.NaN, 112.0, 14)
        assertTrue("NaN coordinates in calculator must return empty list", badCoords.isEmpty())
    }
}
