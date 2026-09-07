package com.example.core.database

import androidx.room.TypeConverter
import com.example.shared.models.*

class Converters {
    @TypeConverter
    fun fromMemberRole(value: MemberRole): String = value.name

    @TypeConverter
    fun toMemberRole(value: String): MemberRole = try {
        MemberRole.valueOf(value)
    } catch (_: Exception) {
        MemberRole.ANGGOTA
    }

    @TypeConverter
    fun fromVerificationStatus(value: VerificationStatus): String = value.name

    @TypeConverter
    fun toVerificationStatus(value: String): VerificationStatus = try {
        VerificationStatus.valueOf(value)
    } catch (_: Exception) {
        VerificationStatus.VERIFIED
    }

    @TypeConverter
    fun fromEmergencyType(value: EmergencyType): String = value.name

    @TypeConverter
    fun toEmergencyType(value: String): EmergencyType = try {
        EmergencyType.valueOf(value)
    } catch (_: Exception) {
        EmergencyType.MOGOK
    }

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = try {
        TransactionType.valueOf(value)
    } catch (_: Exception) {
        TransactionType.INCOME
    }

    @TypeConverter
    fun fromKasCategory(value: KasCategory): String = value.name

    @TypeConverter
    fun toKasCategory(value: String): KasCategory = try {
        KasCategory.valueOf(value)
    } catch (_: Exception) {
        KasCategory.IURAN_BULANAN
    }

    @TypeConverter
    fun fromForumCategory(value: ForumCategory): String = value.name

    @TypeConverter
    fun toForumCategory(value: String): ForumCategory = try {
        ForumCategory.valueOf(value)
    } catch (_: Exception) {
        ForumCategory.TIPS_MESIN
    }

    @TypeConverter
    fun fromNotificationSeverity(value: NotificationSeverity): String = value.name

    @TypeConverter
    fun toNotificationSeverity(value: String): NotificationSeverity = try {
        NotificationSeverity.valueOf(value)
    } catch (_: Exception) {
        NotificationSeverity.INFO
    }

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = try {
        TaskStatus.valueOf(value)
    } catch (_: Exception) {
        TaskStatus.OPEN
    }

    @TypeConverter
    fun fromHazardType(value: HazardType): String = value.name

    @TypeConverter
    fun toHazardType(value: String): HazardType = try {
        HazardType.valueOf(value)
    } catch (_: Exception) {
        HazardType.BEGAL_RISK
    }

    @TypeConverter
    fun fromPostType(value: PostType): String = value.name

    @TypeConverter
    fun toPostType(value: String): PostType = try {
        PostType.valueOf(value)
    } catch (_: Exception) {
        PostType.UPDATE
    }
}
