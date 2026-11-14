package com.example

import com.example.core.RoleManager
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import org.junit.Assert.*
import org.junit.Test

class RoleManagerValidationTest {

    private fun createMember(
        id: String,
        name: String,
        role: MemberRole,
        status: VerificationStatus = VerificationStatus.VERIFIED
    ) = DriverMember(
        id = id,
        name = name,
        driverId = "KTA-$id",
        phone = "0812345678$id",
        role = role,
        motorcyclePlate = "N 1234 $id",
        motorcycleModel = "Vario 160",
        verificationStatus = status,
        canManageKas = role == MemberRole.BENDAHARA || role == MemberRole.KETUA || role == MemberRole.SUPER_ADMIN,
        canVerifyDrivers = role == MemberRole.SEKRETARIS || role == MemberRole.KETUA || role == MemberRole.SUPER_ADMIN,
        canBroadcastSos = role == MemberRole.SATGAS || role == MemberRole.KETUA || role == MemberRole.SUPER_ADMIN,
        canManagePosko = role == MemberRole.SATGAS || role == MemberRole.KETUA || role == MemberRole.SUPER_ADMIN
    )

    @Test
    fun testNullActorRejected() {
        val target = createMember("02", "Budi", MemberRole.ANGGOTA)
        val (isValid, _) = RoleManager.validateRoleChange(null, target, MemberRole.WAKIL_KETUA, listOf(target))
        assertFalse(isValid)
    }

    @Test
    fun testRegularMemberCannotChangeRoles() {
        val actor = createMember("01", "Joko", MemberRole.ANGGOTA)
        val target = createMember("02", "Budi", MemberRole.ANGGOTA)
        val (isValid, _) = RoleManager.validateRoleChange(actor, target, MemberRole.WAKIL_KETUA, listOf(actor, target))
        assertFalse(isValid)
    }

    @Test
    fun testPromoteToSuperAdminBlocked() {
        val actor = createMember("01", "Ketua", MemberRole.KETUA)
        val target = createMember("02", "Budi", MemberRole.ANGGOTA)
        val (isValid, _) = RoleManager.validateRoleChange(actor, target, MemberRole.SUPER_ADMIN, listOf(actor, target))
        assertFalse(isValid)
    }

    @Test
    fun testSeedSuperAdminProtected() {
        val actor = createMember("01", "Ketua", MemberRole.KETUA)
        val seedId = com.example.BuildConfig.SEED_SUPER_ADMIN_ID
        val seed = createMember(seedId, "Seed", MemberRole.SUPER_ADMIN)
        val (isValid, _) = RoleManager.validateRoleChange(actor, seed, MemberRole.ANGGOTA, listOf(actor, seed))
        assertFalse(isValid)
    }

    @Test
    fun testOnlyKetuaOrSuperAdminCanAppointKetua() {
        val actor = createMember("01", "Sekretaris", MemberRole.SEKRETARIS)
        val target = createMember("02", "Budi", MemberRole.ANGGOTA)
        val (isValid, _) = RoleManager.validateRoleChange(actor, target, MemberRole.KETUA, listOf(actor, target))
        assertFalse(isValid)
    }

    @Test
    fun testCannotDemoteLastKetuaWithoutSuperAdmin() {
        val ketua = createMember("01", "Ketua", MemberRole.KETUA)
        val (isValid, _) = RoleManager.validateRoleChange(ketua, ketua, MemberRole.ANGGOTA, listOf(ketua))
        assertFalse(isValid)
    }

    @Test
    fun testValidRoleChangeByKetua() {
        val actor = createMember("01", "Ketua", MemberRole.KETUA)
        val target = createMember("02", "Budi", MemberRole.ANGGOTA)
        val all = listOf(actor, target, createMember("03", "Ketua2", MemberRole.KETUA))
        val (isValid, msg) = RoleManager.validateRoleChange(actor, target, MemberRole.WAKIL_KETUA, all)
        assertTrue(msg.isNotBlank())
        assertTrue(isValid)
    }

    @Test
    fun testSuperAdminCanDemoteLastKetua() {
        val actor = createMember("00", "Seed", MemberRole.SUPER_ADMIN)
        val target = createMember("02", "Ketua", MemberRole.KETUA)
        val all = listOf(actor, target)
        val (isValid, _) = RoleManager.validateRoleChange(actor, target, MemberRole.ANGGOTA, all)
        assertTrue(isValid)
    }
}