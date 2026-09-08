package com.example.core.viewmodel.coordinators

import com.example.core.repository.DRGRepository
import com.example.core.sync.BackgroundSyncEngine
import com.example.shared.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

class CommunityCoordinator(
    private val repository: DRGRepository,
    private val scope: CoroutineScope,
    private val syncEngine: BackgroundSyncEngine,
    private val showToast: (String) -> Unit
) {
    fun createPost(cur: DriverMember?, title: String, content: String, cat: ForumCategory, postType: PostType) {
        if (cur == null) return
        val post = ForumPost(
            id = "FRM-${UUID.randomUUID().toString().take(8)}",
            authorId = cur.id, authorName = cur.name, authorRole = cur.role,
            category = cat, title = title, content = content, postType = postType,
            timeAgo = "Baru saja", timestamp = System.currentTimeMillis()
        )
        scope.launch {
            repository.addForumPost(post)
            syncEngine.enqueueOptimisticAction("FORUM", post.id, post.title)
            showToast(if (postType == PostType.QUESTION) "Pertanyaan berhasil diposting." else "Update berhasil dibagikan.")
        }
    }

    fun deletePost(postId: String) {
        scope.launch { repository.deleteForumPost(postId); showToast("Postingan berhasil dihapus.") }
    }

    fun toggleLike(postId: String, isLiked: Boolean) {
        scope.launch { repository.toggleLikePost(postId, isLiked) }
    }

    fun checkInEvent(cur: DriverMember?, eventId: String) {
        if (cur == null) return
        scope.launch {
            repository.checkInKopdar(eventId, cur.id, 50)
            showToast("Absensi berhasil! +50 Poin Loyalitas ditambahkan ke akun Anda.")
        }
    }

    fun submitDriverReview(cur: DriverMember?, targetId: String, rating: Float, tag: String, comment: String) {
        if (cur == null) return
        val review = DriverReview(
            id = "REV-${UUID.randomUUID().toString().take(8)}",
            targetDriverId = targetId, reviewerName = cur.name, reviewerRole = cur.role,
            rating = rating, tag = tag, comment = comment, dateText = "Hari ini"
        )
        scope.launch { repository.addReview(review); showToast("Ulasan etika berhasil dikirim.") }
    }

    fun sendNotification(cur: DriverMember?, title: String, message: String, severity: NotificationSeverity) {
        if (cur == null) return
        val notif = CommunityNotification(
            id = "NTF-${UUID.randomUUID().toString().take(8)}",
            title = title, message = message, severity = severity,
            senderName = cur.name, senderRole = cur.role, timeAgo = "Baru saja"
        )
        scope.launch { repository.addNotification(notif); showToast("Pengumuman berhasil disiarkan!") }
    }

    fun awardPeerPoints(giver: DriverMember?, targetId: String, targetName: String, isBeneficiary: Boolean, reason: String) {
        if (giver == null) return
        scope.launch {
            repository.awardPeerPoints(targetId, targetName, giver, isBeneficiary, reason)
            showToast("Apresiasi poin berhasil diberikan ke $targetName!")
        }
    }

    fun claimTask(cur: DriverMember?, taskId: String) {
        if (cur == null) return
        scope.launch { repository.claimTask(taskId, cur); showToast("Tugas komunitas berhasil diambil!") }
    }

    fun completeTask(cur: DriverMember?, taskId: String) {
        if (cur == null) return
        scope.launch { repository.completeTask(taskId, cur.id); showToast("Tugas selesai & poin ditambahkan!") }
    }

    fun redeemReward(cur: DriverMember?, rewardId: String) {
        if (cur == null) return
        scope.launch { repository.redeemReward(rewardId, cur); showToast("Keuntungan komunitas berhasil ditukar!") }
    }
}
