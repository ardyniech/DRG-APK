package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED,
    VERIFIED
}

@Entity(tableName = "community_tasks")
data class CommunityTask(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // PATROLI, POSKO, BANTUAN, DOKUMENTASI
    val description: String,
    val rewardPoints: Int,
    val issuerRole: MemberRole,
    val assignedMemberId: String? = null,
    val assignedMemberName: String? = null,
    val status: TaskStatus = TaskStatus.OPEN,
    val deadline: String = "Besok 18:00"
)
