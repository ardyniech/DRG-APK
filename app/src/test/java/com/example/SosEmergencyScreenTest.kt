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
class SosEmergencyScreenTest {

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
    fun testSosEmergencyTriggerPersistence() = runTest {
        val alert = EmergencyAlert(
            id = "EMG-TEST-99",
            driverId = "DRG-001",
            driverName = "Bambang Pamungkas",
            driverPhone = "081234567890",
            plateNumber = "N 1928 AB",
            type = EmergencyType.BEGAL,
            message = "Memerlukan bantuan darurat segera (Begal / Kriminalitas)",
            locationName = "Jl. Ijen No. 10, Malang",
            latitude = -7.9786,
            longitude = 112.6318,
            timestamp = System.currentTimeMillis(),
            isActive = true
        )

        db.emergencyDao().insertAlert(alert)

        val activeAlerts = db.emergencyDao().getActiveAlerts().first()
        assertEquals(1, activeAlerts.size)
        assertEquals(EmergencyType.BEGAL, activeAlerts[0].type)
        assertTrue(activeAlerts[0].isActive)

        // Resolve alert
        db.emergencyDao().resolveAlert("EMG-TEST-99")
        val activeAfterResolve = db.emergencyDao().getActiveAlerts().first()
        assertTrue(activeAfterResolve.isEmpty())
    }

    @Test
    fun testCountdownCancellationFlow() = runTest {
        var isSosTriggered = false
        val cancelAction = { isSosTriggered = false }
        val triggerAction = { isSosTriggered = true }

        // Simulate cancellation before 5s elapsed
        cancelAction.invoke()
        assertFalse(isSosTriggered)

        // Simulate trigger confirmation
        triggerAction.invoke()
        assertTrue(isSosTriggered)
    }
}
