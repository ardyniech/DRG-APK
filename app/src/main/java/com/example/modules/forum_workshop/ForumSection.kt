package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.models.ForumCategory
import com.example.shared.models.ForumPost
import com.example.shared.models.PostType

@Composable
fun ForumSection(
    posts: List<ForumPost>,
    currentMemberId: String,
    onCreatePostClick: (PostType) -> Unit,
    onToggleLike: (String, Boolean) -> Unit,
    onDeletePost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ForumCategory?>(null) }
    var selectedPostType by remember { mutableStateOf<PostType?>(null) }

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

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 28.dp)
    ) {
        item {
            ForumActionBanner(
                onCreateUpdateClick = { onCreatePostClick(PostType.UPDATE) },
                onAskQuestionClick = { onCreatePostClick(PostType.QUESTION) }
            )
        }

        item {
            ForumHeaderAndFilters(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectedCategory = selectedCategory,
                onSelectCategory = { selectedCategory = it },
                selectedPostType = selectedPostType,
                onSelectPostType = { selectedPostType = it }
            )
        }

        if (filteredPosts.isEmpty()) {
            item {
                ForumEmptyState(
                    isFiltered = searchQuery.isNotBlank() || selectedCategory != null || selectedPostType != null,
                    onCreatePostClick = { onCreatePostClick(PostType.UPDATE) },
                    onAskQuestionClick = { onCreatePostClick(PostType.QUESTION) }
                )
            }
        } else {
            items(filteredPosts, key = { it.id }) { post ->
                val canDelete = post.authorId == currentMemberId
                ForumPostCard(
                    post = post,
                    onToggleLike = { onToggleLike(post.id, post.isLikedByMe) },
                    onDeletePost = if (canDelete) { { onDeletePost(post.id) } } else null
                )
            }
        }
    }
}
