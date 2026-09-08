package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.HazardArea
import kotlinx.coroutines.flow.Flow

@Dao
interface HazardDao {
    @Query("SELECT * FROM hazard_areas ORDER BY id DESC")
    fun getAllHazards(): Flow<List<HazardArea>>

    @Query("SELECT * FROM hazard_areas WHERE id = :hazardId LIMIT 1")
    suspend fun getHazardById(hazardId: String): HazardArea?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHazard(hazard: HazardArea)

    @Update
    suspend fun updateHazard(hazard: HazardArea)

    @Query("DELETE FROM hazard_areas WHERE id = :hazardId")
    suspend fun deleteHazard(hazardId: String)
}
