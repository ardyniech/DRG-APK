package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<BadgeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeItem>)

    @Query("SELECT * FROM community_tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<CommunityTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: CommunityTask)

    @Update
    suspend fun updateTask(task: CommunityTask)

    @Query("SELECT * FROM rewards ORDER BY requiredPoints ASC")
    fun getAllRewards(): Flow<List<RewardItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewards(rewards: List<RewardItem>)

    @Update
    suspend fun updateReward(reward: RewardItem)

    @Query("SELECT * FROM point_transactions ORDER BY timestamp DESC")
    fun getAllPointTransactions(): Flow<List<PointTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointTransaction(tx: PointTransaction)
}
