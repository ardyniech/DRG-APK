package com.example.core

import com.example.shared.models.AccessLevel
import com.example.shared.models.MemberRole
import com.example.shared.models.UserRole

object RoleManager {
    
    fun getUserRoleForMemberRole(memberRole: MemberRole): UserRole {
        return when (memberRole) {
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

    fun hasPermission(memberRole: MemberRole, permission: String): Boolean {
        val userRole = getUserRoleForMemberRole(memberRole)
        return userRole.permissions.contains(permission)
    }

    fun canManageMembers(memberRole: MemberRole): Boolean {
        return hasPermission(memberRole, "manage_members")
    }

    fun canManageRoles(memberRole: MemberRole): Boolean {
        return hasPermission(memberRole, "manage_roles")
    }

    fun canApproveScreening(memberRole: MemberRole): Boolean {
        return hasPermission(memberRole, "approve_screening")
    }
}
