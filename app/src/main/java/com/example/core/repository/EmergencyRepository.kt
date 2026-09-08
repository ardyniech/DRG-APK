package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.EmergencyAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EmergencyRepository(private val db: AppDatabase) {
    val allAlerts: Flow<List<EmergencyAlert>> = db.emergencyDao().getAllAlerts()
    val activeAlerts: Flow<List<EmergencyAlert>> = db.emergencyDao().getActiveAlerts()

    suspend fun addEmergencyAlert(alert: EmergencyAlert) = withContext(Dispatchers.IO) {
        db.emergencyDao().insertAlert(alert)
    }

    suspend fun resolveEmergencyAlert(alertId: String, resolvedBy: String) = withContext(Dispatchers.IO) {
        db.emergencyDao().resolveAlert(alertId, resolvedBy)
    }

    suspend fun respondToEmergency(alertId: String) = withContext(Dispatchers.IO) {
        db.emergencyDao().incrementResponder(alertId)
    }
}
