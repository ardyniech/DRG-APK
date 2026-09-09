package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shared.models.AdminLog
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminLogDao {
    @Query("SELECT * FROM admin_logs ORDER BY timestampMillis DESC")
    fun getAllLogs(): Flow<List<AdminLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AdminLog)
}
