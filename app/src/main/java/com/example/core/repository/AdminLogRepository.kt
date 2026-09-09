package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.AdminLog
import kotlinx.coroutines.flow.Flow

class AdminLogRepository(private val db: AppDatabase) {
    val allLogs: Flow<List<AdminLog>> = db.adminLogDao().getAllLogs()
    
    suspend fun insertLog(log: AdminLog) {
        db.adminLogDao().insertLog(log)
    }
}
