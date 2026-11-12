package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "event_committee_members",
    indices = [Index(value = ["eventId"])]
)
data class EventCommitteeMember(
    @PrimaryKey val id: String,
    val eventId: String,
    val memberId: String = "",
    val name: String,
    val role: String, // Ketua Panitia, Sekretaris, Bendahara, Koordinator, Anggota, dll
    val phone: String = "",
    val responsibility: String = "",
    val notes: String = ""
)
