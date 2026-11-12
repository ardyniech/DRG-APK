package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DRGRepository(val db: AppDatabase) {
    val memberRepo = MemberRepository(db)
    val emergencyRepo = EmergencyRepository(db)
    val communityRepo = CommunityRepository(db)
    val gamificationRepo = GamificationRepository(db)
    val radarHazardRepo = RadarHazardRepository(db)
    val adminLogRepo = AdminLogRepository(db)
    val eventRepo = EventManagementRepository(db)

    // Flow Delegations
    val allMembers: Flow<List<DriverMember>> get() = memberRepo.allMembers
    val allAlerts: Flow<List<EmergencyAlert>> get() = emergencyRepo.allAlerts
    val activeAlerts: Flow<List<EmergencyAlert>> get() = emergencyRepo.activeAlerts
    val allPosko: Flow<List<PoskoLocation>> get() = communityRepo.allPosko
    val allTransactions: Flow<List<KasTransaction>> get() = communityRepo.allTransactions
    val allPosts: Flow<List<ForumPost>> get() = communityRepo.allPosts
    val allWorkshops: Flow<List<WorkshopPartner>> get() = communityRepo.allWorkshops
    val allEvents: Flow<List<AttendanceEvent>> get() = communityRepo.allEvents
    val allNotifications: Flow<List<CommunityNotification>> get() = communityRepo.allNotifications
    val allBadges: Flow<List<BadgeItem>> get() = gamificationRepo.allBadges
    val allTasks: Flow<List<CommunityTask>> get() = gamificationRepo.allTasks
    val allRewards: Flow<List<RewardItem>> get() = gamificationRepo.allRewards
    val allHazards: Flow<List<HazardArea>> get() = radarHazardRepo.allHazards
    val allPointTransactions: Flow<List<PointTransaction>> get() = gamificationRepo.allPointTransactions
    val allMapTiles: Flow<List<MapTileMetadata>> get() = radarHazardRepo.allMapTiles
    val totalTileCount: Flow<Int> get() = radarHazardRepo.totalTileCount
    val totalTileSizeBytes: Flow<Long?> get() = radarHazardRepo.totalTileSizeBytes
    val allAppSettings: Flow<List<AppStateSetting>> get() = radarHazardRepo.allAppSettings
    val allAdminLogs: Flow<List<AdminLog>> get() = adminLogRepo.allLogs
    val allCommunityEvents: Flow<List<CommunityEvent>> get() = eventRepo.allEvents

    fun observeCommunityEvent(eventId: String) = eventRepo.observeEvent(eventId)
    fun eventCommittee(eventId: String) = eventRepo.committee(eventId)
    fun eventFunding(eventId: String) = eventRepo.funding(eventId)
    fun eventLpj(eventId: String) = eventRepo.lpj(eventId)
    fun eventStakeholders(eventId: String) = eventRepo.stakeholders(eventId)
    suspend fun upsertCommunityEvent(e: CommunityEvent) = eventRepo.upsertEvent(e)
    suspend fun deleteCommunityEvent(e: CommunityEvent) = eventRepo.deleteEvent(e)
    suspend fun upsertEventCommittee(m: EventCommitteeMember) = eventRepo.upsertCommittee(m)
    suspend fun deleteEventCommittee(m: EventCommitteeMember) = eventRepo.deleteCommittee(m)
    suspend fun upsertEventFunding(f: EventFunding) = eventRepo.upsertFunding(f)
    suspend fun deleteEventFunding(f: EventFunding) = eventRepo.deleteFunding(f)
    suspend fun upsertEventLpj(r: EventLpjReport) = eventRepo.upsertLpj(r)
    suspend fun upsertEventStakeholder(s: EventStakeholder) = eventRepo.upsertStakeholder(s)
    suspend fun deleteEventStakeholder(s: EventStakeholder) = eventRepo.deleteStakeholder(s)

    fun getPrefForMember(memberId: String) = communityRepo.getPrefForMember(memberId)
    fun getReviewsForDriver(driverId: String) = memberRepo.getReviewsForDriver(driverId)

    suspend fun initializeSeedDataIfNeeded() = DRGRepositorySeeder.seedInitialData(this, db)

    fun getReviewsForWorkshop(workshopId: String) = communityRepo.getReviewsForWorkshop(workshopId)
    suspend fun addWorkshopReview(w: WorkshopPartner, r: DriverMember, rate: Float, s: String, c: String) =
        communityRepo.addWorkshopReview(w, r, rate, s, c)

    // Method Delegations
    suspend fun awardPeerPoints(tId: String, tN: String, g: DriverMember, isB: Boolean, r: String) =
        gamificationRepo.awardPeerPoints(tId, tN, g, isB, r)
    suspend fun claimTask(taskId: String, member: DriverMember) = gamificationRepo.claimTask(taskId, member)
    suspend fun completeTask(taskId: String, memberId: String) = gamificationRepo.completeTask(taskId, memberId)
    suspend fun redeemReward(rewardId: String, member: DriverMember) = gamificationRepo.redeemReward(rewardId, member)
    suspend fun addHazard(hazard: HazardArea) = radarHazardRepo.addHazard(hazard)
    suspend fun confirmHazard(hazardId: String) = radarHazardRepo.confirmHazard(hazardId)
    suspend fun addEmergencyAlert(alert: EmergencyAlert) = emergencyRepo.addEmergencyAlert(alert)
    suspend fun resolveEmergencyAlert(alertId: String, resolvedBy: String) = emergencyRepo.resolveEmergencyAlert(alertId, resolvedBy)
    suspend fun respondToEmergency(alertId: String) = emergencyRepo.respondToEmergency(alertId)
    suspend fun addKasTransaction(transaction: KasTransaction) = communityRepo.addKasTransaction(transaction)
    suspend fun addForumPost(post: ForumPost) = communityRepo.addForumPost(post)
    suspend fun deleteForumPost(postId: String) = communityRepo.deleteForumPost(postId)
    suspend fun toggleLikePost(postId: String, currentLiked: Boolean) = communityRepo.toggleLikePost(postId, currentLiked)
    fun getCommentsForPost(postId: String): Flow<List<ForumComment>> = communityRepo.getCommentsForPost(postId)
    suspend fun addForumComment(comment: ForumComment) = communityRepo.addForumComment(comment)
    suspend fun deleteForumComment(commentId: String) = communityRepo.deleteForumComment(commentId)
    suspend fun updateScreening(memberId: String, status: VerificationStatus, notes: String) = memberRepo.updateScreening(memberId, status, notes)
    suspend fun checkInKopdar(eventId: String, memberId: String, points: Int) = memberRepo.checkInKopdar(eventId, memberId, points)
    suspend fun addReview(review: DriverReview) = memberRepo.addReview(review)
    suspend fun updateMember(member: DriverMember) = memberRepo.updateMember(member)
    suspend fun updateMemberLocation(id: String, lat: Double, lng: Double, status: String = "Online") =
        memberRepo.updateMemberLocation(id, lat, lng, status)
    suspend fun registerMember(member: DriverMember) = memberRepo.registerMember(member)
    suspend fun addNotification(notification: CommunityNotification) = communityRepo.addNotification(notification)
    suspend fun addPosko(posko: PoskoLocation) = communityRepo.addPosko(posko)
    suspend fun saveNotificationPreference(pref: NotificationPreference) = communityRepo.saveNotificationPreference(pref)
    suspend fun recordTileMetadata(tile: MapTileMetadata) = radarHazardRepo.recordTileMetadata(tile)
    suspend fun clearMapTileCache() = radarHazardRepo.clearMapTileCache()
    suspend fun saveAppSetting(setting: AppStateSetting) = radarHazardRepo.saveAppSetting(setting)
}
