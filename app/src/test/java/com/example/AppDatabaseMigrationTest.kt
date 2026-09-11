package com.example

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.core.database.AppDatabaseMigrations
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppDatabaseMigrationTest {

    @Test
    fun testAllMigrationsListContainsAllVersions() {
        val migrations = AppDatabaseMigrations.ALL_MIGRATIONS
        assertEquals(11, migrations.size)
        assertEquals(1, migrations[0].startVersion)
        assertEquals(2, migrations[0].endVersion)
        assertEquals(11, migrations.last().startVersion)
        assertEquals(12, migrations.last().endVersion)
    }

    @Test
    fun testDatabaseInitializationWithMigrations() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addMigrations(*AppDatabaseMigrations.ALL_MIGRATIONS)
            .allowMainThreadQueries()
            .build()

        assertNotNull(db.memberDao())
        assertNotNull(db.poskoCheckInDao())
        assertNotNull(db.memberRolePermissionDao())
        assertNotNull(db.roleAuditLogDao())
        assertNotNull(db.kasDao())
        assertNotNull(db.emergencyDao())
        db.close()
    }
}
