package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
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
class MemberRolePermissionDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndRetrieveMemberRolePermissions() = runTest {
        val permission = MemberRolePermissionEntity(
            memberId = "DRG-001",
            role = MemberRole.BENDAHARA,
            assignedByMemberId = "DRG-KETUA",
            assignedByMemberName = "Ketua DRG",
            canManageKas = true,
            canVerifyDrivers = false,
            canBroadcastSos = true,
            canManagePosko = false,
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Diberikan hak akses kelola kas per rapat 2026"
        )

        db.memberRolePermissionDao().upsertPermission(permission)

        val retrieved = db.memberRolePermissionDao().getPermissionByMemberId("DRG-001")
        assertNotNull(retrieved)
        assertEquals(MemberRole.BENDAHARA, retrieved?.role)
        assertTrue(retrieved?.canManageKas == true)
        assertFalse(retrieved?.canVerifyDrivers == true)
        assertEquals("DRG-KETUA", retrieved?.assignedByMemberId)
    }

    @Test
    fun testRoleAuditLogInsertionAndQuery() = runTest {
        val auditLog = RoleAuditLogEntity(
            id = "LOG-001",
            targetMemberId = "DRG-002",
            targetMemberName = "Budi Hartono",
            previousRole = "ANGGOTA",
            newRole = "KORLAP_WILAYAH",
            actionByMemberId = "DRG-KETUA",
            actionByMemberName = "Ketua DRG",
            reason = "Kenaikan jabatan Korlap Wilayah Malang Utara"
        )

        db.roleAuditLogDao().insertLog(auditLog)

        val logs = db.roleAuditLogDao().getLogsForMember("DRG-002").first()
        assertEquals(1, logs.size)
        assertEquals("KORLAP_WILAYAH", logs[0].newRole)
        assertEquals("ANGGOTA", logs[0].previousRole)
        assertEquals("Ketua DRG", logs[0].actionByMemberName)
    }
}
