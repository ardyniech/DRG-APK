package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.EmergencyType
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
class SosFabAndModalTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testEmergencyModalDefaultPayloadGeneration() = runTest {
        val selectedType = EmergencyType.MOGOK
        val locationInput = ""
        val messageInput = ""

        val effectiveLocation = if (locationInput.isBlank()) "Lokasi Terkini GPS" else locationInput.trim()
        val effectiveMessage = if (messageInput.isBlank()) "Memerlukan bantuan rekan (${selectedType.label})" else messageInput.trim()

        assertEquals("Lokasi Terkini GPS", effectiveLocation)
        assertEquals("Memerlukan bantuan rekan (Motor Mogok / Mesin Rusak)", effectiveMessage)

        val alert = EmergencyAlert(
            id = "EMG-FAB-001",
            driverId = "DRG-TEST",
            driverName = "Driver Test",
            driverPhone = "081999888777",
            plateNumber = "B 1234 XYZ",
            type = selectedType,
            message = effectiveMessage,
            lat = -6.2088,
            lng = 106.8456,
            locationName = effectiveLocation,
            timestamp = System.currentTimeMillis(),
            isActive = true
        )

        db.emergencyDao().insertAlert(alert)

        val active = db.emergencyDao().getActiveAlerts().first()
        assertEquals(1, active.size)
        assertEquals("EMG-FAB-001", active[0].id)
        assertEquals(EmergencyType.MOGOK, active[0].type)
        assertEquals("Lokasi Terkini GPS", active[0].locationName)
    }

    @Test
    fun testEmergencyModalCustomLocationAndMessage() = runTest {
        val selectedType = EmergencyType.BEGAL
        val locationInput = "Depan Gerbang Tol Pasar Minggu"
        val messageInput = "Dikejar 2 motor mencurigakan, butuh kumpul satgas"

        val effectiveLocation = if (locationInput.isBlank()) "Lokasi Terkini GPS" else locationInput.trim()
        val effectiveMessage = if (messageInput.isBlank()) "Memerlukan bantuan rekan (${selectedType.label})" else messageInput.trim()

        assertEquals("Depan Gerbang Tol Pasar Minggu", effectiveLocation)
        assertEquals("Dikejar 2 motor mencurigakan, butuh kumpul satgas", effectiveMessage)

        val alert = EmergencyAlert(
            id = "EMG-FAB-002",
            driverId = "DRG-TEST-2",
            driverName = "Suryo",
            driverPhone = "081222333444",
            plateNumber = "B 9999 DEF",
            type = selectedType,
            message = effectiveMessage,
            lat = -6.2900,
            lng = 106.8300,
            locationName = effectiveLocation,
            timestamp = System.currentTimeMillis(),
            isActive = true
        )

        db.emergencyDao().insertAlert(alert)
        val active = db.emergencyDao().getActiveAlerts().first()
        assertEquals(1, active.size)
        assertEquals("Depan Gerbang Tol Pasar Minggu", active[0].locationName)
        assertEquals("Dikejar 2 motor mencurigakan, butuh kumpul satgas", active[0].message)
    }
}
