package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.RoleAuditLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleAuditLogDao {

    @Query("SELECT * FROM role_audit_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<RoleAuditLogEntity>>

    @Query("SELECT * FROM role_audit_logs WHERE targetMemberId = :targetMemberId ORDER BY timestamp DESC")
    fun getLogsForMember(targetMemberId: String): Flow<List<RoleAuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: RoleAuditLogEntity)

    @Query("SELECT COUNT(*) FROM role_audit_logs")
    suspend fun getLogCount(): Int

    @Query("DELETE FROM role_audit_logs")
    suspend fun clearLogs()
}
