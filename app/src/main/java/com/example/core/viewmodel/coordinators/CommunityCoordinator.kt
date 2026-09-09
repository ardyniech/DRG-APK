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
            }.onSuccess {
                showToast(if (postType == PostType.QUESTION) "Pertanyaan berhasil diposting." else "Update berhasil dibagikan.")
            }.onFailure { showToast("Gagal memposting: ${it.localizedMessage}") }
        }
    }

    fun deletePost(postId: String) {
        scope.launch {
            runCatching { repository.deleteForumPost(postId) }
                .onSuccess { showToast("Postingan berhasil dihapus.") }
                .onFailure { showToast("Gagal menghapus: ${it.localizedMessage}") }
        }
    }

    fun toggleLike(postId: String, isLiked: Boolean) {
        scope.launch { runCatching { repository.toggleLikePost(postId, isLiked) } }
    }

    fun checkInEvent(cur: DriverMember?, eventId: String) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.checkInKopdar(eventId, cur.id, 50) }
                .onSuccess { showToast("Absensi berhasil! +50 Poin Loyalitas ditambahkan ke akun Anda.") }
                .onFailure { showToast("Gagal absensi: ${it.localizedMessage}") }
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
                .onFailure { showToast("Gagal mengirim ulasan: ${it.localizedMessage}") }
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
                .onSuccess { showToast("Pengumuman berhasil disiarkan!") }
                .onFailure { showToast("Gagal menyiarkan: ${it.localizedMessage}") }
        }
    }

    fun awardPeerPoints(giver: DriverMember?, targetId: String, targetName: String, isBeneficiary: Boolean, reason: String) {
        if (giver == null) return
        scope.launch {
            runCatching { repository.awardPeerPoints(targetId, targetName, giver, isBeneficiary, reason) }
                .onSuccess { showToast("Apresiasi poin berhasil diberikan ke $targetName!") }
                .onFailure { showToast("Gagal memberi poin: ${it.localizedMessage}") }
        }
    }

    fun claimTask(cur: DriverMember?, taskId: String) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.claimTask(taskId, cur) }
                .onSuccess { showToast("Tugas komunitas berhasil diambil!") }
                .onFailure { showToast("Gagal mengambil tugas: ${it.localizedMessage}") }
        }
    }

    fun completeTask(cur: DriverMember?, taskId: String) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.completeTask(taskId, cur.id) }
                .onSuccess { showToast("Tugas selesai & poin ditambahkan!") }
                .onFailure { showToast("Gagal menyelesaikan tugas: ${it.localizedMessage}") }
        }
    }

    fun redeemReward(cur: DriverMember?, rewardId: String) {
        if (cur == null) return
        scope.launch {
            runCatching { repository.redeemReward(rewardId, cur) }
                .onSuccess { showToast("Keuntungan komunitas berhasil ditukar!") }
                .onFailure { showToast("Gagal menukar voucher: ${it.localizedMessage}") }
        }
    }
}
