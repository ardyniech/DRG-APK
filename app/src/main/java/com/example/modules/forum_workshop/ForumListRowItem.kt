package com.example.modules.forum_workshop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumPost
import com.example.shared.models.PostType
import com.example.ui.theme.*

@Composable
fun ForumListRowItem(
    post: ForumPost,
    canDelete: Boolean,
    onToggleLike: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .clickable { isExpanded = !isExpanded }
            .testTag("forum_list_item_${post.id}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val isQuestion = post.postType == PostType.QUESTION
                    Icon(
                        imageVector = if (isQuestion) Icons.Default.HelpOutline else Icons.Default.Campaign,
                        contentDescription = post.postType.label,
                        tint = if (isQuestion) DrgAmberWarning else DrgBlueInfo,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.category.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(post.category.badgeColorHex)
                    )
                }
                Text(
                    text = post.timeAgo,
                    fontSize = 10.sp,
                    color = DrgTextMuted
                )
            }

            Text(
                text = post.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DrgTextPrimary,
                maxLines = if (isExpanded) 4 else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (!isExpanded) {
                Text(
                    text = post.content,
                    fontSize = 11.sp,
                    color = DrgTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Oleh: ${post.authorName} (${post.authorRole.shortName})",
                        fontSize = 10.sp,
                        color = DrgTextMuted
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Suka",
                            tint = if (post.isLikedByMe) DrgRedDanger else DrgTextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(text = "${post.likesCount}", fontSize = 10.sp, color = DrgTextMuted)
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                ForumExpandedContent(
                    post = post,
                    canDelete = canDelete,
                    onToggleLike = onToggleLike,
                    onDelete = onDelete
                )
            }
        }
    }
}
