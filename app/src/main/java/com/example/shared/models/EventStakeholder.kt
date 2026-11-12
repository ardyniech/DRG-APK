package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "event_stakeholders",
    indices = [Index(value = ["eventId"])]
)
data class EventStakeholder(
    @PrimaryKey val id: String,
    val eventId: String,
    val name: String,
    val role: String, // Sponsor, Vendor, Pemateri, Keamanan, Mitra, Donatur, Tamu, dll
    val organization: String = "",
    val contact: String = "",
    val contribution: String = "",
    val notes: String = ""
)
