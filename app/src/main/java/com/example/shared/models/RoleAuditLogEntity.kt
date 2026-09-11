package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "role_audit_logs",
    indices = [
        Index(value = ["targetMemberId"]),
        Index(value = ["timestamp"])
    ]
)
data class RoleAuditLogEntity(
    @PrimaryKey val id: String,
    val targetMemberId: String,
    val targetMemberName: String,
    val previousRole: String,
    val newRole: String,
    val actionByMemberId: String,
    val actionByMemberName: String,
    val reason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
