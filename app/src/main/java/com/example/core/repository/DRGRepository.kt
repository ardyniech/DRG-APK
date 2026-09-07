package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DRGRepository(private val db: AppDatabase) {
    val allMembers: Flow<List<DriverMember>> = db.memberDao().getAllMembers()
    val allAlerts: Flow<List<EmergencyAlert>> = db.emergencyDao().getAllAlerts()
    val activeAlerts: Flow<List<EmergencyAlert>> = db.emergencyDao().getActiveAlerts()
    val allPosko: Flow<List<PoskoLocation>> = db.poskoDao().getAllPosko()
    val allTransactions: Flow<List<KasTransaction>> = db.kasDao().getAllTransactions()
    val allPosts: Flow<List<ForumPost>> = db.forumDao().getAllPosts()
    val allWorkshops: Flow<List<WorkshopPartner>> = db.workshopDao().getAllWorkshops()
    val allEvents: Flow<List<AttendanceEvent>> = db.attendanceDao().getAllEvents()
    val allNotifications: Flow<List<CommunityNotification>> = db.notificationDao().getAllNotifications()
    val allBadges: Flow<List<BadgeItem>> = db.gamificationDao().getAllBadges()
    val allTasks: Flow<List<CommunityTask>> = db.gamificationDao().getAllTasks()
    val allRewards: Flow<List<RewardItem>> = db.gamificationDao().getAllRewards()
    val allHazards: Flow<List<HazardArea>> = db.hazardDao().getAllHazards()
    val allPointTransactions: Flow<List<PointTransaction>> = db.gamificationDao().getAllPointTransactions()

    fun getPrefForMember(memberId: String): Flow<NotificationPreference?> =
        db.notificationPrefDao().getPrefForMember(memberId)

    fun getReviewsForDriver(driverId: String): Flow<List<DriverReview>> =
        db.reviewDao().getReviewsForDriver(driverId)

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        val existingMembers = db.memberDao().getAllMembers().first()
        if (existingMembers.isEmpty()) {
            db.memberDao().insertMembers(SeedData.getInitialMembers())
            db.poskoDao().insertPoskoList(SeedDataExtra.getInitialPosko())
            db.emergencyDao().insertAlerts(SeedDataExtra.getInitialEmergency())
            db.kasDao().insertTransactions(SeedDataTransactions.getInitialKas())
            db.forumDao().insertPosts(SeedDataTransactions.getInitialForum())
            db.workshopDao().insertWorkshops(SeedDataTransactions.getInitialWorkshops())
            db.attendanceDao().insertEvents(SeedDataTransactions.getInitialEvents())
            db.notificationDao().insertNotifications(SeedDataTransactions.getInitialNotifications())
            db.reviewDao().insertReviews(SeedDataTransactions.getInitialReviews())
            db.gamificationDao().insertBadges(SeedDataGamification.getInitialBadges())
            SeedDataGamification.getInitialTasks().forEach { db.gamificationDao().insertTask(it) }
            db.gamificationDao().insertRewards(SeedDataGamification.getInitialRewards())
            SeedDataGamification.getInitialHazards().forEach { db.hazardDao().insertHazard(it) }
        }
    }

    suspend fun calculatePointWeight(giverRole: MemberRole, isBeneficiary: Boolean): Int {
        if (isBeneficiary) return 2
        return when (giverRole) {
            MemberRole.DEWAN_ETIKA -> 5
            MemberRole.KETUA, MemberRole.WAKIL_KETUA -> 4
            MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.SATGAS -> 3
            MemberRole.ANGGOTA -> 1
        }
    }

    suspend fun awardPeerPoints(
        targetMemberId: String,
        targetMemberName: String,
        giverMember: DriverMember,
        isBeneficiary: Boolean,
        reason: String
    ) = withContext(Dispatchers.IO) {
        val weight = calculatePointWeight(giverMember.role, isBeneficiary)
        db.memberDao().addLoyaltyPoints(targetMemberId, weight)

        val tx = PointTransaction(
            id = "PTX-${System.currentTimeMillis() % 100000}",
            targetMemberId = targetMemberId,
            targetMemberName = targetMemberName,
            giverMemberId = giverMember.id,
            giverMemberName = giverMember.name,
            giverRole = giverMember.role,
            weightPoints = weight,
            isBeneficiaryDirect = isBeneficiary,
            reason = reason
        )
        db.gamificationDao().insertPointTransaction(tx)

        val notif = CommunityNotification(
            id = "NTF-${System.currentTimeMillis() % 100000}",
            title = "Apresiasi Poin Diterima! (+${weight} XP)",
            message = "${giverMember.name} (${if (isBeneficiary) "Korban Bantuan Direct" else giverMember.role.title}) memberikan +$weight poin: \"$reason\"",
            severity = NotificationSeverity.SUCCESS,
            senderName = giverMember.name,
            senderRole = giverMember.role,
            timeAgo = "Baru saja"
        )
        db.notificationDao().insertNotification(notif)
    }

    suspend fun claimTask(taskId: String, member: DriverMember) = withContext(Dispatchers.IO) {
        val tasks = db.gamificationDao().getAllTasks().first()
        val task = tasks.find { it.id == taskId } ?: return@withContext
        val updated = task.copy(
            assignedMemberId = member.id,
            assignedMemberName = member.name,
            status = TaskStatus.IN_PROGRESS
        )
        db.gamificationDao().updateTask(updated)
    }

    suspend fun completeTask(taskId: String, memberId: String) = withContext(Dispatchers.IO) {
        val tasks = db.gamificationDao().getAllTasks().first()
        val task = tasks.find { it.id == taskId } ?: return@withContext
        val updated = task.copy(status = TaskStatus.VERIFIED)
        db.gamificationDao().updateTask(updated)
        db.memberDao().addLoyaltyPoints(memberId, task.rewardPoints)

        val notif = CommunityNotification(
            id = "NTF-${System.currentTimeMillis() % 100000}",
            title = "Tugas Komunitas Selesai (+${task.rewardPoints} XP)",
            message = "Tugas \"${task.title}\" telah diverifikasi. Poin loyalitas ditambahkan!",
            severity = NotificationSeverity.SUCCESS,
            senderName = "Sistem Komunitas DRG",
            senderRole = MemberRole.KETUA,
            timeAgo = "Baru saja"
        )
        db.notificationDao().insertNotification(notif)
    }

    suspend fun redeemReward(rewardId: String, member: DriverMember) = withContext(Dispatchers.IO) {
        val rewards = db.gamificationDao().getAllRewards().first()
        val reward = rewards.find { it.id == rewardId } ?: return@withContext
        if (member.loyaltyPoints >= reward.requiredPoints && reward.stockAvailable > 0) {
            db.memberDao().addLoyaltyPoints(member.id, -reward.requiredPoints)
            db.gamificationDao().updateReward(reward.copy(stockAvailable = reward.stockAvailable - 1, isRedeemed = true))
        }
    }

    suspend fun addHazard(hazard: HazardArea) = withContext(Dispatchers.IO) {
        db.hazardDao().insertHazard(hazard)
        val notif = CommunityNotification(
            id = "NTF-${System.currentTimeMillis() % 100000}",
            title = "Peringatan Jalur Rawan Baru: ${hazard.hazardType.label}",
            message = "Lokasi: ${hazard.locationName}. Dilaporkan oleh ${hazard.reportedBy}. Harap waspada!",
            severity = NotificationSeverity.WARNING,
            senderName = hazard.reportedBy,
            senderRole = hazard.reporterRole,
            timeAgo = "Baru saja"
        )
        db.notificationDao().insertNotification(notif)
    }

    suspend fun confirmHazard(hazardId: String) = withContext(Dispatchers.IO) {
        val hazards = db.hazardDao().getAllHazards().first()
        val hazard = hazards.find { it.id == hazardId } ?: return@withContext
        db.hazardDao().updateHazard(hazard.copy(confirmCount = hazard.confirmCount + 1))
    }

    suspend fun saveNotificationPref(pref: NotificationPreference) = withContext(Dispatchers.IO) {
        db.notificationPrefDao().savePref(pref)
    }

    suspend fun addEmergencyAlert(alert: EmergencyAlert) = withContext(Dispatchers.IO) {
        db.emergencyDao().insertAlert(alert)
    }

    suspend fun resolveEmergencyAlert(alertId: String, resolvedBy: String) = withContext(Dispatchers.IO) {
        db.emergencyDao().resolveAlert(alertId, resolvedBy)
    }

    suspend fun respondToEmergency(alertId: String) = withContext(Dispatchers.IO) {
        db.emergencyDao().incrementResponder(alertId)
    }

    suspend fun addKasTransaction(transaction: KasTransaction) = withContext(Dispatchers.IO) {
        db.kasDao().insertTransaction(transaction)
    }

    suspend fun addForumPost(post: ForumPost) = withContext(Dispatchers.IO) {
        db.forumDao().insertPost(post)
    }

    suspend fun deleteForumPost(postId: String) = withContext(Dispatchers.IO) {
        db.forumDao().deletePost(postId)
    }

    suspend fun toggleLikePost(postId: String, currentLiked: Boolean) = withContext(Dispatchers.IO) {
        val delta = if (currentLiked) -1 else 1
        db.forumDao().toggleLike(postId, delta, !currentLiked)
    }

    suspend fun updateScreening(memberId: String, status: VerificationStatus, notes: String) = withContext(Dispatchers.IO) {
        db.memberDao().updateScreeningStatus(memberId, status, notes)
    }

    suspend fun checkInKopdar(eventId: String, memberId: String, points: Int) = withContext(Dispatchers.IO) {
        db.attendanceDao().checkIn(eventId)
        db.memberDao().addLoyaltyPoints(memberId, points)
    }

    suspend fun addReview(review: DriverReview) = withContext(Dispatchers.IO) {
        db.reviewDao().insertReview(review)
    }

    suspend fun updateMember(member: DriverMember) = withContext(Dispatchers.IO) {
        db.memberDao().updateMember(member)
    }

    suspend fun registerMember(member: DriverMember) = withContext(Dispatchers.IO) {
        db.memberDao().insertMember(member)
    }

    suspend fun addNotification(notification: CommunityNotification) = withContext(Dispatchers.IO) {
        db.notificationDao().insertNotification(notification)
    }

    suspend fun saveNotificationPreference(pref: NotificationPreference) = withContext(Dispatchers.IO) {
        db.notificationPrefDao().savePref(pref)
    }
}
