package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.AttendanceEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<AttendanceEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<AttendanceEvent>)

    @Query("UPDATE attendance_events SET isCheckedInByMe = 1, attendedCount = attendedCount + 1 WHERE id = :id")
    suspend fun checkIn(id: String)
}
