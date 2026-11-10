package com.example

import android.app.Application
import android.os.StrictMode
import com.example.core.database.AppDatabase
import com.example.core.repository.DRGRepository
import com.example.core.update.UpdateCheckWorker
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
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
        // Cek update periodik (hemat baterai: butuh jaringan, setiap 12 jam).
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val request = PeriodicWorkRequestBuilder<UpdateCheckWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        try {
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                UpdateCheckWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        } catch (_: IllegalStateException) {
            // WorkManager belum ter-inisialisasi (mis. environment test) — lewati.
        }
        applicationScope.launch(Dispatchers.IO) {
            val prefs = getSharedPreferences("drg_prefs", MODE_PRIVATE)
            val hasSeeded = prefs.getBoolean("drg_has_seeded_v6", false)
            if (!hasSeeded) {
                repository.initializeSeedDataIfNeeded()
                prefs.edit().putBoolean("drg_has_seeded_v6", true).commit()
            }
        }
    }

    companion object {
        lateinit var instance: DRGApplication
            private set
    }
}
