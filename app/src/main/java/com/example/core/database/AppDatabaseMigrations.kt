package com.example.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

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

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `admin_logs` (`id` TEXT NOT NULL, `action` TEXT NOT NULL, `performedBy` TEXT NOT NULL, `targetMemberId` TEXT, `notes` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_emergency_alerts_isActive_timestamp` ON `emergency_alerts` (`isActive`, `timestamp`)")
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_kas_transactions_type_timestamp` ON `kas_transactions` (`type`, `timestamp`)")
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `forum_comments` (`id` TEXT NOT NULL, `postId` TEXT NOT NULL, `authorId` TEXT NOT NULL, `authorName` TEXT NOT NULL, `authorAvatar` TEXT NOT NULL, `content` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `likesCount` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_forum_comments_postId` ON `forum_comments` (`postId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_forum_comments_timestamp` ON `forum_comments` (`timestamp`)")
        }
    }

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `member_role_permissions` (`memberId` TEXT NOT NULL, `role` TEXT NOT NULL, `assignedByMemberId` TEXT NOT NULL, `assignedByMemberName` TEXT NOT NULL, `assignedTimestamp` INTEGER NOT NULL, `canManageKas` INTEGER NOT NULL, `canVerifyDrivers` INTEGER NOT NULL, `canBroadcastSos` INTEGER NOT NULL, `canManagePosko` INTEGER NOT NULL, `verificationStatus` TEXT NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`memberId`))")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_member_role_permissions_role` ON `member_role_permissions` (`role`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_member_role_permissions_verificationStatus` ON `member_role_permissions` (`verificationStatus`)")
            db.execSQL("CREATE TABLE IF NOT EXISTS `role_audit_logs` (`id` TEXT NOT NULL, `targetMemberId` TEXT NOT NULL, `targetMemberName` TEXT NOT NULL, `previousRole` TEXT NOT NULL, `newRole` TEXT NOT NULL, `actionByMemberId` TEXT NOT NULL, `actionByMemberName` TEXT NOT NULL, `reason` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_role_audit_logs_targetMemberId` ON `role_audit_logs` (`targetMemberId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_role_audit_logs_timestamp` ON `role_audit_logs` (`timestamp`)")
        }
    }

    val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `posko_check_ins` (`id` TEXT NOT NULL, `poskoId` TEXT NOT NULL, `poskoName` TEXT NOT NULL, `memberId` TEXT NOT NULL, `memberName` TEXT NOT NULL, `driverPlate` TEXT NOT NULL, `checkInTimestamp` INTEGER NOT NULL, `pointsAwarded` INTEGER NOT NULL, `checkInMethod` TEXT NOT NULL, PRIMARY KEY(`id`))")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_posko_check_ins_poskoId` ON `posko_check_ins` (`poskoId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_posko_check_ins_memberId` ON `posko_check_ins` (`memberId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_posko_check_ins_checkInTimestamp` ON `posko_check_ins` (`checkInTimestamp`)")
        }
    }

    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
        MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9,
        MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12
    )
}
