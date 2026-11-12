package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class EventFundingType(val label: String) {
    SUMBER("Dana Sumber/Masuk"),
    KELUAR("Dana Keluar")
}

@Entity(
    tableName = "event_funding",
    indices = [Index(value = ["eventId"]), Index(value = ["type"])]
)
data class EventFunding(
    @PrimaryKey val id: String,
    val eventId: String,
    val type: EventFundingType,
    val amount: Long,
    val category: String = "", // iuran panitia, donasi, sponsor, konsumsi, sewa, dll
    val sourceOrRecipient: String = "", // dari siapa / ke siapa
    val description: String = "",
    val dateText: String = "",
    val proofNote: String = "", // catatan bukti/nota (file upload belum tersedia)
    val recordedBy: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
