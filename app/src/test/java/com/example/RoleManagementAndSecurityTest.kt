package com.example

import com.example.core.RoleManager
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import org.junit.Assert.*
import org.junit.Test

class RoleManagementAndSecurityTest {

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
    fun testRegularMemberCannotManageRoles() {
        val regularMember = createMember("01", "Joko", MemberRole.ANGGOTA)
        assertFalse(RoleManager.canManageRoles(regularMember))
        assertFalse(RoleManager.canManageRoles(null))
    }

    @Test
    fun testKetuaAndSekretarisCanManageRoles() {
        val ketua = createMember("01", "Budi", MemberRole.KETUA)
        val sekretaris = createMember("02", "Siti", MemberRole.SEKRETARIS)
        assertTrue(RoleManager.canManageRoles(ketua))
        assertTrue(RoleManager.canManageRoles(sekretaris))
    }

    @Test
    fun testSuperAdminHasAbsolutePower() {
        val superAdmin = createMember("00", "Dev", MemberRole.SUPER_ADMIN)
        assertTrue(RoleManager.canManageRoles(superAdmin))
        assertTrue(RoleManager.canManageKas(superAdmin))
        assertTrue(RoleManager.canVerifyDrivers(superAdmin))
        assertTrue(RoleManager.canBroadcastSos(superAdmin))
        assertTrue(RoleManager.canManagePosko(superAdmin))
    }

    @Test
    fun testOnlySuperAdminCanEstablishSuperAdmin() {
        val superAdmin = createMember("00", "Dev", MemberRole.SUPER_ADMIN)
        val ketua = createMember("01", "Budi", MemberRole.KETUA)
        val anggota = createMember("03", "Andi", MemberRole.ANGGOTA)
        val allMembers = listOf(superAdmin, ketua, anggota)

        // Promoting anyone to Super Admin is BLOCKED to protect root seed
        val (isValid1, msg1) = RoleManager.validateRoleChange(superAdmin, anggota, MemberRole.SUPER_ADMIN, allMembers)
        assertFalse(isValid1)
        assertTrue(msg1.contains("Hak Super Admin terisolasi khusus"))

        // Ketua promoting someone to Super Admin -> BLOCKED
        val (isValid2, msg2) = RoleManager.validateRoleChange(ketua, anggota, MemberRole.SUPER_ADMIN, allMembers)
        assertFalse(isValid2)
        assertTrue(msg2.contains("Hak Super Admin terisolasi khusus"))
    }

    @Test
    fun testCannotDemoteLastKetuaWithoutSuccessor() {
        val ketua = createMember("01", "Budi", MemberRole.KETUA)
        val anggota = createMember("02", "Joko", MemberRole.ANGGOTA)
        val allMembers = listOf(ketua, anggota)

        val (isValid, msg) = RoleManager.validateRoleChange(ketua, ketua, MemberRole.ANGGOTA, allMembers)
        assertFalse(isValid)
        assertTrue(msg.contains("minimal 1 Ketua aktif"))
    }

    @Test
    fun testSekretarisCannotAppointKetua() {
        val sekretaris = createMember("02", "Siti", MemberRole.SEKRETARIS)
        val anggota = createMember("03", "Andi", MemberRole.ANGGOTA)
        val allMembers = listOf(sekretaris, anggota)

        val (isValid, msg) = RoleManager.validateRoleChange(sekretaris, anggota, MemberRole.KETUA, allMembers)
        assertFalse(isValid)
        assertTrue(msg.contains("Hanya Ketua atau Super Admin yang berwenang"))
    }

    @Test
    fun testKetuaCanPromoteAnggotaToSatgas() {
        val ketua = createMember("01", "Budi", MemberRole.KETUA)
        val anggota = createMember("03", "Andi", MemberRole.ANGGOTA)
        val allMembers = listOf(ketua, anggota)

        val (isValid, _) = RoleManager.validateRoleChange(ketua, anggota, MemberRole.SATGAS, allMembers)
        assertTrue(isValid)
    }
}
