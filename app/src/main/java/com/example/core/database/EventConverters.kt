package com.example.core.database

import androidx.room.TypeConverter
import com.example.shared.models.*

class EventConverters {
    @TypeConverter fun fromEventType(v: CommunityEventType): String = v.name
    @TypeConverter fun toEventType(v: String): CommunityEventType = runCatching { CommunityEventType.valueOf(v) }.getOrDefault(CommunityEventType.LAINNYA)

    @TypeConverter fun fromEventStatus(v: CommunityEventStatus): String = v.name
    @TypeConverter fun toEventStatus(v: String): CommunityEventStatus = runCatching { CommunityEventStatus.valueOf(v) }.getOrDefault(CommunityEventStatus.DRAFT)

    @TypeConverter fun fromFundingType(v: EventFundingType): String = v.name
    @TypeConverter fun toFundingType(v: String): EventFundingType = runCatching { EventFundingType.valueOf(v) }.getOrDefault(EventFundingType.SUMBER)

    @TypeConverter fun fromLpjStatus(v: EventLpjStatus): String = v.name
    @TypeConverter fun toLpjStatus(v: String): EventLpjStatus = runCatching { EventLpjStatus.valueOf(v) }.getOrDefault(EventLpjStatus.BELUM_ADA)
}
