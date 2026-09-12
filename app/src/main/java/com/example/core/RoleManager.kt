package com.example.core

import com.example.shared.models.AccessLevel
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.UserRole

object RoleManager {
    
    fun getUserRoleForMemberRole(memberRole: MemberRole): UserRole {
        return when (memberRole) {
            MemberRole.SUPER_ADMIN -> UserRole(
                accessLevel = AccessLevel.ADMIN,
                name = "Super Admin (Sistem)",
                description = "Otoritas mutlak untuk maintenance, scale up, pengawasan, dan kendali penuh seluruh sistem komunitas.",
                permissions = listOf("manage_members", "manage_roles", "approve_screening", "view_treasury_full", "post_emergency_announcement", "manage_posko")
            )
            MemberRole.KETUA, MemberRole.SEKRETARIS, MemberRole.DEWAN_ETIKA -> UserRole(
                accessLevel = AccessLevel.ADMIN,
                name = "Admin Komunitas",
                description = "Otoritas tertinggi pengelolaan anggota, persetujuan screening, dan pengaturan peran.",
                permissions = listOf("manage_members", "manage_roles", "approve_screening", "view_treasury_full", "post_emergency_announcement")
            )
            MemberRole.WAKIL_KETUA, MemberRole.BENDAHARA, MemberRole.SATGAS -> UserRole(
                accessLevel = AccessLevel.PENGURUS,
                name = "Pengurus Komunitas",
                description = "Akses operasional untuk pemantauan radar, pencatatan kas, dan bantuan tanggap darurat.",
                permissions = listOf("view_treasury_full", "post_emergency_announcement", "manage_posko", "view_members")
            )
            MemberRole.ANGGOTA -> UserRole(
                accessLevel = AccessLevel.MEMBER,
                name = "Anggota Driver",
                description = "Akses driver standar untuk fitur harian komunitas.",
                permissions = listOf("view_members", "view_posko", "add_review", "claim_task", "post_forum")
            )
        }
    }

    fun canManageKas(member: DriverMember?): Boolean {
        if (member == null) return false
        if (member.role == MemberRole.SUPER_ADMIN || member.role == MemberRole.KETUA) return true
        return member.canManageKas || member.role == MemberRole.BENDAHARA
    }

    fun canVerifyDrivers(member: DriverMember?): Boolean {
        if (member == null) return false
        if (member.role == MemberRole.SUPER_ADMIN || member.role == MemberRole.KETUA) return true
        return member.canVerifyDrivers || member.role in listOf(MemberRole.SEKRETARIS, MemberRole.DEWAN_ETIKA)
    }

    fun canBroadcastSos(member: DriverMember?): Boolean {
        if (member == null) return false
        if (member.role == MemberRole.SUPER_ADMIN || member.role == MemberRole.KETUA) return true
        return member.canBroadcastSos || member.role in listOf(MemberRole.WAKIL_KETUA, MemberRole.SATGAS)
    }

    fun canManagePosko(member: DriverMember?): Boolean {
        if (member == null) return false
        if (member.role == MemberRole.SUPER_ADMIN || member.role == MemberRole.KETUA) return true
        return member.canManagePosko || member.role in listOf(MemberRole.WAKIL_KETUA, MemberRole.SATGAS)
    }

    fun canManageRoles(member: DriverMember?): Boolean {
        if (member == null) return false
        return member.role in listOf(MemberRole.SUPER_ADMIN, MemberRole.KETUA, MemberRole.SEKRETARIS)
    }

    fun isSeedSuperAdmin(member: DriverMember?): Boolean {
        if (member == null) return false
        return member.id == com.example.BuildConfig.SEED_SUPER_ADMIN_ID && member.role == MemberRole.SUPER_ADMIN
    }

    fun validateRoleChange(
        actor: DriverMember?,
        target: DriverMember,
        newRole: MemberRole,
        allMembers: List<DriverMember>
    ): Pair<Boolean, String> {
        if (actor == null || !canManageRoles(actor)) {
            return false to "Hanya Super Admin, Ketua, dan Sekretaris yang berwenang mengatur jabatan."
        }
        if (target.id == com.example.BuildConfig.SEED_SUPER_ADMIN_ID) {
            return false to "Akun Seed Super Admin Sistem bersifat permanen dan terlindungi dari modifikasi peran."
        }
        if (newRole == MemberRole.SUPER_ADMIN) {
            return false to "Hak Super Admin terisolasi khusus untuk seed root sistem."
        }
        if (newRole == MemberRole.KETUA && actor.role != MemberRole.KETUA && actor.role != MemberRole.SUPER_ADMIN) {
            return false to "Hanya Ketua atau Super Admin yang berwenang mengangkat jabatan Ketua."
        }
        if (target.role == MemberRole.KETUA && newRole != MemberRole.KETUA) {
            val otherKetuaCount = allMembers.count { it.id != target.id && it.role == MemberRole.KETUA }
            if (otherKetuaCount == 0 && actor.role != MemberRole.SUPER_ADMIN) {
                return false to "Tidak dapat menurunkan Ketua: Komunitas wajib memiliki minimal 1 Ketua aktif."
            }
        }
        return true to "Validasi peran berhasil."
    }
}
