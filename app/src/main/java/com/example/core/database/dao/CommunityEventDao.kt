package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityEventDao {
    @Query("SELECT * FROM community_events ORDER BY createdAt DESC")
    fun getAllEvents(): Flow<List<CommunityEvent>>

    @Query("SELECT * FROM community_events WHERE id = :eventId LIMIT 1")
    fun observeEvent(eventId: String): Flow<CommunityEvent?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvent(event: CommunityEvent)

    @Delete
    suspend fun deleteEvent(event: CommunityEvent)

    @Query("SELECT * FROM event_committee_members WHERE eventId = :eventId ORDER BY role")
    fun getCommittee(eventId: String): Flow<List<EventCommitteeMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCommitteeMember(member: EventCommitteeMember)

    @Delete
    suspend fun deleteCommitteeMember(member: EventCommitteeMember)

    @Query("SELECT * FROM event_funding WHERE eventId = :eventId ORDER BY timestamp DESC")
    fun getFunding(eventId: String): Flow<List<EventFunding>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFunding(funding: EventFunding)

    @Delete
    suspend fun deleteFunding(funding: EventFunding)

    @Query("SELECT * FROM event_lpj_reports WHERE eventId = :eventId LIMIT 1")
    fun observeLpj(eventId: String): Flow<EventLpjReport?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLpj(report: EventLpjReport)

    @Query("SELECT * FROM event_stakeholders WHERE eventId = :eventId ORDER BY name")
    fun getStakeholders(eventId: String): Flow<List<EventStakeholder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStakeholder(stakeholder: EventStakeholder)

    @Delete
    suspend fun deleteStakeholder(stakeholder: EventStakeholder)
}
