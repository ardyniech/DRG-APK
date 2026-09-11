package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.PoskoCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoskoCheckInDao {

    @Query("SELECT * FROM posko_check_ins ORDER BY checkInTimestamp DESC")
    fun getAllCheckIns(): Flow<List<PoskoCheckInEntity>>

    @Query("SELECT * FROM posko_check_ins WHERE memberId = :memberId ORDER BY checkInTimestamp DESC")
    fun getCheckInsByMember(memberId: String): Flow<List<PoskoCheckInEntity>>

    @Query("SELECT * FROM posko_check_ins WHERE poskoId = :poskoId ORDER BY checkInTimestamp DESC")
    fun getCheckInsByPosko(poskoId: String): Flow<List<PoskoCheckInEntity>>

    @Query("SELECT COUNT(*) FROM posko_check_ins WHERE memberId = :memberId")
    suspend fun getCheckInCountForMember(memberId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: PoskoCheckInEntity)
}
