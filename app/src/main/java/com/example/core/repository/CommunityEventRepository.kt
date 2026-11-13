package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CommunityEventRepository(private val db: AppDatabase) {
    private val dao = db.communityEventDao()

    val allEvents: Flow<List<CommunityEventEntity>> = dao.getAllEvents()
    val allCommitteeTasks: Flow<List<EventCommitteeTaskEntity>> = dao.getAllCommitteeTasks()
    val allEventFeedback: Flow<List<EventMemberFeedbackEntity>> = dao.getAllEventFeedback()

    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.getEventCount() == 0) {
            dao.insertEvents(EventSeedData.defaultEvents())
            dao.insertTasks(EventSeedData.defaultTasks())
            dao.insertFeedbackList(EventSeedData.defaultFeedback())
        }
    }

    suspend fun createEventWithTasks(
        event: CommunityEventEntity,
        tasks: List<EventCommitteeTaskEntity>
    ) = withContext(Dispatchers.IO) {
        dao.insertEvent(event)
        if (tasks.isNotEmpty()) dao.insertTasks(tasks)
    }

    suspend fun updateEventStage(eventId: String, stage: EventPhaseStage, readinessNotes: String) =
        withContext(Dispatchers.IO) {
            dao.updateEventStage(eventId, stage, readinessNotes)
        }

    suspend fun addCommitteeTask(task: EventCommitteeTaskEntity) = withContext(Dispatchers.IO) {
        dao.insertTask(task)
    }

    suspend fun updateTaskProgress(
        taskId: String,
        progressPercent: Int,
        status: CommitteeTaskStatus,
        blockerNote: String
    ) = withContext(Dispatchers.IO) {
        dao.updateTaskProgress(
            taskId = taskId,
            progress = progressPercent.coerceIn(0, 100),
            status = status,
            blockerNote = blockerNote,
            updatedAt = System.currentTimeMillis()
        )
    }

    suspend fun submitFeedback(feedback: EventMemberFeedbackEntity) = withContext(Dispatchers.IO) {
        dao.insertFeedback(feedback)
    }

    suspend fun resolveFeedback(feedbackId: String, response: String) = withContext(Dispatchers.IO) {
        dao.resolveFeedback(feedbackId, response)
    }
}
