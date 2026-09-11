package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.MemberRole
import com.example.shared.models.MemberRolePermissionEntity
import com.example.shared.models.VerificationStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberRolePermissionDao {

    @Query("SELECT * FROM member_role_permissions WHERE memberId = :memberId")
    suspend fun getPermissionByMemberId(memberId: String): MemberRolePermissionEntity?

    @Query("SELECT * FROM member_role_permissions WHERE memberId = :memberId")
    fun observePermissionByMemberId(memberId: String): Flow<MemberRolePermissionEntity?>

    @Query("SELECT * FROM member_role_permissions")
    fun getAllPermissions(): Flow<List<MemberRolePermissionEntity>>

    @Query("SELECT * FROM member_role_permissions WHERE role = :role")
    fun getMembersByRole(role: MemberRole): Flow<List<MemberRolePermissionEntity>>

    @Query("SELECT * FROM member_role_permissions WHERE verificationStatus = :status")
    fun getMembersByVerificationStatus(status: VerificationStatus): Flow<List<MemberRolePermissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPermission(permission: MemberRolePermissionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(permissions: List<MemberRolePermissionEntity>)

    @Query("DELETE FROM member_role_permissions WHERE memberId = :memberId")
    suspend fun deletePermission(memberId: String)
}
