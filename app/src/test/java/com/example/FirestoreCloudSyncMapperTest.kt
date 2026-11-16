package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.core.firebase.FirestoreCloudSyncMapper
import com.example.shared.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FirestoreCloudSyncMapperTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun syncMemberToRoom_updatesRoleStatusAndLoyaltyFromCloud() = runTest {
        val local = DriverMember(
            id = "uid_driver_1",
            name = "Budi Driver",
            driverId = "DRG-UID1",
            phone = "081234567890",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1234 AB",
            motorcycleModel = "Beat",
            rating = 5.0f,
            reviewCount = 0,
            loyaltyPoints = 10,
            loyaltyTier = "Junior Rider",
            verificationStatus = VerificationStatus.PENDING_SCREENING,
            isVerified = false,
            isOnline = false,
            currentStatus = "Standby",
            baseArea = "Malang Kota"
        )
        db.memberDao().insertMember(local)

        val cloud = CloudCommunityMember(
            id = "uid_driver_1",
            userId = "uid_driver_1",
            name = "Budi Satgas",
            driverId = "DRG-UID1",
            baseArea = "Suhat",
            role = MemberRole.SATGAS.name,
            verificationStatus = VerificationStatus.VERIFIED.name,
            loyaltyPoints = 250L,
            isOnline = true
        )

        FirestoreCloudSyncMapper.syncMemberToRoom(db, cloud, "uid_driver_1")

        val updated = db.memberDao().getMemberById("uid_driver_1")!!
        assertEquals(MemberRole.SATGAS, updated.role)
        assertEquals(VerificationStatus.VERIFIED, updated.verificationStatus)
        assertTrue(updated.isVerified)
        assertEquals(250, updated.loyaltyPoints)
        assertEquals("081234567890", updated.phone)
    }

    @Test
    fun syncActiveAlertsToRoom_upsertsCloudAlertsAndResolvesStaleCloudAlerts() = runTest {
        val stale = EmergencyAlert(
            id = "SOS-OLD-1",
            driverId = "uid_1",
            driverName = "Lama",
            driverPhone = "",
            plateNumber = "",
            type = EmergencyType.MOGOK,
            message = "Sudah selesai di cloud",
            lat = -7.98,
            lng = 112.63,
            locationName = "Klojen",
            timestamp = 1000L,
            isActive = true
        )
        val localNonCloud = stale.copy(id = "LOCAL-TEMP-1", driverName = "Lokal")
        db.emergencyDao().insertAlert(stale)
        db.emergencyDao().insertAlert(localNonCloud)

        val cloudAlerts = listOf(
            CloudEmergencyAlert(
                id = "SOS-NEW-2",
                userId = "uid_2",
                driverName = "Andi Rider",
                alertType = EmergencyType.KECELAKAAN.name,
                message = "Kendaraan bermasalah di Dinoyo",
                locationName = "Dinoyo",
                latitude = -7.94,
                longitude = 112.61,
                status = "ACTIVE",
                respondersCount = 2L
            ),
            CloudEmergencyAlert(
                id = "", // malformed blank id must be ignored
                userId = "uid_3",
                driverName = "Invalid",
                alertType = "INVALID_TYPE",
                status = "ACTIVE"
            )
        )

        FirestoreCloudSyncMapper.syncActiveAlertsToRoom(db, cloudAlerts)

        val active = db.emergencyDao().getActiveAlerts().first()
        val ids = active.map { it.id }.toSet()
        assertTrue("SOS-NEW-2" in ids)
        assertTrue("LOCAL-TEMP-1" in ids)
        assertFalse("SOS-OLD-1" in ids)
        assertEquals(2, active.first { it.id == "SOS-NEW-2" }.responderCount)
    }

    @Test
    fun syncMemberToRoom_ignoresNullOrMissingLocalMemberSafely() = runTest {
        FirestoreCloudSyncMapper.syncMemberToRoom(db, null, "missing_id")
        FirestoreCloudSyncMapper.syncMemberToRoom(
            db,
            CloudCommunityMember(id = "x", userId = "x", name = "Ghost", role = "INVALID_ROLE"),
            ""
        )
        assertTrue(db.memberDao().getAllMembers().first().isEmpty())
    }
}
