package com.example.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
        AppStateSetting::class
    ],
    version = 5,
    exportSchema = false
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
