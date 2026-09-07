package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.cache.TileMetadataCacheEngine
import com.example.core.database.AppDatabase
import com.example.shared.models.AppStateSetting
import com.example.shared.models.MapTileMetadata
import kotlinx.coroutines.flow.first
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
class MapTileCacheAndStateTest {

    private lateinit var db: AppDatabase
    private lateinit var cacheEngine: TileMetadataCacheEngine

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cacheEngine = TileMetadataCacheEngine(db.mapTileDao(), db.appStateDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testTileMetadataInsertionAndQuery() = runTest {
        val tile = MapTileMetadata(
            tileKey = "osm_15_26091_16993",
            zoom = 15,
            tileX = 26091,
            tileY = 16993,
            mapSource = "OSM",
            sizeBytes = 24500L
        )
        db.mapTileDao().insertOrUpdateTile(tile)

        val fetched = db.mapTileDao().getTileMetadata("osm_15_26091_16993")
        assertNotNull(fetched)
        assertEquals(15, fetched?.zoom)
        assertEquals(24500L, fetched?.sizeBytes)

        val allTiles = db.mapTileDao().getAllTileMetadata().first()
        assertEquals(1, allTiles.size)

        val totalSize = db.mapTileDao().getTotalCacheSizeBytes().first()
        assertEquals(24500L, totalSize)
    }

    @Test
    fun testTileCacheEngineRecordAndEvict() = runTest {
        cacheEngine.recordTileCached(14, 13045, 8496, "OSM", 20000L)
        cacheEngine.recordTileCached(14, 13046, 8496, "OSM", 22000L)

        val count = db.mapTileDao().getTotalTileCount().first()
        assertEquals(2, count)

        val hitSavedBytes = cacheEngine.recordTileHit("osm_14_13045_8496", 20000L)
        assertEquals(20000L, hitSavedBytes)

        val tile = db.mapTileDao().getTileMetadata("osm_14_13045_8496")
        assertEquals(2, tile?.accessCount)
    }

    @Test
    fun testAppStateDaoPersistenceAndRetrieval() = runTest {
        val setting = AppStateSetting(
            key = "map_cache_policy",
            stringValue = "CACHE_FIRST",
            boolValue = true
        )
        db.appStateDao().saveSetting(setting)

        val retrieved = db.appStateDao().getSetting("map_cache_policy")
        assertNotNull(retrieved)
        assertEquals("CACHE_FIRST", retrieved?.stringValue)
        assertTrue(retrieved?.boolValue == true)

        cacheEngine.saveStateString("current_radar_zoom", "16")
        assertEquals("16", cacheEngine.getStateString("current_radar_zoom"))

        cacheEngine.saveStateBoolean("power_saver_active", true)
        assertTrue(cacheEngine.getStateBoolean("power_saver_active"))
    }
}
