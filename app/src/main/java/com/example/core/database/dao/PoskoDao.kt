package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.PoskoLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface PoskoDao {
    @Query("SELECT * FROM posko_locations ORDER BY isMainPosko DESC, name ASC")
    fun getAllPosko(): Flow<List<PoskoLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoskoList(poskoList: List<PoskoLocation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosko(posko: PoskoLocation)
}
