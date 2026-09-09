package com.example.modules.forum_workshop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.HelpOutline
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
import com.example.ui.theme.DrgAmberWarning
import com.example.ui.theme.DrgBlueInfo
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgSurface
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary

@Composable
fun ForumListRowItem(
    post: ForumPost,
    canDelete: Boolean,
    onToggleLike: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Waktu",
                        tint = DrgTextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = post.timeAgo,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = DrgTextMuted
                    )
                }
            }

            Text(
                text = post.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DrgTextPrimary,
                maxLines = if (isExpanded) 3 else 1,
                overflow = TextOverflow.Ellipsis
            )

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
