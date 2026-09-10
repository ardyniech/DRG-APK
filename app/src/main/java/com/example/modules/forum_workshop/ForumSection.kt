package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumCategory
import com.example.shared.models.ForumComment
import com.example.shared.models.ForumPost
import com.example.shared.models.PostType
import com.example.ui.theme.DrgTextSecondary

@Composable
fun ForumSection(
    posts: List<ForumPost>,
    currentMemberId: String,
    onCreatePostClick: (PostType) -> Unit,
    onToggleLike: (String, Boolean) -> Unit,
    onDeletePost: (String) -> Unit,
    commentsMap: Map<String, List<ForumComment>> = emptyMap(),
    onAddComment: (String, String) -> Unit = { _, _ -> },
    onDeleteComment: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ForumCategory?>(null) }
    var selectedPostType by remember { mutableStateOf<PostType?>(null) }
    var isListView by remember { mutableStateOf(true) }

    val filteredPosts = remember(posts, searchQuery, selectedCategory, selectedPostType) {
        posts.filter { post ->
            val matchesQuery = searchQuery.isBlank() ||
                post.title.contains(searchQuery, ignoreCase = true) ||
                post.content.contains(searchQuery, ignoreCase = true) ||
                post.authorName.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || post.category == selectedCategory
            val matchesType = selectedPostType == null || post.postType == selectedPostType
            matchesQuery && matchesCategory && matchesType
        }
    }

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ForumActionBanner(
            onCreateUpdateClick = { onCreatePostClick(PostType.UPDATE) },
            onAskQuestionClick = { onCreatePostClick(PostType.QUESTION) }
        )

        ForumHeaderAndFilters(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedCategory = selectedCategory,
            onSelectCategory = { selectedCategory = it },
            selectedPostType = selectedPostType,
            onSelectPostType = { selectedPostType = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isListView) "Daftar Update & Waktu" else "Kartu Diskusi Lengkap", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(selected = isListView, onClick = { isListView = true }, label = { Text("Daftar Update", fontSize = 10.sp, fontWeight = FontWeight.Bold) })
                FilterChip(selected = !isListView, onClick = { isListView = false }, label = { Text("Kartu Lengkap", fontSize = 10.sp, fontWeight = FontWeight.Bold) })
            }
        }

        if (filteredPosts.isEmpty()) {
            ForumEmptyState(
                isFiltered = searchQuery.isNotBlank() || selectedCategory != null || selectedPostType != null,
                onCreatePostClick = { onCreatePostClick(PostType.UPDATE) },
                onAskQuestionClick = { onCreatePostClick(PostType.QUESTION) }
            )
        } else if (isListView) {
            ForumListView(
                posts = filteredPosts,
                currentMemberId = currentMemberId,
                onToggleLike = { postId ->
                    posts.firstOrNull { it.id == postId }?.let { onToggleLike(postId, it.isLikedByMe) }
                },
                onDeletePost = onDeletePost,
                commentsMap = commentsMap,
                onAddComment = onAddComment,
                onDeleteComment = onDeleteComment,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredPosts, key = { it.id }) { post ->
                    ForumPostCard(
                        post = post,
                        onToggleLike = { onToggleLike(post.id, post.isLikedByMe) },
                        onDeletePost = if (post.authorId == currentMemberId) { { onDeletePost(post.id) } } else null
                    )
                }
            }
        }
    }
}
