package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.NotificationPreference
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationPrefDao {
    @Query("SELECT * FROM notification_preferences WHERE memberId = :memberId")
    fun getPrefForMember(memberId: String): Flow<NotificationPreference?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePref(pref: NotificationPreference)
}
