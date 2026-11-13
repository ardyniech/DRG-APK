package com.example.core.database

import androidx.room.TypeConverter
import com.example.shared.models.CommitteeTaskStatus
import com.example.shared.models.EventPhaseStage

class EventConverters {
    @TypeConverter
    fun fromEventPhaseStage(v: EventPhaseStage): String = v.name

    @TypeConverter
    fun toEventPhaseStage(v: String): EventPhaseStage =
        runCatching { EventPhaseStage.valueOf(v) }.getOrDefault(EventPhaseStage.PERENCANAAN)

    @TypeConverter
    fun fromCommitteeTaskStatus(v: CommitteeTaskStatus): String = v.name

    @TypeConverter
    fun toCommitteeTaskStatus(v: String): CommitteeTaskStatus =
        runCatching { CommitteeTaskStatus.valueOf(v) }.getOrDefault(CommitteeTaskStatus.BELUM_MULAI)
}
