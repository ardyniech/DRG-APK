package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityEventDao {
    @Query("SELECT * FROM community_events ORDER BY eventTimestamp DESC")
    fun getAllEvents(): Flow<List<CommunityEventEntity>>

    @Query("SELECT * FROM community_events WHERE id = :eventId LIMIT 1")
    fun observeEvent(eventId: String): Flow<CommunityEventEntity?>

    @Query("SELECT COUNT(*) FROM community_events")
    suspend fun getEventCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CommunityEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CommunityEventEntity>)

    @Update
    suspend fun updateEvent(event: CommunityEventEntity)

    @Query("UPDATE community_events SET stage = :stage, readinessNotes = :readinessNotes WHERE id = :eventId")
    suspend fun updateEventStage(eventId: String, stage: EventPhaseStage, readinessNotes: String)

    @Query("SELECT * FROM event_committee_tasks ORDER BY updatedAt DESC")
    fun getAllCommitteeTasks(): Flow<List<EventCommitteeTaskEntity>>

    @Query("SELECT * FROM event_committee_tasks WHERE eventId = :eventId ORDER BY updatedAt DESC")
    fun getTasksForEvent(eventId: String): Flow<List<EventCommitteeTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: EventCommitteeTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<EventCommitteeTaskEntity>)

    @Query("UPDATE event_committee_tasks SET progressPercent = :progress, status = :status, blockerNote = :blockerNote, updatedAt = :updatedAt WHERE id = :taskId")
    suspend fun updateTaskProgress(taskId: String, progress: Int, status: CommitteeTaskStatus, blockerNote: String, updatedAt: Long)

    @Query("SELECT * FROM event_member_feedback ORDER BY createdAt DESC")
    fun getAllEventFeedback(): Flow<List<EventMemberFeedbackEntity>>

    @Query("SELECT * FROM event_member_feedback WHERE eventId = :eventId ORDER BY createdAt DESC")
    fun getFeedbackForEvent(eventId: String): Flow<List<EventMemberFeedbackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: EventMemberFeedbackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedbackList(feedback: List<EventMemberFeedbackEntity>)

    @Query("UPDATE event_member_feedback SET committeeResponse = :response, isResolved = 1 WHERE id = :feedbackId")
    suspend fun resolveFeedback(feedbackId: String, response: String)
}
