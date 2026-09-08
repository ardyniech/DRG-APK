package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CommunityRepository(private val db: AppDatabase) {
    val allPosko: Flow<List<PoskoLocation>> = db.poskoDao().getAllPosko()
    val allTransactions: Flow<List<KasTransaction>> = db.kasDao().getAllTransactions()
    val allPosts: Flow<List<ForumPost>> = db.forumDao().getAllPosts()
    val allWorkshops: Flow<List<WorkshopPartner>> = db.workshopDao().getAllWorkshops()
    val allEvents: Flow<List<AttendanceEvent>> = db.attendanceDao().getAllEvents()
    val allNotifications: Flow<List<CommunityNotification>> = db.notificationDao().getAllNotifications()

    fun getPrefForMember(memberId: String): Flow<NotificationPreference?> =
        db.notificationPrefDao().getPrefForMember(memberId)

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

    suspend fun addNotification(notification: CommunityNotification) = withContext(Dispatchers.IO) {
        db.notificationDao().insertNotification(notification)
    }

    suspend fun saveNotificationPreference(pref: NotificationPreference) = withContext(Dispatchers.IO) {
        db.notificationPrefDao().savePref(pref)
    }
}
