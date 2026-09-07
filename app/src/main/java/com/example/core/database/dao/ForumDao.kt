package com.example.core.database.dao

import androidx.room.*
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
}
