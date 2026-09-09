package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shared.models.PendingSyncEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM pending_sync_queue ORDER BY timestamp ASC")
    fun getAllPendingSyncs(): Flow<List<PendingSyncEntity>>

    @Query("SELECT * FROM pending_sync_queue ORDER BY timestamp ASC LIMIT 10")
    suspend fun getNextBatch(): List<PendingSyncEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncItem(item: PendingSyncEntity)

    @Query("DELETE FROM pending_sync_queue WHERE id = :id")
    suspend fun removeSyncItem(id: String)

    @Query("SELECT COUNT(*) FROM pending_sync_queue")
    fun getPendingCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM pending_sync_queue")
    suspend fun getPendingCount(): Int
}
