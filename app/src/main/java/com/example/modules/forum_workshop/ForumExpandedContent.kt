package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumComment
import com.example.shared.models.ForumPost
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextSecondary

@Composable
fun ForumExpandedContent(
    post: ForumPost,
    canDelete: Boolean,
    onToggleLike: () -> Unit,
    onDelete: () -> Unit,
    currentMemberId: String = "",
    comments: List<ForumComment> = emptyList(),
    onAddComment: (String) -> Unit = {},
    onDeleteComment: (String) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(
            text = post.content,
            fontSize = 12.sp,
            color = DrgTextSecondary,
            lineHeight = 17.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Oleh: ${post.authorName} (${post.authorRole.shortName})",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = DrgTextMuted
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleLike, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = if (post.isLikedByMe) DrgRedDanger else DrgTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (canDelete) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = DrgRedDanger, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        ForumCommentSection(
            comments = comments,
            currentMemberId = currentMemberId,
            onAddComment = onAddComment,
            onDeleteComment = onDeleteComment
        )
    }
}
