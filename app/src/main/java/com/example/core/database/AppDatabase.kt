package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.core.database.dao.*
import com.example.shared.models.*

@Database(
    entities = [
        DriverMember::class, EmergencyAlert::class, PoskoLocation::class,
        KasTransaction::class, ForumPost::class, WorkshopPartner::class,
        AttendanceEvent::class, DriverReview::class, CommunityNotification::class,
        BadgeItem::class, CommunityTask::class, RewardItem::class,
        HazardArea::class, PointTransaction::class, NotificationPreference::class,
        MapTileMetadata::class, AppStateSetting::class, PendingSyncEntity::class,
        AdminLog::class, ForumComment::class, MemberRolePermissionEntity::class,
        RoleAuditLogEntity::class, PoskoCheckInEntity::class
    ],
    version = 12,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun emergencyDao(): EmergencyDao
    abstract fun poskoDao(): PoskoDao
    abstract fun kasDao(): KasDao
    abstract fun forumDao(): ForumDao
    abstract fun workshopDao(): WorkshopDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun reviewDao(): ReviewDao
    abstract fun notificationDao(): NotificationDao
    abstract fun gamificationDao(): GamificationDao
    abstract fun hazardDao(): HazardDao
    abstract fun notificationPrefDao(): NotificationPrefDao
    abstract fun mapTileDao(): MapTileDao
    abstract fun appStateDao(): AppStateDao
    abstract fun syncQueueDao(): SyncQueueDao
    abstract fun adminLogDao(): AdminLogDao
    abstract fun memberRolePermissionDao(): MemberRolePermissionDao
    abstract fun roleAuditLogDao(): RoleAuditLogDao
    abstract fun poskoCheckInDao(): PoskoCheckInDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drg_community.db"
                )
                    .addMigrations(*AppDatabaseMigrations.ALL_MIGRATIONS)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
