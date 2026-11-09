package com.example.modules.devtools

import android.content.Context
import com.example.core.database.AppDatabase

data class DatabaseRuntimeReport(
    val actualUserVersion: Int,
    val actualTableCount: Int,
    val tableNames: List<String>,
    val isSchemaUpToDate: Boolean,
    val missingTablesFromLatest: List<String>,
    val statusDescription: String
)

object DynamicDbIntrospector {
    const val TARGET_SCHEMA_VERSION = 13
    val EXPECTED_TABLES_V13 = listOf(
        "driver_members", "emergency_alerts", "posko_locations", "kas_transactions",
        "forum_posts", "workshop_partners", "attendance_events", "driver_reviews",
        "community_notifications", "badge_items", "community_tasks", "reward_items",
        "hazard_areas", "point_transactions", "notification_preferences",
        "map_tile_metadata", "app_state_settings", "pending_sync", "admin_logs",
        "forum_comments", "member_role_permissions", "role_audit_logs", "posko_check_ins"
    )

    fun introspectDatabase(context: Context): DatabaseRuntimeReport {
        return try {
            val db = AppDatabase.getInstance(context)
            val sqlite = db.openHelper.readableDatabase

            var userVersion = 0
            val cursorVersion = sqlite.query("PRAGMA user_version")
            if (cursorVersion.moveToFirst()) {
                userVersion = cursorVersion.getInt(0)
            }
            cursorVersion.close()

            val tables = mutableListOf<String>()
            val query = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%' AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'room_%'"
            val cursorTables = sqlite.query(query)
            while (cursorTables.moveToNext()) {
                tables.add(cursorTables.getString(0))
            }
            cursorTables.close()

            val missing = EXPECTED_TABLES_V13.filter { it !in tables }
            val isUpToDate = userVersion >= TARGET_SCHEMA_VERSION && missing.isEmpty()

            val desc = if (isUpToDate) {
                "Skema SQLite riil v$userVersion (${tables.size} tabel aktif). Sinkron sempurna."
            } else {
                "APK menjalankan skema v$userVersion (${tables.size} tabel, ${missing.size} tabel belum ada). Adaptasi aktif."
            }

            DatabaseRuntimeReport(
                actualUserVersion = userVersion,
                actualTableCount = tables.size,
                tableNames = tables,
                isSchemaUpToDate = isUpToDate,
                missingTablesFromLatest = missing,
                statusDescription = desc
            )
        } catch (_: Exception) {
            DatabaseRuntimeReport(
                actualUserVersion = 0,
                actualTableCount = 0,
                tableNames = emptyList(),
                isSchemaUpToDate = false,
                missingTablesFromLatest = EXPECTED_TABLES_V13,
                statusDescription = "Database belum diinisialisasi atau berjalan pada APK versi lawas tanpa Room."
            )
        }
    }
}
