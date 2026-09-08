package com.example.core.repository

import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GamificationRepository(private val db: AppDatabase) {
    val allBadges: Flow<List<BadgeItem>> = db.gamificationDao().getAllBadges()
    val allTasks: Flow<List<CommunityTask>> = db.gamificationDao().getAllTasks()
    val allRewards: Flow<List<RewardItem>> = db.gamificationDao().getAllRewards()
    val allPointTransactions: Flow<List<PointTransaction>> = db.gamificationDao().getAllPointTransactions()

    fun calculatePointWeight(giverRole: MemberRole, isBeneficiary: Boolean): Int {
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
            id = "PTX-${java.util.UUID.randomUUID().toString().replace("-", "").take(8)}",
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
            id = "NTF-${java.util.UUID.randomUUID().toString().replace("-", "").take(8)}",
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
        val task = db.gamificationDao().getTaskById(taskId) ?: return@withContext
        val updated = task.copy(
            assignedMemberId = member.id,
            assignedMemberName = member.name,
            status = TaskStatus.IN_PROGRESS
        )
        db.gamificationDao().updateTask(updated)
    }

    suspend fun completeTask(taskId: String, memberId: String) = withContext(Dispatchers.IO) {
        val task = db.gamificationDao().getTaskById(taskId) ?: return@withContext
        val updated = task.copy(status = TaskStatus.VERIFIED)
        db.gamificationDao().updateTask(updated)
        db.memberDao().addLoyaltyPoints(memberId, task.rewardPoints)

        val notif = CommunityNotification(
            id = "NTF-${java.util.UUID.randomUUID().toString().replace("-", "").take(8)}",
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
        val reward = db.gamificationDao().getRewardById(rewardId) ?: return@withContext
        if (member.loyaltyPoints >= reward.requiredPoints && reward.stockAvailable > 0) {
            db.memberDao().addLoyaltyPoints(member.id, -reward.requiredPoints)
            db.gamificationDao().updateReward(reward.copy(stockAvailable = reward.stockAvailable - 1, isRedeemed = true))
        }
    }
}
