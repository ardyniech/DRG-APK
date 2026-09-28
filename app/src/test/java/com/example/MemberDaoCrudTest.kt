package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
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
class MemberDaoCrudTest {

    private lateinit var db: AppDatabase

    private val sampleDriver = DriverMember(
        id = "MEM-001",
        name = "Ahmad Syafii",
        driverId = "DRG-7788",
        phone = "081298765432",
        motorcyclePlate = "N 1234 XY",
        motorcycleModel = "Honda Vario 160",
        role = MemberRole.ANGGOTA,
        verificationStatus = VerificationStatus.VERIFIED
    )

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testCreateAndReadOperations() = runTest {
        db.memberDao().insertMember(sampleDriver)

        val byId = db.memberDao().getMemberById("MEM-001")
        assertNotNull(byId)
        assertEquals("Ahmad Syafii", byId?.name)
        assertEquals("DRG-7788", byId?.driverId)
        assertEquals("081298765432", byId?.phone)
        assertEquals("N 1234 XY", byId?.motorcyclePlate)

        val byDriverId = db.memberDao().getMemberByDriverId("DRG-7788")
        assertEquals("MEM-001", byDriverId?.id)

        val byPhone = db.memberDao().getMemberByPhone("081298765432")
        assertEquals("MEM-001", byPhone?.id)

        val byPlate = db.memberDao().getMemberByPlate("N 1234 XY")
        assertEquals("MEM-001", byPlate?.id)

        val all = db.memberDao().getAllMembers().first()
        assertEquals(1, all.size)
    }

    @Test
    fun testUpdateOperation() = runTest {
        db.memberDao().insertMember(sampleDriver)

        val updated = sampleDriver.copy(
            phone = "081999888777",
            motorcyclePlate = "N 5678 ZZZ",
            currentStatus = "Sedang Narik"
        )
        db.memberDao().updateMember(updated)

        val retrieved = db.memberDao().getMemberById("MEM-001")
        assertEquals("081999888777", retrieved?.phone)
        assertEquals("N 5678 ZZZ", retrieved?.motorcyclePlate)
        assertEquals("Sedang Narik", retrieved?.currentStatus)
    }

    @Test
    fun testDeleteOperationsAndNotFound() = runTest {
        db.memberDao().insertMember(sampleDriver)
        assertEquals(1, db.memberDao().getCount())

        db.memberDao().deleteMember(sampleDriver)
        assertNull(db.memberDao().getMemberById("MEM-001"))
        assertNull(db.memberDao().getMemberByPhone("081298765432"))
        assertEquals(0, db.memberDao().getCount())

        // Negative/edge scenario: query non-existent ID
        val nonExistent = db.memberDao().getMemberById("NON-EXISTENT")
        assertNull(nonExistent)
    }
}
