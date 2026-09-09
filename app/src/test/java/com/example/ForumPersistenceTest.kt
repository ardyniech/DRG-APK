package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.database.AppDatabase
import com.example.shared.models.ForumCategory
import com.example.shared.models.ForumPost
import com.example.shared.models.MemberRole
import com.example.shared.models.PostType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ForumPersistenceTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndRetrieveQuestionAndPost() = runBlocking {
        val forumDao = db.forumDao()

        val updatePost = ForumPost(
            id = "FRM-01",
            authorId = "DRG-001",
            authorName = "Bambang",
            authorRole = MemberRole.KETUA,
            category = ForumCategory.INFO_JALUR,
            title = "Jalur Alternatif Macet",
            content = "Gunakan rute Dinoyo - Tlogomas",
            postType = PostType.UPDATE
        )

        val questionPost = ForumPost(
            id = "FRM-02",
            authorId = "DRG-002",
            authorName = "Rudi",
            authorRole = MemberRole.ANGGOTA,
            category = ForumCategory.TANYA_JAWAB,
            title = "Oli Terbaik Tarikan Siang?",
            content = "Mohon rekomendasi oli yang tahan panas",
            postType = PostType.QUESTION
        )

        forumDao.insertPost(updatePost)
        forumDao.insertPost(questionPost)

        val posts = forumDao.getAllPosts().first()
        assertEquals(2, posts.size)
        assertTrue(posts.any { it.postType == PostType.QUESTION })
        assertTrue(posts.any { it.postType == PostType.UPDATE })
    }

    @Test
    fun testToggleLikeAndDeletion() = runBlocking {
        val forumDao = db.forumDao()

        val post = ForumPost(
            id = "FRM-03",
            authorId = "DRG-005",
            authorName = "Anton",
            authorRole = MemberRole.SATGAS,
            category = ForumCategory.TIPS_MESIN,
            title = "Tips V-Belt",
            content = "Cek rutin tiap 10rb km",
            likesCount = 5,
            isLikedByMe = false
        )

        forumDao.insertPost(post)
        forumDao.toggleLike("FRM-03", 1, true)

        val afterLike = forumDao.getAllPosts().first().find { it.id == "FRM-03" }
        assertNotNull(afterLike)
        assertEquals(6, afterLike?.likesCount)
        assertEquals(true, afterLike?.isLikedByMe)

        forumDao.deletePost("FRM-03")
        val afterDelete = forumDao.getAllPosts().first()
        assertTrue(afterDelete.none { it.id == "FRM-03" })
    }
}
