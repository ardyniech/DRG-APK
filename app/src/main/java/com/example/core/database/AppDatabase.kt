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
        DriverMember::class,
        EmergencyAlert::class,
        PoskoLocation::class,
        KasTransaction::class,
        ForumPost::class,
        WorkshopPartner::class,
        AttendanceEvent::class,
        DriverReview::class,
        CommunityNotification::class,
        BadgeItem::class,
        CommunityTask::class,
        RewardItem::class,
        HazardArea::class,
        PointTransaction::class,
        NotificationPreference::class,
        MapTileMetadata::class,
        AppStateSetting::class,
        PendingSyncEntity::class,
        AdminLog::class
    ],
    version = 9,
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

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `point_transactions` (`id` TEXT NOT NULL, `targetMemberId` TEXT NOT NULL, `targetMemberName` TEXT NOT NULL, `giverMemberId` TEXT NOT NULL, `giverMemberName` TEXT NOT NULL, `giverRole` TEXT NOT NULL, `weightPoints` INTEGER NOT NULL, `isBeneficiaryDirect` INTEGER NOT NULL, `reason` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `notification_preferences` (`memberId` TEXT NOT NULL, `isSosAudible` INTEGER NOT NULL, `isAccidentPushEnabled` INTEGER NOT NULL, `isBegalPushEnabled` INTEGER NOT NULL, `isCommunityEventsEnabled` INTEGER NOT NULL, `isKasReminderEnabled` INTEGER NOT NULL, `isLowBatteryAutoEco` INTEGER NOT NULL, PRIMARY KEY(`memberId`))")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `map_tile_cache` (`tileKey` TEXT NOT NULL, `zoom` INTEGER NOT NULL, `x` INTEGER NOT NULL, `y` INTEGER NOT NULL, `sizeBytes` INTEGER NOT NULL, `cachedTimestamp` INTEGER NOT NULL, PRIMARY KEY(`tileKey`))")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `app_state_settings` (`key` TEXT NOT NULL, `stringValue` TEXT, `intValue` INTEGER, `boolValue` INTEGER, PRIMARY KEY(`key`))")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `pending_sync_queue` (`id` TEXT NOT NULL, `entityType` TEXT NOT NULL, `payloadJson` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "drg_community.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
