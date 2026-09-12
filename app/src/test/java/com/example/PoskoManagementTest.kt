package com.example

import com.example.core.database.AppDatabase
import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PoskoManagementTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: DRGRepository

    @Before
    fun setup() {
        val context = RuntimeEnvironment.getApplication()
        db = androidx.room.Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = DRGRepository(db)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testAddPoskoHappyPath() = runTest {
        val initialPoskoList = repository.allPosko.first()
        assertTrue(initialPoskoList.isEmpty())

        val newPosko = PoskoLocation(
            id = "PSKO-TEST-1",
            name = "Posko Sukun Hebat",
            area = "Sukun, Malang",
            address = "Jl. Sukun No. 45",
            lat = -7.9912,
            lng = 112.6123,
            coordinatorName = "Pak Sukun",
            phone = "0812345678",
            facilities = "Rest Area, Charger, Kopi",
            activeDriversCount = 0,
            isMainPosko = false
        )

        repository.addPosko(newPosko)

        val updatedPoskoList = repository.allPosko.first()
        assertEquals(1, updatedPoskoList.size)
        assertEquals("Posko Sukun Hebat", updatedPoskoList[0].name)
        assertEquals("0812345678", updatedPoskoList[0].phone)
    }

    @Test
    fun testPoskoAccessControlAdversarial() = runTest {
        val regularMember = DriverMember(
            id = "DRG-002",
            name = "Ardiansyah",
            driverId = "DRG-002",
            phone = "082152124899",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 3528 EGF",
            motorcycleModel = "Honda Vario",
            canManagePosko = false
        )

        val pengurusMember = DriverMember(
            id = "DRG-001",
            name = "Ardiansyah Admin",
            driverId = "DRG-001",
            phone = "082152124899",
            role = MemberRole.WAKIL_KETUA,
            motorcyclePlate = "N 3528 EGF",
            motorcycleModel = "Honda PCX",
            canManagePosko = true
        )

        assertFalse(regularMember.canManagePosko)
        assertFalse(regularMember.role.isLeadership)

        assertTrue(pengurusMember.canManagePosko)
        assertTrue(pengurusMember.role.isLeadership)
    }
}
