package com.example.core.repository

import com.example.BuildConfig
import com.example.core.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Mengisi katalog referensi non-akun (bengkel, badge, misi komunitas, event awal)
 * tanpa membuat akun demo lokal maupun PIN hardcoded di perangkat.
 * Postingan forum & kas awal hanya diisi pada build DEBUG agar rilis produksi bebas data contoh.
 */
object DRGRepositorySeeder {
    suspend fun seedInitialData(repo: DRGRepository, db: AppDatabase) = withContext(Dispatchers.IO) {
        db.workshopDao().insertWorkshops(CommunitySeedData.defaultWorkshops())
        db.gamificationDao().insertBadges(CommunitySeedData.defaultBadges())
        if (BuildConfig.DEBUG) {
            db.forumDao().insertPosts(CommunitySeedPostsKas.defaultPosts())
            db.kasDao().insertTransactions(CommunitySeedPostsKas.defaultTransactions())
        }
        db.gamificationDao().insertRewards(CommunitySeedGamification.defaultRewards())
        CommunitySeedGamification.defaultTasks().forEach { db.gamificationDao().insertTask(it) }
        repo.eventRepo.seedIfEmpty()
    }
}
