package com.example

import android.app.Application
import android.os.StrictMode
import com.example.core.database.AppDatabase
import com.example.core.repository.DRGRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DRGApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { DRGRepository(database) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationScope.launch(Dispatchers.IO) {
            val prefs = getSharedPreferences("drg_prefs", MODE_PRIVATE)
            val hasSeeded = prefs.getBoolean("drg_has_seeded_v5", false)
            if (!hasSeeded) {
                repository.initializeSeedDataIfNeeded()
                prefs.edit().putBoolean("drg_has_seeded_v5", true).commit()
            }
        }
    }

    companion object {
        lateinit var instance: DRGApplication
            private set
    }
}
