package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DRGRepositorySeeder {
    suspend fun seedInitialData(repo: DRGRepository, db: AppDatabase) = withContext(Dispatchers.IO) {
        if (repo.memberRepo.getMemberCount() == 0) {
            val foundingKetua = DriverMember(
                id = com.example.BuildConfig.SEED_SUPER_ADMIN_ID,
                name = com.example.BuildConfig.SEED_SUPER_ADMIN_NAME,
                driverId = com.example.BuildConfig.SEED_SUPER_ADMIN_ID,
                phone = com.example.BuildConfig.SEED_SUPER_ADMIN_PHONE,
                role = MemberRole.SUPER_ADMIN,
                motorcyclePlate = com.example.BuildConfig.SEED_SUPER_ADMIN_PLATE,
                motorcycleModel = "Honda PCX 160",
                rating = 5.0f,
                reviewCount = 12,
                loyaltyPoints = 300,
                loyaltyTier = "Leader",
                verificationStatus = VerificationStatus.VERIFIED,
                screeningNotes = "Super Admin Komunitas DRG Malang Raya",
                isOnline = true,
                currentStatus = "Siaga Komando DRG",
                baseArea = "Klojen, Malang",
                canManageKas = true,
                canVerifyDrivers = true,
                canBroadcastSos = true,
                canManagePosko = true
            )
            repo.memberRepo.registerMember(foundingKetua)
            try {
                val context = com.example.DRGApplication.instance
                com.example.shared.utils.SecurityUtils.storePinHash(context, foundingKetua.id, com.example.BuildConfig.SEED_SUPER_ADMIN_PIN)
                com.example.shared.utils.SecurityUtils.storePinHash(context, foundingKetua.driverId, com.example.BuildConfig.SEED_SUPER_ADMIN_PIN)
                com.example.shared.utils.SecurityUtils.storePinHash(context, foundingKetua.phone, com.example.BuildConfig.SEED_SUPER_ADMIN_PIN)
            } catch (e: Exception) {
                // Fail-safe to avoid crash during offline JVM tests
            }
        }

        // Seed community workshops, badges, posts, and kas transactions
        db.workshopDao().insertWorkshops(CommunitySeedData.defaultWorkshops())
        db.gamificationDao().insertBadges(CommunitySeedData.defaultBadges())
        db.forumDao().insertPosts(CommunitySeedPostsKas.defaultPosts())
        db.kasDao().insertTransactions(CommunitySeedPostsKas.defaultTransactions())
        db.gamificationDao().insertRewards(CommunitySeedGamification.defaultRewards())
        CommunitySeedGamification.defaultTasks().forEach { db.gamificationDao().insertTask(it) }
    }
}
