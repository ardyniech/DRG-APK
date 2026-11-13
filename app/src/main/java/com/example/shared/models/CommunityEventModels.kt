package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EventPhaseStage(val label: String) {
    PERENCANAAN("Perencanaan"),
    PERSIAPAN_PANITIA("Persiapan Panitia"),
    SIAP_PELAKSANAAN("Siap Dilaksanakan"),
    BERLANGSUNG("Sedang Berjalan"),
    SELESAI("Selesai & Lapor"),
    RAWAN_TERTUNDA("Rawan Tertunda")
}

enum class CommitteeTaskStatus(val label: String) {
    BELUM_MULAI("Belum Mulai"),
    DIKERJAKAN("Dikerjakan"),
    TERKENDALA("Terkendala"),
    SELESAI("Selesai")
}

@Entity(
    tableName = "community_events",
    indices = [Index(value = ["stage"]), Index(value = ["eventTimestamp"])]
)
data class CommunityEventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val description: String,
    val targetDateText: String,
    val eventTimestamp: Long,
    val locationName: String,
    val coordinatorId: String,
    val coordinatorName: String,
    val estimatedBudgetRupiah: Long,
    val collectedFundsRupiah: Long,
    val targetAttendees: Int,
    val stage: EventPhaseStage = EventPhaseStage.PERENCANAAN,
    val readinessNotes: String = "",
    val createdByMemberId: String,
    val createdByMemberName: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "event_committee_tasks",
    indices = [Index(value = ["eventId"]), Index(value = ["status"])]
)
data class EventCommitteeTaskEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val divisionName: String,
    val taskTitle: String,
    val picMemberId: String,
    val picMemberName: String,
    val deadlineText: String,
    val progressPercent: Int = 0,
    val status: CommitteeTaskStatus = CommitteeTaskStatus.BELUM_MULAI,
    val blockerNote: String = "",
    val budgetAllocatedRupiah: Long = 0L,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "event_member_feedback",
    indices = [Index(value = ["eventId"]), Index(value = ["createdAt"])]
)
data class EventMemberFeedbackEntity(
    @PrimaryKey val id: String,
    val eventId: String,
    val authorMemberId: String,
    val authorName: String,
    val authorRole: MemberRole,
    val category: String,
    val severityLevel: String,
    val message: String,
    val committeeResponse: String = "",
    val isResolved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
