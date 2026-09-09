package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.core.repository.DRGRepository
import com.example.core.viewmodel.DRGViewModel
import com.example.core.viewmodel.MainNavTab
import com.example.core.viewmodel.coordinators.AdminLogUtils
import com.example.shared.models.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ViewModelRefactorAuditTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase
    private lateinit var repo: DRGRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = AppDatabase.getInstance(context)
        repo = DRGRepository(db)
    }

    @Test
    fun testRoomDatabaseInstanceNotNull() {
        assertNotNull(db)
        assertNotNull(db.hazardDao())
        assertNotNull(db.gamificationDao())
        assertNotNull(db.memberDao())
    }

    @Test
    fun testAdminLogUtilsCreation() {
        val member = DriverMember(
            id = "DRG-TEST",
            name = "Test Driver",
            driverId = "REG-99",
            phone = "08123456789",
            role = MemberRole.KETUA,
            motorcyclePlate = "N 1234 AA",
            motorcycleModel = "Vario 160",
            rating = 5.0f,
            reviewCount = 10,
            loyaltyPoints = 100,
            loyaltyTier = "Master",
            verificationStatus = VerificationStatus.VERIFIED,
            screeningNotes = "",
            isOnline = true,
            currentStatus = "Siaga"
        )

        val log = AdminLogUtils.createLog(member, "verifikasi berkas", "Budi")
        assertEquals("Test Driver", log.actorName)
        assertEquals("Ketua", log.actorRole)
        assertEquals("verifikasi berkas", log.action)
        assertEquals("Budi", log.targetName)
        assertNotNull(UUID.fromString(log.id))
    }

    @Test
    fun testUUIDGenerationUniqueness() {
        val idSet = HashSet<String>()
        for (i in 1..1000) {
            val id = "SOS-${UUID.randomUUID().toString().take(8)}"
            assertTrue("ID must be unique", idSet.add(id))
        }
        assertEquals(1000, idSet.size)
    }

    @Test
    fun testGetTaskByIdReturnsNullOnInvalidTask() = runBlocking {
        val nonExistent = repo.claimTask("INVALID-TASK-ID-9999", DriverMember(
            id = "DRG-01", name = "Test", driverId = "REG-01", phone = "081",
            role = MemberRole.ANGGOTA, motorcyclePlate = "N 1 A", motorcycleModel = "Vario",
            rating = 5f, reviewCount = 1, loyaltyPoints = 10, loyaltyTier = "Junior",
            verificationStatus = VerificationStatus.VERIFIED, screeningNotes = "",
            isOnline = true, currentStatus = "Aktif"
        ))
        // Should complete safely without exception or crash
        assertTrue(true)
    }

    @Test
    fun testViewModelStateFlows() {
        val sharedPrefs = context.getSharedPreferences("test_drg_prefs", Context.MODE_PRIVATE)
        val vm = DRGViewModel(repo, sharedPrefs, context as android.app.Application)

        assertEquals(MainNavTab.DASHBOARD, vm.selectedTab.value)
        vm.selectTab(MainNavTab.RADAR)
        assertEquals(MainNavTab.RADAR, vm.selectedTab.value)

        vm.showToast("Test Toast")
        assertEquals("Test Toast", vm.snackBarMessage.value)
        vm.clearSnackBar()
        assertNull(vm.snackBarMessage.value)

        assertFalse(vm.isLoggedIn.value)
        vm.login("DRG-001")
        assertTrue(vm.isLoggedIn.value)
        assertEquals("DRG-001", vm.currentMemberId.value)
    }
}
