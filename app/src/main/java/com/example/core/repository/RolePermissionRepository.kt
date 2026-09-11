package com.example.core.repository

import androidx.room.withTransaction
import com.example.core.RoleManager
import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RolePermissionRepository(private val db: AppDatabase) {

    val allPermissions: Flow<List<MemberRolePermissionEntity>> =
        db.memberRolePermissionDao().getAllPermissions()

    val allAuditLogs: Flow<List<RoleAuditLogEntity>> =
        db.roleAuditLogDao().getAllLogs()

    fun observeUserPermission(memberId: String): Flow<MemberRolePermissionEntity?> =
        db.memberRolePermissionDao().observePermissionByMemberId(memberId)

    fun canAccessFinancialReports(memberId: String): Flow<Boolean> =
        observeUserPermission(memberId).map { perm ->
            perm?.canManageKas == true || perm?.role in listOf(
                MemberRole.KETUA, MemberRole.BENDAHARA, MemberRole.SEKRETARIS
            )
        }

    fun canAccessSosSettings(memberId: String): Flow<Boolean> =
        observeUserPermission(memberId).map { perm ->
            perm?.canBroadcastSos == true || perm?.role in listOf(
                MemberRole.KETUA, MemberRole.WAKIL_KETUA, MemberRole.SATGAS
            )
        }

    fun canAccessAdminDashboard(memberId: String): Flow<Boolean> =
        observeUserPermission(memberId).map { perm ->
            perm?.role in listOf(
                MemberRole.KETUA, MemberRole.WAKIL_KETUA,
                MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.DEWAN_ETIKA
            )
        }

    fun canVerifyNewDrivers(memberId: String): Flow<Boolean> =
        observeUserPermission(memberId).map { perm ->
            perm?.canVerifyDrivers == true || perm?.role in listOf(
                MemberRole.KETUA, MemberRole.SEKRETARIS, MemberRole.DEWAN_ETIKA
            )
        }

    suspend fun updateMemberRoleAndPermissions(
        targetMember: DriverMember,
        newRole: MemberRole,
        canManageKas: Boolean,
        canVerifyDrivers: Boolean,
        canBroadcastSos: Boolean,
        canManagePosko: Boolean,
        verificationStatus: VerificationStatus,
        actorMember: DriverMember,
        reason: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val allMembers = db.memberDao().getAllMembers().first()
        val (isValid, message) = RoleManager.validateRoleChange(
            actor = actorMember,
            target = targetMember,
            newRole = newRole,
            allMembers = allMembers
        )
        if (!isValid) {
            return@withContext Result.failure(Exception(message))
        }

        db.withTransaction {
            val entity = MemberRolePermissionEntity(
                memberId = targetMember.id,
                role = newRole,
                assignedByMemberId = actorMember.id,
                assignedByMemberName = actorMember.name,
                assignedTimestamp = System.currentTimeMillis(),
                canManageKas = canManageKas,
                canVerifyDrivers = canVerifyDrivers,
                canBroadcastSos = canBroadcastSos,
                canManagePosko = canManagePosko,
                verificationStatus = verificationStatus,
                notes = reason
            )
            db.memberRolePermissionDao().upsertPermission(entity)
            db.memberDao().updateMember(targetMember.copy(role = newRole, verificationStatus = verificationStatus))
            db.roleAuditLogDao().insertLog(
                RoleAuditLogEntity(
                    id = "AUDIT-${System.currentTimeMillis()}-${targetMember.id.takeLast(4)}",
                    targetMemberId = targetMember.id,
                    targetMemberName = targetMember.name,
                    previousRole = targetMember.role.name,
                    newRole = newRole.name,
                    actionByMemberId = actorMember.id,
                    actionByMemberName = actorMember.name,
                    reason = reason,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        Result.success(Unit)
    }
}
