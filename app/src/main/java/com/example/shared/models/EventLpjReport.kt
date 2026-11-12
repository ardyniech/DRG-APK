package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EventLpjStatus(val label: String) {
    BELUM_ADA("Belum Ada LPJ"),
    DRAFT("Draft"),
    DIAJUKAN("Diajukan"),
    DISETUJUI("Disetujui"),
    REVISI("Perlu Revisi")
}

@Entity(
    tableName = "event_lpj_reports",
    indices = [Index(value = ["eventId"], unique = true)]
)
data class EventLpjReport(
    @PrimaryKey val id: String,
    val eventId: String,
    val status: EventLpjStatus = EventLpjStatus.BELUM_ADA,
    val summary: String = "",
    val incomeTotal: Long = 0L,
    val expenseTotal: Long = 0L,
    val balance: Long = 0L,
    val notes: String = "",
    val submittedBy: String = "",
    val submittedAt: Long = 0L,
    val updatedAt: Long = System.currentTimeMillis()
)
