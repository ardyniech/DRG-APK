package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.core.repository.PoskoCheckInRepository
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.PoskoLocation
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
class PoskoCheckInRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repo: PoskoCheckInRepository

    private val testDriver = DriverMember(
        id = "DRG-999",
        name = "Slamet Driver",
        driverId = "DRG-999",
        phone = "081234567890",
        motorcyclePlate = "N 9999 XX",
        motorcycleModel = "Aerox 155",
        role = MemberRole.ANGGOTA,
        loyaltyPoints = 100
    )

    private val testPosko = PoskoLocation(
        id = "POSKO-01",
        name = "Posko Utama Alun-Alun",
        area = "Klojen",
        address = "Jl. Merdeka Barat No. 1, Malang",
        lat = -7.9826,
        lng = 112.6308,
        coordinatorName = "Pakde Bambang",
        phone = "081333444555",
        facilities = "Kopi gratis, Wi-Fi 100Mbps, Pompa Ban, Tempat Istirahat",
        activeDriversCount = 8,
        isMainPosko = true
    )

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repo = PoskoCheckInRepository(db)
        db.memberDao().insertMember(testDriver)
        db.poskoDao().insertPosko(testPosko)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testRecordCheckInAndAwardPoints() = runTest {
        val initialCount = repo.getCheckInCount("DRG-999")
        assertEquals(0, initialCount)

        val result = repo.recordCheckIn(
            member = testDriver,
            posko = testPosko,
            pointsAwarded = 15,
            method = "GEOFENCE_AUTO"
        )
        assertTrue(result.isSuccess)

        val checkIns = repo.getMemberCheckIns("DRG-999").first()
        assertEquals(1, checkIns.size)
        assertEquals("Posko Utama Alun-Alun", checkIns[0].poskoName)
        assertEquals(15, checkIns[0].pointsAwarded)

        // Verify driver received points
        val updatedDriver = db.memberDao().getMemberById("DRG-999")
        assertNotNull(updatedDriver)
        assertEquals(115, updatedDriver?.loyaltyPoints)
    }
}
