package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.AppStateSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {
    @Query("SELECT * FROM app_state_settings")
    fun getAllStateSettings(): Flow<List<AppStateSetting>>

    @Query("SELECT * FROM app_state_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSetting(key: String): AppStateSetting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: AppStateSetting)

    @Query("DELETE FROM app_state_settings WHERE `key` = :key")
    suspend fun deleteSetting(key: String)

    @Query("DELETE FROM app_state_settings")
    suspend fun clearAllSettings()
}
