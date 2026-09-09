package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.shared.models.ForumPost

@Composable
fun ForumListView(
    posts: List<ForumPost>,
    currentMemberId: String,
    onToggleLike: (String) -> Unit,
    onDeletePost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().testTag("forum_list_view"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
    ) {
        items(posts, key = { it.id }) { post ->
            ForumListRowItem(
                post = post,
                canDelete = post.authorId == currentMemberId,
                onToggleLike = { onToggleLike(post.id) },
                onDelete = { onDeletePost(post.id) }
            )
        }
    }
}
