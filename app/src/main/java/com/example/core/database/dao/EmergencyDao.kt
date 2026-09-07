package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.EmergencyAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM emergency_alerts ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<EmergencyAlert>>

    @Query("SELECT * FROM emergency_alerts WHERE isActive = 1 ORDER BY timestamp DESC")
    fun getActiveAlerts(): Flow<List<EmergencyAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: EmergencyAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<EmergencyAlert>)

    @Query("UPDATE emergency_alerts SET isActive = 0, resolvedBy = :resolvedBy WHERE id = :id")
    suspend fun resolveAlert(id: String, resolvedBy: String)

    @Query("UPDATE emergency_alerts SET responderCount = responderCount + 1 WHERE id = :id")
    suspend fun incrementResponder(id: String)
}
