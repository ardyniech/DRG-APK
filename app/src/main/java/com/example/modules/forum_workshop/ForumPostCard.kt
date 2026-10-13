package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.ForumPost
import com.example.shared.models.PostType
import com.example.ui.theme.*

@Composable
fun ForumPostCard(
    post: ForumPost,
    onToggleLike: () -> Unit,
    onDeletePost: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.2f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
            .testTag("forum_post_card_${post.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ForumPostCardHeader(post = post, onDeletePost = onDeletePost)

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                val isQuestion = post.postType == PostType.QUESTION
                val typeBg = if (isQuestion) DrgAmberWarning.copy(alpha = 0.15f) else DrgBlueInfo.copy(alpha = 0.15f)
                val typeColor = if (isQuestion) DrgAmberWarning else DrgBlueInfo
                val typeLabel = if (isQuestion) "❓ PERTANYAAN" else "📢 UPDATE"

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(typeBg)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(text = typeLabel, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = typeColor)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(post.category.badgeColorHex).copy(alpha = 0.14f))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(text = post.category.label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(post.category.badgeColorHex))
                }
            }

            Text(text = post.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
            Text(text = post.content, fontSize = 12.sp, color = DrgTextSecondary, lineHeight = 17.sp)

            HorizontalDivider(color = DrgOutline.copy(alpha = 0.4f), thickness = 0.8.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onToggleLike() }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Suka",
                        tint = if (post.isLikedByMe) DrgRedDanger else DrgTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${post.likesCount} Suka", fontSize = 11.sp, color = DrgTextSecondary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = "Komentar", tint = DrgTextMuted, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${post.commentsCount} Diskusi", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }
    }
}
