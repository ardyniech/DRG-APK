package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CommunityEventType(val label: String) {
    KOPDAR_RUTIN("Kopdar Rutin (3 Bulanan)"),
    ANNIVERSARY("Anniversary Komunitas"),
    BAKTI_SOSIAL("Bakti Sosial"),
    LAINNYA("Event Lainnya")
}

enum class CommunityEventStatus(val label: String) {
    DRAFT("Draft/Perencanaan"),
    AKTIF("Aktif/Berjalan"),
    SELESAI("Selesai"),
    LPJ_PENDING("Menunggu LPJ")
}

@Entity(tableName = "community_events")
data class CommunityEvent(
    @PrimaryKey val id: String,
    val title: String,
    val type: CommunityEventType = CommunityEventType.KOPDAR_RUTIN,
    val status: CommunityEventStatus = CommunityEventStatus.DRAFT,
    val scheduleNote: String = "", // e.g. "Setiap 3 bulan"
    val dateText: String = "",
    val timeText: String = "",
    val location: String = "",
    val description: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
