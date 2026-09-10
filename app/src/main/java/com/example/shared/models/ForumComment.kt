package com.example.shared.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "forum_comments",
    indices = [
        Index(value = ["postId"]),
        Index(value = ["timestamp"])
    ]
)
data class ForumComment(
    @PrimaryKey val id: String,
    val postId: String,
    val authorId: String,
    val authorName: String,
    val authorRole: MemberRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val timeAgo: String = "Baru saja"
)
