package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.WorkshopPartner
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkshopDao {
    @Query("SELECT * FROM workshop_partners ORDER BY distanceKm ASC")
    fun getAllWorkshops(): Flow<List<WorkshopPartner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkshops(workshops: List<WorkshopPartner>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkshop(workshop: WorkshopPartner)
}
