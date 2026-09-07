package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ForumCategory(val label: String, val badgeColorHex: Long) {
    TANYA_JAWAB("Tanya Driver / Q&A", 0xFFE65100),
    INFO_JALUR("Info Jalur & Pantauan", 0xFF0288D1),
    TIPS_MESIN("Tips & Rawat Motor", 0xFF00875A),
    INFO_GACOR("Info Spot & Jam Ramai", 0xFFFF8F00),
    SAPA_REKAN("Sapa Rekan & Sharing", 0xFF7B1FA2)
}

enum class PostType(val label: String, val iconName: String) {
    UPDATE("Update Terkini", "Campaign"),
    QUESTION("Tanya Rekan", "HelpOutline")
}

@Entity(tableName = "forum_posts")
data class ForumPost(
    @PrimaryKey val id: String,
    val authorId: String,
    val authorName: String,
    val authorRole: MemberRole,
    val category: ForumCategory,
    val title: String,
    val content: String,
    val postType: PostType = PostType.UPDATE,
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val commentsCount: Int = 0,
    val timeAgo: String = "Baru saja",
    val timestamp: Long = System.currentTimeMillis()
)
