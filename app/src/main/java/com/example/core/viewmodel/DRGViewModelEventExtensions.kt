package com.example.core.viewmodel

import com.example.shared.models.CommitteeTaskStatus
import com.example.shared.models.EventCommitteeTaskEntity
import com.example.shared.models.EventPhaseStage

fun DRGViewModel.createCommunityEvent(
    title: String,
    category: String,
    description: String,
    targetDateText: String,
    eventTimestamp: Long,
    locationName: String,
    coordinatorId: String,
    coordinatorName: String,
    estimatedBudget: Long,
    collectedFunds: Long,
    targetAttendees: Int,
    readinessNotes: String,
    draftTasks: List<EventCommitteeTaskEntity>
) = eventCoord.createEvent(
    currentMember.value, title, category, description, targetDateText, eventTimestamp,
    locationName, coordinatorId, coordinatorName, estimatedBudget, collectedFunds,
    targetAttendees, readinessNotes, draftTasks
)

fun DRGViewModel.updateCommunityEventStage(eventId: String, stage: EventPhaseStage, readinessNotes: String) =
    eventCoord.updateStage(currentMember.value, eventId, stage, readinessNotes)

fun DRGViewModel.addCommitteeTask(
    eventId: String, divisionName: String, taskTitle: String,
    picName: String, deadlineText: String, budgetRupiah: Long
) = eventCoord.addTask(currentMember.value, eventId, divisionName, taskTitle, picName, deadlineText, budgetRupiah)

fun DRGViewModel.updateCommitteeTask(taskId: String, progress: Int, status: CommitteeTaskStatus, blockerNote: String) =
    eventCoord.updateTask(taskId, progress, status, blockerNote)

fun DRGViewModel.submitEventFeedback(eventId: String, category: String, severity: String, message: String) =
    eventCoord.submitFeedback(currentMember.value, eventId, category, severity, message)

fun DRGViewModel.resolveEventFeedback(feedbackId: String, response: String) =
    eventCoord.resolveFeedback(feedbackId, response)
