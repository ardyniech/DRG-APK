package com.example.core.viewmodel.coordinators

import com.example.core.repository.DRGRepository
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

class EventCoordinator(
    private val repository: DRGRepository,
    private val scope: CoroutineScope,
    private val showToast: (String) -> Unit
) {
    private fun id(prefix: String) = "$prefix-${UUID.randomUUID().toString().take(8).uppercase()}"

    fun createEvent(
        cur: DriverMember?, title: String, category: String, description: String,
        targetDateText: String, eventTimestamp: Long, locationName: String,
        coordinatorId: String, coordinatorName: String, estimatedBudget: Long,
        collectedFunds: Long, targetAttendees: Int, readinessNotes: String,
        draftTasks: List<EventCommitteeTaskEntity>
    ) {
        if (cur == null) { showToast("Login diperlukan."); return }
        val now = System.currentTimeMillis()
        val eventId = id("EVT")
        val event = CommunityEventEntity(
            id = eventId, title = title.trim(), category = category.trim(), description = description.trim(),
            targetDateText = targetDateText.trim(), eventTimestamp = eventTimestamp, locationName = locationName.trim(),
            coordinatorId = coordinatorId.ifBlank { cur.id }, coordinatorName = coordinatorName.ifBlank { cur.name },
            estimatedBudgetRupiah = estimatedBudget, collectedFundsRupiah = collectedFunds,
            targetAttendees = targetAttendees, stage = EventPhaseStage.PERENCANAAN, readinessNotes = readinessNotes.trim(),
            createdByMemberId = cur.id, createdByMemberName = cur.name, createdAt = now
        )
        val tasks = draftTasks.map { t ->
            t.copy(id = if (t.id.startsWith("EVTK-D")) id("TSK") else t.id, eventId = eventId, updatedAt = now)
        }
        scope.launch {
            runCatching { repository.createEventWithTasks(event, tasks) }
                .onSuccess { showToast("Event komunitas dibuat.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun updateStage(cur: DriverMember?, eventId: String, stage: EventPhaseStage, readinessNotes: String) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.updateEventStage(eventId, stage, readinessNotes.trim()) }
                .onSuccess { showToast("Status event diperbarui.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun addTask(cur: DriverMember?, eventId: String, divisionName: String, taskTitle: String, picName: String, deadlineText: String, budgetRupiah: Long) {
        if (cur == null) return
        val task = EventCommitteeTaskEntity(
            id = id("TSK"), eventId = eventId, divisionName = divisionName.trim(), taskTitle = taskTitle.trim(),
            picMemberId = cur.id, picMemberName = picName.trim().ifBlank { cur.name }, deadlineText = deadlineText.trim(),
            progressPercent = 0, status = CommitteeTaskStatus.BELUM_MULAI, blockerNote = "", budgetAllocatedRupiah = budgetRupiah
        )
        scope.launch {
            runCatching { repository.addCommitteeTask(task) }
                .onSuccess { showToast("Tugas panitia ditambahkan.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun updateTask(taskId: String, progress: Int, status: CommitteeTaskStatus, blockerNote: String) = scope.launch {
        runCatching { repository.updateCommitteeTaskProgress(taskId, progress.coerceIn(0, 100), status, blockerNote.trim()) }
            .onSuccess { showToast("Progres tugas diperbarui.") }
            .onFailure { showToast("Gagal: ${it.localizedMessage}") }
    }

    fun submitFeedback(cur: DriverMember?, eventId: String, category: String, severity: String, message: String) {
        if (cur == null || message.isBlank()) return
        val fb = EventMemberFeedbackEntity(
            id = id("EVFB"), eventId = eventId, authorMemberId = cur.id, authorName = cur.name,
            authorRole = cur.role, category = category.trim(), severityLevel = severity.trim(),
            message = message.trim(), committeeResponse = "", isResolved = false
        )
        scope.launch {
            runCatching { repository.submitEventFeedback(fb) }
                .onSuccess { showToast("Masukan event terkirim.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun resolveFeedback(feedbackId: String, response: String) = scope.launch {
        runCatching { repository.resolveEventFeedback(feedbackId, response.trim()) }
            .onSuccess { showToast("Masukan ditandai selesai.") }
            .onFailure { showToast("Gagal: ${it.localizedMessage}") }
    }
}
