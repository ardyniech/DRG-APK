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
            runCatching {
                repository.addForumPost(post)
                syncEngine.enqueueOptimisticAction("FORUM", post.id, post.title)
            }.onSuccess { showToast(if (postType == PostType.QUESTION) "Pertanyaan diposting." else "Update dibagikan.") }
             .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun deletePost(postId: String) = scope.launch {
        runCatching { repository.deleteForumPost(postId) }
            .onSuccess { showToast("Postingan dihapus.") }
    }

    fun toggleLike(postId: String, isLiked: Boolean) = scope.launch {
        runCatching { repository.toggleLikePost(postId, isLiked) }
    }

    fun addComment(cur: DriverMember?, postId: String, content: String) {
        if (cur == null || content.isBlank()) return
        val comment = ForumComment(
            id = "CMT-${UUID.randomUUID().toString().take(8)}",
            postId = postId, authorId = cur.id, authorName = cur.name,
            authorRole = cur.role, content = content.trim(), timestamp = System.currentTimeMillis()
        )
        scope.launch {
            runCatching { repository.addForumComment(comment) }
                .onSuccess { showToast("Balasan terkirim.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun deleteComment(commentId: String) = scope.launch {
        runCatching { repository.deleteForumComment(commentId) }
            .onSuccess { showToast("Balasan dihapus.") }
    }

    fun checkInEvent(cur: DriverMember?, eventId: String) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.checkInKopdar(eventId, cur.id, 50) }
                .onSuccess { showToast("Absensi berhasil! +50 Poin Loyalitas.") }
                .onFailure { showToast("Gagal: ${it.localizedMessage}") }
        }
    }

    fun submitDriverReview(cur: DriverMember?, targetId: String, rating: Float, tag: String, comment: String) {
        if (cur == null) return
        val review = DriverReview(
            id = "REV-${UUID.randomUUID().toString().take(8)}",
            targetDriverId = targetId, reviewerName = cur.name, reviewerRole = cur.role,
            rating = rating, tag = tag, comment = comment, dateText = "Hari ini"
        )
        scope.launch {
            runCatching { repository.addReview(review) }
                .onSuccess { showToast("Ulasan etika berhasil dikirim.") }
        }
    }

    fun sendNotification(cur: DriverMember?, title: String, message: String, severity: NotificationSeverity) {
        if (cur == null) return
        val notif = CommunityNotification(
            id = "NTF-${UUID.randomUUID().toString().take(8)}",
            title = title, message = message, severity = severity,
            senderName = cur.name, senderRole = cur.role, timeAgo = "Baru saja"
        )
        scope.launch {
            runCatching { repository.addNotification(notif) }
                .onSuccess { showToast("Pengumuman disiarkan!") }
        }
    }

    fun awardPeerPoints(giver: DriverMember?, targetId: String, targetName: String, isBeneficiary: Boolean, reason: String) {
        if (giver == null) return
        scope.launch {
            runCatching { repository.awardPeerPoints(targetId, targetName, giver, isBeneficiary, reason) }
                .onSuccess { showToast("Poin diberikan ke $targetName!") }
        }
    }

    fun claimTask(cur: DriverMember?, taskId: String) = scope.launch {
        if (cur != null) runCatching { repository.claimTask(taskId, cur) }.onSuccess { showToast("Tugas diambil!") }
    }

    fun completeTask(cur: DriverMember?, taskId: String) = scope.launch {
        if (cur != null) runCatching { repository.completeTask(taskId, cur.id) }.onSuccess { showToast("Tugas selesai & poin diterima!") }
    }

    fun redeemReward(cur: DriverMember?, rewardId: String) = scope.launch {
        if (cur != null) runCatching { repository.redeemReward(rewardId, cur) }.onSuccess { showToast("Voucher berhasil ditukar!") }
    }
}
