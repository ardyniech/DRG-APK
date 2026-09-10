package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.ForumComment
import com.example.shared.models.ForumPost
import kotlinx.coroutines.flow.Flow

@Dao
interface ForumDao {
    @Query("SELECT * FROM forum_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<ForumPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ForumPost>)

    @Query("UPDATE forum_posts SET likesCount = likesCount + :delta, isLikedByMe = :isLiked WHERE id = :id")
    suspend fun toggleLike(id: String, delta: Int, isLiked: Boolean)

    @Query("DELETE FROM forum_posts WHERE id = :id")
    suspend fun deletePost(id: String)

    @Query("SELECT * FROM forum_comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: String): Flow<List<ForumComment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: ForumComment)

    @Query("UPDATE forum_posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
    suspend fun incrementCommentsCount(postId: String)

    @Query("DELETE FROM forum_comments WHERE id = :commentId")
    suspend fun deleteComment(commentId: String)
}
