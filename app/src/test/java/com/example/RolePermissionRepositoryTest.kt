package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.core.repository.RolePermissionRepository
import com.example.shared.models.*
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
class RolePermissionRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repo: RolePermissionRepository

    private val ketua = DriverMember(
        id = "DRG-001",
        name = "Ketua DRG",
        driverId = "DRG-001",
        phone = "0811111111",
        motorcycleModel = "NMAX",
        motorcyclePlate = "N 1234 AB",
        role = MemberRole.KETUA
    )

    private val anggota = DriverMember(
        id = "DRG-002",
        name = "Driver Anggota",
        driverId = "DRG-002",
        phone = "0822222222",
        motorcycleModel = "Vario",
        motorcyclePlate = "N 5678 CD",
        role = MemberRole.ANGGOTA
    )

    @Before
    fun setup() = runTest {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repo = RolePermissionRepository(db)

        db.memberDao().insertMember(ketua)
        db.memberDao().insertMember(anggota)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testAccessControlForFinancialAndSos() = runTest {
        // Anggota initially has no special permissions
        val canAccessFinanceInit = repo.canAccessFinancialReports("DRG-002").first()
        assertFalse(canAccessFinanceInit)

        // Grant Bendahara permissions
        val result = repo.updateMemberRoleAndPermissions(
            targetMember = anggota,
            newRole = MemberRole.BENDAHARA,
            canManageKas = true,
            canVerifyDrivers = false,
            canBroadcastSos = false,
            canManagePosko = false,
            verificationStatus = VerificationStatus.VERIFIED,
            actorMember = ketua,
            reason = "Promosi Bendahara"
        )
        assertTrue(result.isSuccess)

        val canAccessFinanceAfter = repo.canAccessFinancialReports("DRG-002").first()
        assertTrue(canAccessFinanceAfter)

        // Audit log was created
        val logs = repo.allAuditLogs.first()
        assertEquals(1, logs.size)
        assertEquals("BENDAHARA", logs[0].newRole)
    }
}
